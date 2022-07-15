package com.example.foodie.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.foodie.data.Repository
import com.example.foodie.data.database.RecipesEntity
import com.example.foodie.models.FoodRecipe
import com.example.foodie.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel // since we need to setup a ViewModelFactory to pass in parameters in the constructor of a ViewModel, such as passing in Repository here, this annotation tells Hilt to automatically create a ViewModelFactory for us.
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE */
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) = viewModelScope.launch {
        repository.local.insertRecipes(recipesEntity)
    }

    /** RETROFIT */
    val foodRecipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    // in safe API calls, we handle errors and exceptions gracefully
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        foodRecipeResponse.value = NetworkResult.Loading()
        Log.d("MainViewModel", "hasInternetConnection() called ${hasInternetConnection()}")

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                foodRecipeResponse.value = handleFoodRecipeResponse(response)

                val foodRecipes = foodRecipeResponse.value!!.data
                if (foodRecipes != null) { // here we perform the offline cache, only and only when we retrieve data from our API (i.e. when foodRecipeResponse.value!!.data isn't null)
                    offlineCacheRecipes(foodRecipes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                foodRecipeResponse.value = NetworkResult.Error("HTTP or IO Exception Occurred.")
            }
        } else {
            foodRecipeResponse.value = NetworkResult.Error("No Internet Connection Available.")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipeResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        return when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("API Timeout.")
            response.code() == 402 -> NetworkResult.Error("API Calls Daily Quota Reached. Payment Required.")
            response.code() ==  401 -> NetworkResult.Error("You are not authorized.\nPlease authenticate API key.")
            response.body()?.results.isNullOrEmpty() -> NetworkResult.Error("Recipe Not Found.")
            response.isSuccessful -> NetworkResult.Success(response.body()!!)
            else -> NetworkResult.Error(response.message())
        }
    }

    // returns true when device is connected to wifi, cellular or ethernet, false otherwise
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}
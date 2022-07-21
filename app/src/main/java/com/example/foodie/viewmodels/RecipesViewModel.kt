package com.example.foodie.viewmodels

import android.app.Application
import androidx.datastore.dataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodie.*
import com.example.foodie.data.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var defaultMealType = DEFAULT_MEAL_TYPE
    private var defaultDietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(
                mealType = mealType,
                mealTypeId = mealTypeId,
                dietType = dietType,
                dietTypeId = dietTypeId
            )
        }

    fun applyQueries(): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect {
            defaultMealType = it.selectedMealType
            defaultDietType = it.selectedDietType
            }
        }

        queries.set("apiKey", BuildConfig.API_KEY) // same as below, but using regular set() method instead of Kotlin's indexing operator syntax.
        queries[NUMBER_QUERY] = DEFAULT_RECIPES_NUMBER
        queries[TYPE_QUERY] = defaultDietType
        queries[DIET_QUERY] = defaultDietType
        queries[ADD_RECIPE_INFORMATION_QUERY] = "true"
        queries[FILL_INGREDIENTS_QUERY] = "true"
        return queries
    }

}
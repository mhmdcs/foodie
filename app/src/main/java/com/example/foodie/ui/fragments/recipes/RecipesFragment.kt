package com.example.foodie.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie.BuildConfig
import com.example.foodie.viewmodels.MainViewModel
import com.example.foodie.R
import com.example.foodie.adapter.RecipesAdapter
import com.example.foodie.utils.NetworkResult
import com.example.foodie.viewmodels.RecipesViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mView: View
    private val adapter by lazy { RecipesAdapter() } // lazy initialization means that the object's initialization will be delayed until the first time it's used/accessed
    private lateinit var recyclerView: RecyclerView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initializing ViewModels in onCreate() or onCreateView() doesn't make that much of a difference, but you should usually initialize your general objects here, and your view-related objects in onCreateView() just to keep your Fragment all nice and tidy.
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java] // same as ViewModelProvider(requireActivity()).get(MainViewModel::class.java) but using Kotlin's indexing operator for methods named set/get. Note: we pass in either `this` or requireActivity() depending on whether or not we want to share the same ViewModel instance across multiple fragments that are hosted under the same activity, and we want to precisely do that with our MainViewModel since it'll be used on multiple fragments
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)

        recyclerView = mView.findViewById(R.id.recipes_recyclerview)
        shimmerFrameLayout = mView.findViewById(R.id.shimmer_frame_layout)

        setupRecyclerView()
        readRecipesFromDatabase()

        mainViewModel.foodRecipeResponse.observe(viewLifecycleOwner) { response ->
            when(response){
                is NetworkResult.Success -> response.data?.let {
                    hideShimmerEffect()
                    adapter.setData(it) //this could probably be commented out, since our local database is our sole, single source of truth and we always display data from it, and shouldn't display data from anywhere else.
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    readRecipesFromDatabase() // call the offline cache here so that when there's an error with the request, we still display to the user data from the offline cache in the local database
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> { showShimmerEffect() }
            }
        }

        return mView
    }

        private fun setupRecyclerView() {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            showShimmerEffect()
        }

    // load data from the offline cache in local database. don't forget to launch a coroutine for this one to make mainViewModel.readRecipes suspend the coroutine until it finishes its work :)
    private fun readRecipesFromDatabase() = lifecycleScope.launch {
        mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
            if(database.isNotEmpty()){ // if our database is not empty (i.e. it contains records) then we want to display its data inside our recyclerview
                Log.i("RecipesFragment", "readRecipesFromDatabase called")
                val foodRecipe = database.get(0).foodRecipe // here we specify index 0 because we'll only have one row in our recipes table and it'll always be the first row i.e. 0 index.
                adapter.setData(foodRecipe) // tell recyclerview adapter to set its data from our database FoodRecipe
                hideShimmerEffect()
            } else { // else, if the database IS empty, then request new data from the API
                requestRecipesFromApi()
            }
       }
    }

    private fun requestRecipesFromApi() {
        Log.i("RecipesFragment", "requestRecipesFromApi called")
        val queries = recipesViewModel.applyQueries()
        mainViewModel.getRecipes(queries)
    }


    private fun showShimmerEffect(){
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideShimmerEffect(){
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

}
package com.example.foodie.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie.BuildConfig
import com.example.foodie.MainViewModel
import com.example.foodie.R
import com.example.foodie.adapter.RecipesAdapter
import com.example.foodie.utils.NetworkResult
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mView: View
    private val adapter by lazy { RecipesAdapter() }
    private lateinit var recyclerView: RecyclerView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)

        recyclerView = mView.findViewById(R.id.recipes_recyclerview)
        shimmerFrameLayout = mView.findViewById(R.id.shimmer_frame_layout)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java] // same as ViewModelProvider(this).get(MainViewModel::class.java)

        setupRecyclerView()
        requestRecipesFromApi()

        mainViewModel.foodRecipeResponse.observe(viewLifecycleOwner) { response ->
            when(response){
                is NetworkResult.Success -> response.data?.let {
                    adapter.setData(it) // hide shimmer effect here
                }
                is NetworkResult.Error -> Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show() // hide shimmer effect here
                is NetworkResult.Loading -> {} // show shimmer effect here
            }
        }

        return mView
    }

    private fun requestRecipesFromApi() {
        val queries = applyQueries()
        mainViewModel.getRecipes(queries)
    }

    private fun applyQueries(): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries.set("apiKey", BuildConfig.API_KEY)
        queries["number"] = "50"
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"
        return queries
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // show shimmer effect here
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
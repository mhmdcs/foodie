package com.example.foodie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie.databinding.RecipeRowItemBinding
import com.example.foodie.models.FoodRecipe
import com.example.foodie.models.Result

class RecipesAdapter: RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    private var recipes = emptyList<Result>()

    class RecipesViewHolder(private val binding: RecipeRowItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecipesViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeRowItemBinding.inflate(layoutInflater, parent, false)
                return RecipesViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        return RecipesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
       val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(foodRecipe: FoodRecipe){
        val recipesDiffUtil = RecipesDiffUtil(recipes, foodRecipe.results)
        val recipesDiffResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes =  foodRecipe.results
        recipesDiffResult.dispatchUpdatesTo(this)
      //  notifyDataSetChanged() // notify RecyclerView that data has been changed and that it needs to redraw the whole UI. This is an expensive operation. Instead of it, create a DiffUtil class which calculate the difference between an old and a new list's content and to make RecyclerView Adapter only redraw the new item(s) on the screen.
    }
}
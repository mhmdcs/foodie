package com.example.foodie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
       val result = recipes[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(foodRecipe: FoodRecipe){
        recipes =  foodRecipe.results
        notifyDataSetChanged() // notify RecyclerView that data has been changed and it needs to redraw the whole UI
    }
}
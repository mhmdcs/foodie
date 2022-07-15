package com.example.foodie.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foodie.data.database.RecipesEntity
import com.example.foodie.models.FoodRecipe
import com.example.foodie.utils.NetworkResult

class FragmentRecipesBindingAdapters {
    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun displayErrorImageView(imageView: ImageView, apiResponse: NetworkResult<FoodRecipe>?, database: List<RecipesEntity>?) {
            Log.d("FragmentRecipesBindingAdapters", " 0 - Do I get called?")

            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                Log.d("FragmentRecipesBindingAdapters", " 1 - Do I get called?")
                imageView.visibility = View.VISIBLE
            } else if(apiResponse is NetworkResult.Success){
                Log.d("FragmentRecipesBindingAdapters", " 2 - Do I get called?")
                imageView.visibility = View.INVISIBLE
            } else if(apiResponse is NetworkResult.Loading){
                Log.d("FragmentRecipesBindingAdapters", " 3 - Do I get called?")
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponse", "readDatabase")
        @JvmStatic
        fun displayErrorTextView(textView: TextView, apiResponse: NetworkResult<FoodRecipe>?, database: List<RecipesEntity>?) {
            Log.d("FragmentRecipesBindingAdapters", " 4 - Do I get called?")
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                Log.d("FragmentRecipesBindingAdapters", " 5 - Do I get called?")
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if(apiResponse is NetworkResult.Success){
                Log.d("FragmentRecipesBindingAdapters", " 6 - Do I get called?")
                textView.visibility = View.INVISIBLE
            } else if(apiResponse is NetworkResult.Loading){
                Log.d("FragmentRecipesBindingAdapters", " 7 - Do I get called?")
                textView.visibility = View.INVISIBLE
            }
        }

    }
}
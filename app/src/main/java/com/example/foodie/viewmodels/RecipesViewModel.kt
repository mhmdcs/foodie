package com.example.foodie.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.foodie.*

class RecipesViewModel(application: Application): AndroidViewModel(application) {

     fun applyQueries(): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries.set("apiKey", BuildConfig.API_KEY) // same as below, but using regular set() method instead of Kotlin's indexing operator syntax.
        queries[NUMBER_QUERY] = DEFAULT_RECIPES_NUMBER
        queries[TYPE_QUERY] = DEFAULT_MEAL_TYPE
        queries[DIET_QUERY] = DEFAULT_DIET_TYPE
        queries[ADD_RECIPE_INFORMATION_QUERY] = "true"
        queries[FILL_INGREDIENTS_QUERY] = "true"
        return queries
    }

}
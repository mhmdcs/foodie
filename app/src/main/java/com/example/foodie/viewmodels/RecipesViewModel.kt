package com.example.foodie.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.foodie.*

class RecipesViewModel(application: Application): AndroidViewModel(application) {

     fun applyQueries(): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries.set("apiKey", BuildConfig.API_KEY) // same as below, but using regular set() method instead of Kotlin's indexing operator syntax.
        queries[NUMBER_QUERY] = "50"
        queries[TYPE_QUERY] = "snack"
        queries[DIET_QUERY] = "vegan"
        queries[ADD_RECIPE_INFORMATION_QUERY] = "true"
        queries[FILL_INGREDIENTS_QUERY] = "true"
        return queries
    }

}
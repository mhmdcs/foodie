package com.example.foodie

// API constants
const val BASE_URL = "https://api.spoonacular.com/"

// @QueryMap keys
const val NUMBER_QUERY = "number"
const val TYPE_QUERY = "type"
const val DIET_QUERY = "diet"
const val ADD_RECIPE_INFORMATION_QUERY = "addRecipeInformation"
const val FILL_INGREDIENTS_QUERY = "fillIngredients"

// Room database consts
const val  FOODIE_DATABASE = "foodie_database.db" // it doesn't matter if your sqlite database file extension is .db, .db3, .sqlite, .sqlite3 or anything else or nothing at all, it's just a naming convention. Really down to personal preference.
const val  RECIPES_TABLE =  "recipes_table"
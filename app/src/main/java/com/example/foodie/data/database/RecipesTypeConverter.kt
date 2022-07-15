package com.example.foodie.data.database

import androidx.room.TypeConverter
import com.example.foodie.models.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// to actually store our FoodRecipe inside our database, we need to create a Type Converter because Room only accepts primitive values
// and doesn't accept complex objects, so we will convert(serialize) our FoodRecipe from a complex object to JSON (basically a String)
// then we will store the String in our database. And when we read from our database we'll deserialize by converting back from this JSON String into our lovely FoodRecipe Kotlin object :)
// don't forget to let Room know about our Type Converters in the FoodieDatabase class with @TypeConverters annotation.
class RecipesTypeConverter {

    val gson = Gson()

    // serializing from complex object to JSON String
    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    // deserializing from JSON String to complex object
    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        val objectType = object: TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, objectType)
    }
}
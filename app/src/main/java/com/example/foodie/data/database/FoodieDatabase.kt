package com.example.foodie.data.database

import androidx.room.Database
import androidx.room.TypeConverters

@Database(entities = [RecipesEntity::class], version = 1, exportSchema = true) //you should always set exportSchema to true, and then configure the module's build.gradle file to export the database schema to a JSON file in the project directory, and you should always check out this file in VCS like Git since it could help you in the case something wrong goes with your schema and you need an older version.
@TypeConverters(RecipesTypeConverter::class) // inform Room about the Type Converters we created.
abstract class FoodieDatabase {
    abstract fun recipesDao(): RecipesDao
}
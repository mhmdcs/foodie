package com.example.foodie.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodie.RECIPES_TABLE
import com.example.foodie.models.FoodRecipe

@Entity(tableName = RECIPES_TABLE)
data class RecipesEntity(
 // our recipes table will only have one "actual" column, FoodRecipe, which contains a list of Results that we will display on the screen, we only need to store this object to achieve offline caching, since it contains everything we'll need :)
    var foodRecipe: FoodRecipe
){
    // the primary key will be the second column, which will never change, we'll make it a constant member field set to 0, and we'll set autoGenerating the key to false so it's not auto-incremented, this way we'll only ever have one row (record of data) of this table, and whenever we need to update our table, or display a list from the UI, we'll always use this single record that's guaranteed to be the latest record.
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}

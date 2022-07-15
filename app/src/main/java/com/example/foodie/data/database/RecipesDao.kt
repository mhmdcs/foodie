package com.example.foodie.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodie.models.FoodRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // this tells retrofit that whenever we insert a new RecipesEntity, replace the old record with the new record who hold the same ID (the id will always be 0 for our RecipesEntity record(s). So basically we're ensuring that we only ever have one row of FoodRecipe inside our table.
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC") //we really don't need to order by id in ascending order since there will only be one record of FoodRecipe anyway :) could've written the query as "SELECT foodRecipe FROM recipes_table" and it would've worked just as well.
    fun readRecipes(): Flow<List<RecipesEntity>> // flow will then be converted to LiveData in the ViewModel. Putting it inside a List is probably pointless :)

}
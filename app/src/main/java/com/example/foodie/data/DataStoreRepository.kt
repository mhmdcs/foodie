package com.example.foodie.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.foodie.*
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped // since we'll use this DataStore inside a ViewModel, make this dependency exist throughout the lifecycle of the activity while survive configuration changes (in short, the lifecycle of the ViewModel) by annotating it with @ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context){

    // create the Preferences DataStore
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

    // setting the Preferences DataStore keys for String and Int values
    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)  // the id refers to the index of the chip
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID) // the id refers to the index of the chip
    }

    // writing to the Preferences DataStore - declare a suspend  function that takes in our Preferences DataStore values
   suspend fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        context.dataStore.edit { preferences -> //since edit() is a suspend functions, the function calling it needs to be suspended too, or be called from a coroutine
            // we write to the Preferences DataStore by first providing the key then the value
            preferences.set(PreferenceKeys.selectedMealType, mealType) // same as the ones below, but without using Kotlin's indexing operator for set() method
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    // reading from the Preferences DataStore - declare a Flow variable and initialize it by using the map() function to return the Flow's type argument, and catch a Flow exception while you're at it.
    val readMealAndDietType: Flow<MealAndDietType> = context.dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedMealType = preferences.get(PreferenceKeys.selectedMealType) ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0 // 0 refers to the index of the first chip in the Meal Type chip group, so if the value is null then it'll point to the "main course" chip at index 0 as the default value
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0 //// 0 refers to the index of the first chip in the Diet Type chip group, so if the value is null then it'll point to the "gluten free" chip at index 0 as the default value

            MealAndDietType(selectedMealType, selectedMealTypeId, selectedDietType, selectedDietTypeId)
        }

}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)
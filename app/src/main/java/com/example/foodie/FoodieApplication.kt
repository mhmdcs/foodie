package com.example.foodie

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// NEVER forget to add your application, activities, services and broadcastreceiver classes to the manifest, else they won't work ;)
@HiltAndroidApp //you must annotation application class with this annotation to use Dagger-Hilt
class FoodieApplication: Application() {

}
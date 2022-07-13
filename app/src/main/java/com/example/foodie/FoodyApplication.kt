package com.example.foodie

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //you must annotation application class with this annotation to use Dagger-Hilt
class FoodyApplication: Application() {

}
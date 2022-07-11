package com.example.foodie.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodie.R
import com.example.foodie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recipesFragment,
                R.id.favoriteRecipesFragment,
                R.id.jokeFragment
            )
        )

        binding.btmNavView.setupWithNavController(navController) // this is what triggers navigation in the bottom nav view
        setupActionBarWithNavController(navController, appBarConfiguration) // this just updates title in the action bar at the top accordingly

    }


    //The Back button appears in the system navigation bar at the bottom of the screen and is used to navigate in reverse-chronological order through the history of screens the user has recently worked with. When you press the Back button, the current destination is popped off the top of the back stack, and you then navigate to the previous destination.
    //The Up button appears inside the app's action bar at the top of the screen. The Up and Back buttons behave identically.
    //https://developer.android.com/guide/navigation/navigation-principles#:~:text=Up%20and%20Back%20are%20identical%20within%20your%20app's%20task,-Figure%202%3A%20The&text=When%20you%20press%20the%20Back,the%20top%20of%20the%20screen.

    // we override this to tell the navController to navigate back accordingly when the Up button is pressed. For example when we navigate from ListFragment to DetailsFragment, the Up button will appear to navigate back, and this callback will be called when it's clicked.
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
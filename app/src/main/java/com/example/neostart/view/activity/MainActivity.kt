package com.example.neostart.view.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.neostart.databinding.ActivityMainBinding
import com.example.neostart.view.fragment.RegisterFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

//    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        toggleColorOfStatusBarIcons(this)

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fcvHost, RegisterFragment())
//            .commit()

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.registerFragment) as NavHostFragment
//        navController = navHostFragment.navController
//
//        findNavController(binding.fcvHost.id)
    }
//    private fun toggleColorOfStatusBarIcons(activity: Activity) {
//        val decorView = activity.window.decorView
//        val insetsController = WindowInsetsControllerCompat(activity.window, decorView)
//
//        // Get current system bars appearance
//        val currentAppearance = insetsController.isAppearanceLightStatusBars
//
//        // Toggle the appearance
//        if (currentAppearance) {
//            insetsController.isAppearanceLightStatusBars = false // Dark icons
//        } else {
//            insetsController.isAppearanceLightStatusBars = true // Light icons
//        }
//    }
}
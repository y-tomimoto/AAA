package com.company.takitate.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.company.takitate.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // bottomNavigationView with navigation: https://developer.android.com/guide/navigation/navigation-ui?hl=ja
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navHostFragment =
          supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // bottom_nav を選択すると、fragmentを切り替えるよう紐付ける
        findViewById<BottomNavigationView>(R.id.bottom_nav)
          .setupWithNavController(navController)
    }
}

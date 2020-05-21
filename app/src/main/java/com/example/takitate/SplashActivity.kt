package com.example.takitate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        moveToNextSection()
    }
    private fun moveToNextSection(){
        val mainActivityIntent = Intent(this,MainActivity::class.java)
        startActivity(mainActivityIntent)
    }
}

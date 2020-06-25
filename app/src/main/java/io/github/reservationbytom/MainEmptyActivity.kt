package io.github.reservationbytom

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainEmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // go straight to main if a token is stored
        val activityIntent: Intent = if (true) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(activityIntent)
        finish()
    }
}

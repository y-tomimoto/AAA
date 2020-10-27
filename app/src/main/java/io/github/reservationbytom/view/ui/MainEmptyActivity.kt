package io.github.reservationbytom.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken


class MainEmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // go straight to main if a token is stored
        // Reffer to https://stackoverflow.com/questions/29366945/facebook-accesstoken-getaccesstoken-is-null-on-opening-of-app-even-after-first-l
        // Reffer to https://android.jlelse.eu/login-and-main-activity-flow-a52b930f8351
        val activityIntent: Intent = if (AccessToken.getCurrentAccessToken() == null) {
            Intent(this, LoginActivity::class.java)
        } else {
            // Intent(this, MainActivity::class.java)
            Intent(this, Test::class.java)
        }
        startActivity(activityIntent)
        finish()
    }
}

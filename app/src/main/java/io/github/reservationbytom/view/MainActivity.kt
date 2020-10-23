package io.github.reservationbytom.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.reservationbytom.R
import io.github.reservationbytom.R.id

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            //プロジェクト一覧のFragment
            val fragment = RestListFragment()

            supportFragmentManager
                .beginTransaction()
                .add(id.fragment_container, fragment, TAG_OF_PROJECT_LIST_FRAGMENT)
                .commit()
        }
    }
}

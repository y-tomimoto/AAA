package io.github.reservationbytom.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.reservationbytom.R
import io.github.reservationbytom.R.id
import io.github.reservationbytom.service.GetLocationService
import io.github.reservationbytom.service.model.Rest

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GetLocationService().getLocation(this)
        // savedInstanceState == null について : https://qiita.com/Nkzn/items/c09629d91d5cf42ff05d
        if (savedInstanceState == null) {

            val fragment = RestListFragment()

            supportFragmentManager
                .beginTransaction()
                .add(id.fragment_container, fragment,
                    TAG_OF_REST_LIST_FRAGMENT
                )
                .commit()
        }
    }

    //詳細画面への遷移
    fun show(rest: Rest) {
        val restFragment = RestFragment.forRest(rest.name)

        supportFragmentManager
            .beginTransaction()
            .addToBackStack("rest")
            .replace(id.fragment_container, restFragment, null)
            .commit()
    }
}

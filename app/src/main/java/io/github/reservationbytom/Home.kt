package io.github.reservationbytom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        val fragment = Example3Fragment()
        supportFragmentManager.beginTransaction() // ここでfragmentを実行している //  あー、これは返し方を定義しているのね、、、
            .run {
                return@run setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
            .add(
                R.id.homeContainer,
                fragment,
                fragment.javaClass.simpleName
            ) // ここにrepもあるのね。そしてここにはtagが入る

            // 3つ目の引数はなんだ？
            .addToBackStack(fragment.javaClass.simpleName) // これで戻るボタンを押すとfragmentに戻れる。一回これなしでやってみる。今のアクティビテぃの後続としてfragmentを採用するかたちか。
            .commit()
    }
}

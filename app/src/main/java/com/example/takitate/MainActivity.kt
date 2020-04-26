package com.example.takitate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent


class MainActivity : AppCompatActivity() { // 基本的に1つのアクティビティが１つの画面を表示している。これはAppCompatActivityからextendされている

    override fun onCreate(savedInstanceState: Bundle?) { // アクティビティが生成されたときに実行される
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // ここでレイアウトを紐付ける（ビューやビューグループも設定できる）

        val button = findViewById<Button>(R.id.calculate) // Rにlayoutが保存されていてそこからcaluculateを引いている

        // 下記はoncreate内で実行しないと、Expecting member declaration エラーが出る
        button.setOnClickListener {
            // ここに任意の処理を実装する
            // 妥当かどうか
            var isValid = true

            val priceEditText = findViewById<EditText>(R.id.price)
            val priceText = priceEditText.text.toString()

            if (priceText.isEmpty()) {
                // 定価が未入力
                priceEditText.error = getString(R.string.price_error)
                isValid = false
            }

            val discountEditText = findViewById<EditText>(R.id.discount)
            val discountText = discountEditText.text.toString()

            if (discountText.isEmpty()) {
                // 割引率が未入力
                discountEditText.error = getString(R.string.discount_error)
                isValid = false
            }

            if (isValid) {
                // 文字列を整数型に変換
                val price = priceText.toInt()
                val discount = discountText.toInt()

                // ここで画面遷移を行う
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("price", price)
                intent.putExtra("discount", discount)
                startActivity(intent)
            }
        }
    }
}

package com.example.takitate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // 入力画面で値が入れられていなければ、何もしない
        val extras = intent.extras ?: return

        // 入力画面でIntentに詰めた値を取り出す
        val price = extras.getInt("price", 0)
        val discount = extras.getInt("discount", 0)

        val expression = findViewById<TextView>(R.id.expression_label)
        expression.text = getString(R.string.expression, price, discount)

        // 割引後の価格を計算
        val discountedPrice = price * (100 - discount) / 100

        val discounted = findViewById<TextView>(R.id.result_label)
        discounted.text = getString(R.string.result, discountedPrice)
    }
}

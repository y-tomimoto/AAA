package com.example.takitate

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.widget.*
import androidx.annotation.StringRes
import com.jakewharton.threetenabp.AndroidThreeTen

data class ExampleItem(@StringRes val titleRes: Int, @StringRes val subtitleRes: Int, val createView: () -> BaseFragment)

class MainActivity : AppCompatActivity() { // 基本的に1つのアクティビティが１つの画面を表示している。これはAppCompatActivityからextendされている

    private fun show() {
        // データベースに登録されている文字列の一覧を得る
        val texts = queryTexts(this)
        val listView = findViewById<ListView>(R.id.listVIew)

        listView.adapter = ArrayAdapter<String>(this, R.layout.list_text_row, R.id.textView3, texts)
    }

    override fun onCreate(savedInstanceState: Bundle?) { // アクティビティが生成されたときに実行される
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContentView(R.layout.activity_main) // ここでレイアウトを紐付ける（ビューやビューグループも設定できる）

        val button1 = findViewById<Button>(R.id.button)
        button1.setOnClickListener {
            //ここが引数になっている。
            val fragment = Ex.createView() // ここにはdata内に宣言されているものが入る。
            // ここで、戻り値をエル。
            supportFragmentManager.beginTransaction() // ここでfragmentを実行している //  あー、これは返し方を定義しているのね、、、
                .run {
                    return@run setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                }
                .add(R.id.homeContainer, fragment, fragment.javaClass.simpleName) // ここにrepもあるのね。そしてここにはtagが入る
                // 3つ目の引数はなんだ？
                .addToBackStack(fragment.javaClass.simpleName) // これで戻るボタンを押すとfragmentに戻れる。一回これなしでやってみる。今のアクティビテぃの後続としてfragmentを採用するかたちか。
                .commit() // これが設定の反映ね。
            // ここまでがtapしたときの動き。これがhome画面からtapしたときの遷移になる。
        }
        show()


        val fragmentButton = findViewById<Button>(R.id.fragmentButton)

        button1.setOnClickListener {


        }



        val searchButton = findViewById<Button>(R.id.button3)

        searchButton.setOnClickListener {
            // ここで画面遷移を行う
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val adminButton = findViewById<Button>(R.id.admin)

        adminButton.setOnClickListener {
            // ここで画面遷移を行う
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }



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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }
}

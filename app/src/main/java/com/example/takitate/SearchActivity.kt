package com.example.takitate

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val disp = findViewById<TextView>(R.id.disp)
        val searchBtn = findViewById<Button>(R.id.startSearch)
        val searchText = findViewById<EditText>(R.id.searchText)

        searchBtn.setOnClickListener {
            // テキストを初期化している
            disp.text = "SEARCH RESULTS:\n";
            val str = searchText.text.toString()
            val database = SampleDBOpenHelper(this).readableDatabase
            val texts = mutableListOf<String>()
            val c: Cursor = database.query(
                "texts",
                arrayOf("text"),
                "text=?",
                arrayOf(str),
                null,
                null,
                null
            )
            c.use {
                    // カーソルで順次処理していく
                    while(c.moveToNext()) {
                        // 保存されているテキストを得る
                        val text = c.getString(c.getColumnIndex("text"))
                        texts.add(text)
                    }
                }

            database.close()
            disp.text = texts.toString()
        }

    }


}

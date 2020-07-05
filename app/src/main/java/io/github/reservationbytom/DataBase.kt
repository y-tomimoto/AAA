package io.github.reservationbytom

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DB_NAME = "test"
private const val DB_VERSION = 1

class SampleDBOpenHelper(context:Context):
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION,null){
    override fun onCreate(db: SQLiteDatabase?) {
        // ここでtableを作成する
        db?.execSQL("CREATE TABLE test_table ( " +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " _text TEXT NOT NULL," +
                " _created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // バージョン更新
    }
}

fun queryTexts(context:Context):List<String>{
    // 開く
    val database = SampleDBOpenHelper(context).readableDatabase

    // データベースを検索
    val cursor = database.query(
        "test_table",null,null,null,null,null,"created_at DESC"
    )
    val texts = mutableListOf<String>()
    cursor.use {
        // カーソルで処理
        while(cursor.moveToNext()){
            val text = cursor.getString(cursor.getColumnIndex("_text"))
            texts.add(text)
        }
    }
    database.close()
    return texts
}

fun insertText(context:Context,text:String){
    val database = SampleDBOpenHelper(context).writableDatabase
    database.use{ db->
        val record = ContentValues().apply {
            put("texts",text)
        }
        db.insert("test_table",null,record)
    }
}
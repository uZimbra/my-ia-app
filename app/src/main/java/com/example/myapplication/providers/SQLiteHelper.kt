package com.example.myapplication.providers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.contracts.HistoryContract

class SQLiteHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE ${HistoryContract.TABLE_NAME} (" +
                "${HistoryContract.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${HistoryContract.COLUMN_NAME} TEXT," +
                "${HistoryContract.COLUMN_VALUE} TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${HistoryContract.TABLE_NAME}")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "HistoryDatabase.db"
        const val DATABASE_VERSION = 1
    }
}
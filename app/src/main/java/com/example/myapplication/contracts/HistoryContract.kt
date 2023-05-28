package com.example.myapplication.contracts

import android.net.Uri

object HistoryContract {
    const val COLUMN_ID = "_id"
    const val COLUMN_NAME = "question"
    const val COLUMN_VALUE = "response"

    const val TABLE_NAME = "history"
    val HISTORY_PROVIDER_URI: Uri =
        Uri.parse("content://com.example.myapplication.provider/history")
}
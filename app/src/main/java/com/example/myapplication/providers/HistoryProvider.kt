package com.example.myapplication.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.example.myapplication.contracts.HistoryContract

class HistoryProvider : ContentProvider() {
    private lateinit var database: SQLiteDatabase

    override fun onCreate(): Boolean {
        val dbHelper = SQLiteHelper(context)
        database = dbHelper.writableDatabase
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = database.query(
            HistoryContract.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = database.insert(HistoryContract.TABLE_NAME, null, values)
        context?.contentResolver?.notifyChange(uri, null)
        return Uri.withAppendedPath(uri, id.toString())
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return database.delete(HistoryContract.TABLE_NAME, selection, selectionArgs)
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}
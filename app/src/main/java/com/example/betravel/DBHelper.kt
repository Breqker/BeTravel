package com.example.betravel

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "betravel.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "DatiUtente"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOME = "nome"
        private const val COLUMN_COGNOME = "cognome"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_FOTO_PROFILO = "fotoProfilo"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_NOME TEXT, " +
                "$COLUMN_COGNOME TEXT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_FOTO_PROFILO BLOB)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(nome: String, cognome: String, email: String, password: String, fotoProfilo: ByteArray) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOME, nome)
        values.put(COLUMN_COGNOME, cognome)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)
        values.put(COLUMN_FOTO_PROFILO, fotoProfilo)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllData(): List<UserData> {
        val dataList = mutableListOf<UserData>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor?.let {
            if (it.moveToFirst()) {
                val nomeIndex = it.getColumnIndexOrThrow(COLUMN_NOME)
                val cognomeIndex = it.getColumnIndexOrThrow(COLUMN_COGNOME)
                val emailIndex = it.getColumnIndexOrThrow(COLUMN_EMAIL)
                val passwordIndex = it.getColumnIndexOrThrow(COLUMN_PASSWORD)
                val fotoProfiloIndex = it.getColumnIndexOrThrow(COLUMN_FOTO_PROFILO)

                do {
                    val nome = it.getString(nomeIndex)
                    val cognome = it.getString(cognomeIndex)
                    val email = it.getString(emailIndex)
                    val password = it.getString(passwordIndex)
                    val fotoProfilo = it.getBlob(fotoProfiloIndex)
                    val userData = UserData(nome, cognome, email, password, fotoProfilo)
                    dataList.add(userData)
                } while (it.moveToNext())
            }
            it.close()
        }
        db.close()
        return dataList
    }

    fun updateData(id: Int, nome: String, cognome: String, email: String, password: String, fotoProfilo: ByteArray) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOME, nome)
        values.put(COLUMN_COGNOME, cognome)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)
        values.put(COLUMN_FOTO_PROFILO, fotoProfilo)

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(id.toString())

        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun deleteUserData(id: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(id.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun getUserById(id: Int): UserData? {
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf(id.toString()))
        var userData: UserData? = null

        cursor?.let {
            if (it.moveToFirst()) {
                val nomeIndex = it.getColumnIndexOrThrow(COLUMN_NOME)
                val cognomeIndex = it.getColumnIndexOrThrow(COLUMN_COGNOME)
                val emailIndex = it.getColumnIndexOrThrow(COLUMN_EMAIL)
                val passwordIndex = it.getColumnIndexOrThrow(COLUMN_PASSWORD)
                val fotoProfiloIndex = it.getColumnIndexOrThrow(COLUMN_FOTO_PROFILO)

                val nome = it.getString(nomeIndex)
                val cognome = it.getString(cognomeIndex)
                val email = it.getString(emailIndex)
                val password = it.getString(passwordIndex)
                val fotoProfilo = it.getBlob(fotoProfiloIndex)

                userData = UserData(nome, cognome, email, password, fotoProfilo)
            }
            it.close()
        }
        db.close()
        return userData
    }

    fun checkUserExistenceByEmail(email: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val userExists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return userExists
    }

}

data class UserData(
    val nome: String,
    val cognome: String,
    val email: String,
    val password: String,
    val fotoProfilo: ByteArray
)


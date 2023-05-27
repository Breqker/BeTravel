package com.example.betravel

import java.sql.Blob

class DBManager(private val dbHelper: DBHelper) {

    fun insertData(nome: String, cognome: String, email: String, password: String, fotoProfilo: ByteArray?) {
        dbHelper.insertData(nome, cognome, email, password, fotoProfilo)
    }

    fun getAllData(): List<UserData> {
        return dbHelper.getAllData()
    }

    fun updateUserData(id: Int, nome: String, cognome: String, email: String, password: String, fotoProfilo: ByteArray?) {
        dbHelper.updateData(id, nome, cognome, email, password, fotoProfilo)
    }

    fun deleteUserData(id: Int){
        dbHelper.deleteUserData(id)
    }

    fun getUserById(id: Int): UserData? {
        return dbHelper.getUserById(id)
    }

    fun checkUserExistenceByEmail(email: String): Boolean {
        return dbHelper.checkUserExistenceByEmail(email)
    }

    fun checkCredentials(email: String, password: String): Boolean {
        return dbHelper.checkCredentials(email, password)
    }

    fun checkPassword(password1: String,password2: String): Boolean{
        return dbHelper.checkPasswordMatch(password1,password2)
    }
}

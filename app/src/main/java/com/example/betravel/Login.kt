package com.example.betravel

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.betravel.databinding.ActivityLoginBinding


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        dbManager = DBManager(dbHelper)

        binding.editTextRegistrazione.setOnClickListener{
            val intent = Intent(this, Registrazione::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener{
            val email = binding.editTextEmail2.text.toString()
            val password = binding.editTextPassword2.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                if (dbManager.checkCredentials(email, password)) {
                    showMessage("Login effettuato con successo")
                }else {
                    showErrorMessage("Credenziali non valide")
                }
            }else {
                showErrorMessage("Compilare tutti i campi")
            }
        }
    }

    private fun showErrorMessage(message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Errore")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }
    private fun showMessage(message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Informazione")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                navigateToHome()
            }
            .create()
        alertDialog.show()
    }

    private fun navigateToHome() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}
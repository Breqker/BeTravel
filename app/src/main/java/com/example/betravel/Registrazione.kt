package com.example.betravel

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.betravel.databinding.ActivityRegistrazioneBinding
import com.example.betravel.databinding.ActivityRegistrazioneLandBinding

class Registrazione : AppCompatActivity() {

    private lateinit var binding : ActivityRegistrazioneBinding
    private lateinit var bindingLand : ActivityRegistrazioneLandBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrazioneBinding.inflate(layoutInflater)
        bindingLand = ActivityRegistrazioneLandBinding.inflate(layoutInflater)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(bindingLand.root)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
        }

        dbHelper = DBHelper(this)
        dbManager = DBManager(dbHelper)

        binding.buttonRegistrati.setOnClickListener{
            val nome = binding.editTextNome.text.toString()
            val cognome = binding.editTextCognome.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confermaPassword = binding.editTextConfermaPassword.text.toString()

            if(nome.isNotEmpty() && cognome.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confermaPassword.isNotEmpty()){
                if(dbManager.checkPassword(password,confermaPassword)){
                    if(dbManager.checkUserExistenceByEmail(email)){
                        showErrorMessage("Utente già registrato")
                    }else{
                        dbManager.insertData(nome,cognome,email,password,null)
                        showMessage("Registrazione effettuata con successo")
                    }
                }else{
                    showErrorMessage("Password non coincidenti")
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
                navigateToLogin()
            }
            .create()
        alertDialog.show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}
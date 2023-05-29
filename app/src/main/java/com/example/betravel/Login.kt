package com.example.betravel

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.betravel.databinding.ActivityLoginBinding
import com.example.betravel.databinding.ActivityLoginLandBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var bindingLand: ActivityLoginLandBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        bindingLand = ActivityLoginLandBinding.inflate(layoutInflater)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(bindingLand.root)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
        }

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

        binding.editTextRecuperoPassword.setOnClickListener{
            val intent = Intent(this, RecuperoPassword::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::dbHelper.isInitialized) {
            dbHelper.close()
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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
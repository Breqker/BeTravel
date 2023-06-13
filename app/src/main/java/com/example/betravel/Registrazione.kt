package com.example.betravel

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.betravel.databinding.ActivityRegistrazioneBinding
import com.example.betravel.databinding.ActivityRegistrazioneLandBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registrazione : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrazioneBinding
    private lateinit var bindingLand: ActivityRegistrazioneLandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrazioneBinding.inflate(layoutInflater)
        bindingLand = ActivityRegistrazioneLandBinding.inflate(layoutInflater)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(bindingLand.root)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
        }

        binding.buttonRegistrati.setOnClickListener {
            val nome = binding.editTextNome.text.toString()
            val cognome = binding.editTextCognome.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confermaPassword = binding.editTextConfermaPassword.text.toString()

            if (nome.isNotEmpty() && cognome.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confermaPassword.isNotEmpty()) {
                if (checkPassword(password, confermaPassword)) {
                    registerUser(nome, cognome, email, password)
                } else {
                    showErrorMessage("Password non coincidenti")
                }
            } else {
                showErrorMessage("Compilare tutti i campi")
            }
        }

        bindingLand.buttonRegistrati.setOnClickListener {
            val nome = bindingLand.editTextNome.text.toString()
            val cognome = bindingLand.editTextCognome.text.toString()
            val email = bindingLand.editTextEmail.text.toString()
            val password = bindingLand.editTextPassword.text.toString()
            val confermaPassword = bindingLand.editTextConfermaPassword.text.toString()

            if (nome.isNotEmpty() && cognome.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confermaPassword.isNotEmpty()) {
                if (checkPassword(password, confermaPassword)) {
                    registerUser(nome, cognome, email, password)
                } else {
                    showErrorMessage("Password non coincidenti")
                }
            } else {
                showErrorMessage("Compilare tutti i campi")
            }
        }
    }

    private fun checkPassword(password: String, confermaPassword: String): Boolean {
        val passwordRegex = Regex("(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}")

        return password == confermaPassword && password.matches(passwordRegex)
    }

    private fun registerUser(nome: String, cognome: String, email: String, password: String) {
        val checkQuery = "SELECT * FROM webmobile.Utente WHERE email = '$email';"
        val insertQuery = "INSERT INTO webmobile.Utente (nome, cognome, email, password) VALUES ('$nome', '$cognome', '$email', '$password');"

        val checkCall = ClientNetwork.retrofit.select(checkQuery)
        checkCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if ((data?.get("queryset") as JsonArray).size() > 0) {
                        showErrorMessage("L'email esiste già")
                    } else {
                        // L'email non esiste, puoi procedere con l'insert
                        val insertCall = ClientNetwork.retrofit.insert(insertQuery)
                        insertCall.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    showMessage("Registrazione effettuata con successo")
                                } else {
                                    showErrorMessage("Errore durante la registrazione")
                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                showErrorMessage("Errore di connessione: ${t.message}")
                            }
                        })
                    }
                } else {
                    showErrorMessage("Errore durante la verifica dell'email")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showErrorMessage("Errore di connessione: ${t.message}")
            }
        })
    }
    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}

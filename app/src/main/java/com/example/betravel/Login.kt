package com.example.betravel

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.betravel.databinding.ActivityLoginBinding
import com.example.betravel.databinding.ActivityLoginLandBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var bindingLand: ActivityLoginLandBinding

    companion object{
        const val user_id = "id_utente"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        bindingLand = ActivityLoginLandBinding.inflate(layoutInflater)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(bindingLand.root)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
        }

        binding.editTextRegistrazione.setOnClickListener {
            val intent = Intent(this, Registrazione::class.java)
            startActivity(intent)
        }

        bindingLand.editTextRegistrazione.setOnClickListener {
            val intent = Intent(this, Registrazione::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail2.text.toString()
            val password = binding.editTextPassword2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginRequest(email, password)
            } else {
                showErrorMessage("Compilare tutti i campi")
            }
        }

        bindingLand.buttonLogin.setOnClickListener {
            val email = bindingLand.editTextEmail2.text.toString()
            val password = bindingLand.editTextPassword2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginRequest(email, password)
            } else {
                showErrorMessage("Compilare tutti i campi")
            }
        }

        binding.editTextRecuperoPassword.setOnClickListener {
            val intent = Intent(this, RecuperoPassword::class.java)
            startActivity(intent)
        }

        bindingLand.editTextRecuperoPassword.setOnClickListener {
            val intent = Intent(this, RecuperoPassword::class.java)
            startActivity(intent)
        }
    }

    private fun loginRequest(email: String, password: String) {
        val loginQuery = "SELECT * FROM webmobile.Utente WHERE email = '$email' AND password = '$password';"

        val loginCall = ClientNetwork.retrofit.select(loginQuery)
        loginCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()?.get("queryset") as JsonArray

                    if (data.size() == 1){
                        val id = data.get(0).toString().toIntOrNull()
                        showMessage("Login effettuato con successo",id)
                    } else {
                        showErrorMessage("Credenziali non valide")
                    }
                } else {
                    showErrorMessage("Errore durante il login")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showErrorMessage("Errore di connessione: ${t.message}")
            }
        })
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

    private fun showMessage(message: String,id_utente: Int?) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Informazione")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                navigateToHome(id_utente)
            }
            .create()
        alertDialog.show()
    }

    private fun navigateToHome(id_utente: Int?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("id_utente",id_utente)
        }
        startActivity(intent)
        finish()
    }
}

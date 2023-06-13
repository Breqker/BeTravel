package com.example.betravel

import android.content.DialogInterface
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.betravel.databinding.ActivityRecuperoPasswordBinding
import com.example.betravel.databinding.ActivityRecuperoPasswordLandBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random

class RecuperoPassword : AppCompatActivity() {

    private lateinit var binding: ActivityRecuperoPasswordBinding
    private lateinit var bindingLand: ActivityRecuperoPasswordLandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperoPasswordBinding.inflate(layoutInflater)
        bindingLand = ActivityRecuperoPasswordLandBinding.inflate(layoutInflater)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(bindingLand.root)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
        }

        binding.recuperoPassword.setOnClickListener {
            val email = binding.editRecuperoPassword.text.toString()
            checkEmailExists(email)
        }

        bindingLand.recuperoPassword.setOnClickListener {
            val email = bindingLand.editRecuperoPassword.text.toString()
            checkEmailExists(email)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun checkEmailExists(email: String) {
        val query = "SELECT email FROM webmobile.Utente WHERE email = '$email';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    if ((data?.get("queryset") as JsonArray).size() > 0) {
                        showMessage("Una mail con le istruzioni per il recupero password è stata inviata all'indirizzo fornito")
                    } else {
                        showMessage("Email non trovata")
                    }
                } else {
                    showMessage("Errore durante la verifica dell'email")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

}

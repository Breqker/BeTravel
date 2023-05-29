package com.example.betravel

import android.content.DialogInterface
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.betravel.databinding.ActivityRecuperoPasswordBinding
import com.example.betravel.databinding.ActivityRecuperoPasswordLandBinding
import java.util.*

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
            resetPassword(email)
        }

        bindingLand.recuperoPassword.setOnClickListener {
            val email = bindingLand.editRecuperoPassword.text.toString()
            resetPassword(email)
        }
    }

    private fun resetPassword(email: String) {
        val randomPassword = generateRandomPassword(8) // Genera una password casuale
        updatePasswordInDatabase(email, randomPassword) // Aggiorna la password nel database

        val message = "La tua nuova password temporanea è: $randomPassword"

        showMessage(message)
    }

    private fun showMessage(message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Nuova Password temporanea")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    private fun generateRandomPassword(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random()
        val password = StringBuilder()

        for (i in 0 until length) {
            val randomIndex = random.nextInt(characters.length)
            password.append(characters[randomIndex])
        }

        return password.toString()
    }

    private fun updatePasswordInDatabase(email: String, newPassword: String) {
        // Logica per aggiornare la password nel database
    }
}

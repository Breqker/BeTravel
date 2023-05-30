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
            showMessage("Un'email con le istruzioni per il recupero password è stata inviata all'indirizzo fornito")
        }

        // Layout orizzontale
        bindingLand.recuperoPassword.setOnClickListener {
            val email = bindingLand.editRecuperoPassword.text.toString()
            resetPassword(email)
            showMessage("Un'email con le istruzioni per il recupero password è stata inviata all'indirizzo fornito")
        }
    }

    private fun resetPassword(email: String) {
        val successMessage = "Un'email con le istruzioni per il recupero password è stata inviata all'indirizzo fornito."

        showMessage(successMessage)
    }

    private fun showMessage(message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Informazione")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }
}

package com.example.betravel

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.betravel.databinding.ActivityRecuperoPasswordBinding
import com.example.betravel.databinding.ActivityRecuperoPasswordLandBinding

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

        binding.recuperoPassword.setOnClickListener{
            val email = binding.editRecuperoPassword.text.toString()
            sendEmail(email)
        }

        bindingLand.recuperoPassword.setOnClickListener{
            val email = bindingLand.editRecuperoPassword.text.toString()
            sendEmail(email)
        }
    }

    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, "Recupero Password")
            putExtra(Intent.EXTRA_TEXT, "Ciao,\n\nAbbiamo ricevuto la tua richiesta di recupero password.\n\nInviati ulteriori dettagli per il recupero.\n\nCordiali saluti,\nIl tuo team di supporto")
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}
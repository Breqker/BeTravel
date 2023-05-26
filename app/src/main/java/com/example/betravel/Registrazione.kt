package com.example.betravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.betravel.databinding.ActivityRegistrazioneBinding

class Registrazione : AppCompatActivity() {

    private lateinit var binding : ActivityRegistrazioneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Controllare se tutti i campi sono stati compilati, dopo controllare se i dati non presenti all'interno del DB , dopo mostrare un Alert Dialog "Registrazione effettuata con successo" e dopo ritornare alla schermata Login
        binding.buttonRegistrati.setOnClickListener{
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
    }
}
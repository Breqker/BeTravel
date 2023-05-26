package com.example.betravel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.betravel.databinding.ActivityLoginBinding


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextRegistrazione.setOnClickListener{
            val intent = Intent(this, Registrazione::class.java)
            startActivity(intent)
        }

        //Controllare se tutti i campi sono stati compilati, dopo controllare che i dati siano presenti nel DB e dopo accedere alla schermata home
        binding.buttonLogin.setOnClickListener{
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
        }
    }

}
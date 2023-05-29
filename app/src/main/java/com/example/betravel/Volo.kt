package com.example.betravel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.betravel.databinding.ActivityVoloBinding

class Volo : AppCompatActivity() {

    private lateinit var binding: ActivityVoloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoloBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
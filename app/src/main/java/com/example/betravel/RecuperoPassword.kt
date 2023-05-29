package com.example.betravel

import android.content.res.Configuration
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
    }
}
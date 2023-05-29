package com.example.betravel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.betravel.databinding.ActivityProfiloBinding
import com.example.betravelimport.BottomNavigationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Profilo: AppCompatActivity() {

    private lateinit var binding: ActivityProfiloBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfiloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationFragment = BottomNavigationFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, bottomNavigationFragment)
            .commit()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Profilo -> {
                    true
                }

                R.id.Preferiti -> {
                    // Logica per gestire la pressione del pulsante Preferiti
                    true
                }

                else -> false
            }
        }
    }
}
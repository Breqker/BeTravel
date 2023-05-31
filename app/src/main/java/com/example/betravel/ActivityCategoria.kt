package com.example.betravel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.betravel.databinding.ActivityCategoriaBinding

class ActivityCategoria : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val position = intent.getIntExtra("position", 0)
        val fragmentVolo = FragmentVolo()
        val fragmentSoggiorno = FragmentSoggiorno()
        val fragmentCrociera = FragmentCrociera()
        val fragmentTaxi = FragmentTaxi()
        val fragmentAuto = FragmentAuto()

        when (position) {
            0 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView,fragmentVolo)
                .remove(fragmentSoggiorno)
                .commit()
            1 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView,fragmentSoggiorno)
                .remove(fragmentVolo)
                .commit()
            2 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView,fragmentCrociera)
                .remove(fragmentVolo)
                .commit()
            3 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView,fragmentTaxi)
                .remove(fragmentVolo)
                .commit()
            4 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView,fragmentAuto)
                .remove(fragmentVolo)
                .commit()

        }
    }
}
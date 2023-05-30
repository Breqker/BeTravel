package com.example.betravel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.ActivityProfiloBinding
import com.example.betravelimport.BottomNavigationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Profilo: Fragment() {

    private lateinit var binding: ActivityProfiloBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityProfiloBinding.inflate(inflater)
        val view = inflater.inflate(R.layout.activity_profilo, container, false)

        return binding.root
    }
}
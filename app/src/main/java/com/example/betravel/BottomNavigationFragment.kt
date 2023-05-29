package com.example.betravelimport

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.betravel.Login
import com.example.betravel.MainActivity
import com.example.betravel.R
import com.example.betravel.Registrazione
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationFragment : Fragment() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false)
        bottomNavigationView = view.findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    val intent = Intent(activity, Login::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Profilo -> {
                    Log.e("TAG", "PROFILO")
                    true
                }
                R.id.Preferiti -> {
                    val obj_c = Intent(requireActivity(), Login::class.java)
                    startActivity(obj_c)
                    true
                }
                else -> false
            }
        }

        return view
    }
}
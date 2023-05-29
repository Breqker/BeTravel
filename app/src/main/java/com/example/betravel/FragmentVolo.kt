package com.example.betravel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.FragmentVoloBinding

class FragmentVolo : Fragment() {

    private lateinit var binding: FragmentVoloBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVoloBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }
}
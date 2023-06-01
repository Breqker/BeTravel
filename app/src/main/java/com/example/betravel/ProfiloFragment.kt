package com.example.betravel

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import com.example.betravel.databinding.FragmentProfiloBinding


class ProfiloFragment : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentProfiloBinding
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Abilita la gestione personalizzata del tasto indietro
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Verifica se ci sono fragment nello stack di backstack
                if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    isEnabled = false // Disabilita il callback
                    requireActivity().onBackPressed() // Esegui il comportamento di default del tasto indietro
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentProfiloBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.editButton.setOnClickListener {
            val fragmentProfilo = ModificaProfiloFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragmentProfilo)
                .addToBackStack(null)
                .commit()
            currentFragment = fragmentProfilo
        }

        return view
    }


    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
    }
}

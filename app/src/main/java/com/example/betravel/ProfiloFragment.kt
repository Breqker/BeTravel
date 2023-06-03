package com.example.betravel

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import com.example.betravel.databinding.FragmentProfiloBinding
import com.example.betravel.databinding.FragmentProfiloOrizzontaleBinding

class ProfiloFragment : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentProfiloBinding
    private lateinit var bindingOrizzontale: FragmentProfiloOrizzontaleBinding
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale = FragmentProfiloOrizzontaleBinding.inflate(inflater, container, false)
            val view = bindingOrizzontale.root
            bindingOrizzontale.editButton.setOnClickListener {
                val fragmentProfilo = ModificaProfiloFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container_orizzontale, fragmentProfilo)
                    .addToBackStack(null)
                    .commit()
                currentFragment = fragmentProfilo
            }
            return view
        } else {
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
    }

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
    }
}
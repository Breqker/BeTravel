package com.example.betravel

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.FragmentRiepilogoBinding
import com.example.betravel.databinding.FragmentRiepilogoOrizzontaleBinding

class FragmentRiepilogo: Fragment() {

    private lateinit var binding: FragmentRiepilogoBinding
    private lateinit var bindingOrizzontale : FragmentRiepilogoOrizzontaleBinding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale = FragmentRiepilogoOrizzontaleBinding.inflate(inflater, container, false)
            val view = bindingOrizzontale.root

            Log.d("FragmentRisultati.dati", "${FragmentRisultati.dati.toString()}")

            setUpRecyclerView(FragmentRisultati.dati)

            bindingOrizzontale.button.setOnClickListener {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, FragmentPagamento())
                transaction.addToBackStack(null)
                transaction.commit()
            }

            return view
        }else{
            binding = FragmentRiepilogoBinding.inflate(inflater, container, false)
            val view = binding.root

        setUpRecyclerView(FragmentRisultati.dati)

        binding.button.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, FragmentPagamento())
            transaction.addToBackStack(null)
            transaction.commit()
            binding.textView.isVisible = false
        }

            return view
        }
    }

    private fun setUpRecyclerView(dati: ArrayList<ItemsViewModel>?) {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale.recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            val adapter = dati?.let { CustomAdapterRisultati(it) }
            bindingOrizzontale.recyclerView.adapter = adapter
        }else{
            binding.recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            val adapter = dati?.let { CustomAdapterRisultati(it) }
            binding.recyclerView.adapter = adapter
        }
    }

    companion object {
        private const val ARG_DATA = "data"

        fun newInstance(data: String): FragmentRiepilogo {
            val fragment = FragmentRiepilogo()
            val bundle = Bundle()
            bundle.putString(ARG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}
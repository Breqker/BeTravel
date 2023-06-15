package com.example.betravel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.FragmentRiepilogoBinding

class FragmentRiepilogo: Fragment() {

    private lateinit var binding: FragmentRiepilogoBinding

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
        binding = FragmentRiepilogoBinding.inflate(inflater, container, false)
        val view = binding.root

        Log.d("FragmentRisultati.dati", "${FragmentRisultati.dati.toString()}")

        setUpRecyclerView(FragmentRisultati.dati)

        binding.button.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, FragmentPagamento())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    private fun setUpRecyclerView(dati: ArrayList<ItemsViewModel>?) {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        val adapter = dati?.let { CustomAdapterRisultati(it) }
        binding.recyclerView.adapter = adapter

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
package com.example.betravel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.FragmentRisultatiBinding

class FragmentRisultati : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentRisultatiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRisultatiBinding.inflate(inflater, container, false)
        val view = binding.root

        setUpRecyclerView()

        return view
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val data = ArrayList<ItemsViewModelPreferiti>()

        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Descrizione"))
        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Descrizione"))
        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Descrizione"))
        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Descrizione"))
        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Descrizione"))

        val adapter = CustomAdapterRisultati(data)
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : CustomAdapterRisultati.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    // Gestisci l'evento di clic
                }
            }
        })
    }

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

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
    }
}
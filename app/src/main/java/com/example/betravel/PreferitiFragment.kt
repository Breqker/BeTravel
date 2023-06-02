package com.example.betravel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.FragmentPreferitiBinding

class PreferitiFragment : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentPreferitiBinding
    private var currentFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreferitiBinding.inflate(inflater, container, false)
        val view = binding.root

        setUpRecyclerView()

        return view
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        val data = ArrayList<ItemsViewModelPreferiti>()

        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Scopri tutti i voli"))
        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Soggiorno"))
        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Prenota taxi"))
        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Scopri le crociere"))
        data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, "Noleggia un auto"))

        val adapter = CustomAdapterPreferiti(data)
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : CustomAdapterPreferiti.OnItemClickListener {
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

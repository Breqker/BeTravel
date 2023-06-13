package com.example.betravel

import android.os.Bundle
import android.util.Log
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
import org.json.JSONArray
import org.json.JSONObject

class FragmentRisultati : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentRisultatiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRisultatiBinding.inflate(inflater, container, false)
        val view = binding.root

        val data = arguments?.getStringArrayList(ARG_DATA)

        if (!data.isNullOrEmpty()) {
            val isVoliData = data.first().contains("nome_volo")
            if (isVoliData) {
                setUpRecyclerViewVoli()
            } else {
                setUpRecyclerViewSoggiorni()
            }
        }

        return view
    }


    private fun setUpRecyclerViewVoli() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        val data = ArrayList<ItemsViewModelPreferiti>()

        val inputData = arguments?.getStringArrayList("data")
        if (!inputData.isNullOrEmpty()) {
            for (i in 0 until inputData.size) {
                val volo = inputData[i]
                val flightDetails = formatFlightDetails(volo)
                data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, flightDetails))
            }
        }

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

    private fun formatFlightDetails(jsonString: String): String {
        val jsonObject = JSONObject(jsonString)

        if (jsonObject.has("data_ritorno")) {
            val nomeVolo = jsonObject.getString("nome_volo")
            val aeroportoPartenza = jsonObject.getString("aeroporto_partenza")
            val aeroportoArrivo = jsonObject.getString("aeroporto_arrivo")
            val dataPartenza = jsonObject.getString("data_partenza")
            val dataRitorno = jsonObject.getString("data_ritorno")
            val oraPartenza = jsonObject.getString("ora_partenza")
            val oraArrivo = jsonObject.getString("ora_arrivo")
            val costoBiglietto = jsonObject.getDouble("costo_biglietto")

            val formattedString = StringBuilder()
            formattedString.append("Nome Volo: $nomeVolo")
            formattedString.append("\nDa $aeroportoPartenza a $aeroportoArrivo")
            formattedString.append("\nData partenza: $dataPartenza")
            formattedString.append("\nData ritorno: $dataRitorno")
            formattedString.append("\nOra partenza: ${oraPartenza.substring(0, 5)}")
            formattedString.append("\nOra arrivo: ${oraArrivo.substring(0, 5)}")
            formattedString.append("\nCosto biglietto: $costoBiglietto")

            return formattedString.toString()
        } else {
            val nomeVolo = jsonObject.getString("nome_volo")
            val aeroportoPartenza = jsonObject.getString("aeroporto_partenza")
            val aeroportoArrivo = jsonObject.getString("aeroporto_arrivo")
            val dataPartenza = jsonObject.getString("data_partenza")
            val oraPartenza = jsonObject.getString("ora_partenza")
            val oraArrivo = jsonObject.getString("ora_arrivo")
            val costoBiglietto = jsonObject.getDouble("costo_biglietto")

            val formattedString = StringBuilder()
            formattedString.append("Nome Volo: $nomeVolo")
            formattedString.append("\nDa $aeroportoPartenza a $aeroportoArrivo")
            formattedString.append("\nData partenza: $dataPartenza")
            formattedString.append("\nOra partenza: ${oraPartenza.substring(0, 5)}")
            formattedString.append("\nOra arrivo: ${oraArrivo.substring(0, 5)}")
            formattedString.append("\nCosto biglietto: $costoBiglietto")

            return formattedString.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                    requireActivity().supportFragmentManager.popBackStack() // Rimuovi il FragmentRisultati dal back stack
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setUpRecyclerViewSoggiorni() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        val data = ArrayList<ItemsViewModelPreferiti>()

        val inputData = arguments?.getStringArrayList("data")
        if (!inputData.isNullOrEmpty()) {
            for (i in 0 until inputData.size) {
                val soggiorno = inputData[i]
                val soggiorniDetails = formatSoggiorniDetails(soggiorno)
                data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, soggiorniDetails))
            }
        }

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

    private fun formatSoggiorniDetails(jsonString: String?): String {
        val jsonObject = JSONObject(jsonString)

        val nomeAlloggio = jsonObject.getString("nome_alloggio")
        val citta = jsonObject.getString("citta")
        val dataInizio = jsonObject.getString("data_inizio")
        val dataRilascio = jsonObject.getString("data_rilascio")
        val costoGiornaliero = jsonObject.getString("costo_giornaliero")
        val numOspiti = jsonObject.getString("num_ospiti")

        val formattedString = StringBuilder()
        formattedString.append("Nome alloggio: \n$nomeAlloggio")
        formattedString.append("\nCittà: $citta")
        formattedString.append("\nDal: $dataInizio \nal $dataRilascio")
        formattedString.append("\nCosto giornaliero: $costoGiornaliero")
        formattedString.append("\nNumero ospiti: $numOspiti")

        return formattedString.toString()
    }

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
    }

    companion object {
        private const val ARG_DATA = "data"

        fun newInstance(data: ArrayList<String>): FragmentRisultati {
            val fragment = FragmentRisultati()
            val bundle = Bundle()
            bundle.putStringArrayList(ARG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstanceSoggiorno(data: ArrayList<String>): FragmentRisultati {
            val fragment = FragmentRisultati()
            val bundle = Bundle()
            bundle.putStringArrayList(ARG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}
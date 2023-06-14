package com.example.betravel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.core.view.isVisible
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
    ): View? {
        binding = FragmentRisultatiBinding.inflate(inflater, container, false)
        val view = binding.root

        val data = arguments?.getStringArrayList(ARG_DATA)

        if (!data.isNullOrEmpty()) {
            if (data.first().contains("nome_volo")) {
                setUpRecyclerViewVoli()
            }
            else if(data.first().contains("nome_alloggio")) {
                setUpRecyclerViewSoggiorni()
            } else if(data.first().contains("nome_crociera")) {
                setUpRecyclerViewCrociera()
            } else if(data.first().contains("nome_auto")) {
                setUpRecyclerViewAuto()
            } else {
                setUpRecyclerViewTaxi()
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
            binding.textView7.isVisible = false
            for (i in 0 until inputData.size) {
                val volo = inputData[i]
                Log.d("VOLO", "$volo")
                val flightDetails = formatFlightDetails(volo)
                data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, flightDetails))
            }
        } else {
            binding.textView7.isVisible = true
        }

        val adapter = CustomAdapterRisultati(data)
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : CustomAdapterRisultati.OnItemClickListener {
            override fun onItemClick(position: Int) {
                    // Ottenere l'elemento selezionato dalla lista dei dati
                        val selectedItem = data[position]

                    // Creare un'istanza del fragment dettagli e passare i dati necessari tramite argomenti
                        val detailsFragment = FragmentDettagli.newDettagliInstanceSoggiorno(selectedItem.toString())

                    // Sostituire il fragment corrente con il fragment dettagli utilizzando il FragmentManager
                        val fragmentManager = requireActivity().supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, detailsFragment)
                        .addToBackStack(null)
                        .commit()

            }
        })
    }

    private fun setUpRecyclerViewAuto() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val data = ArrayList<ItemsViewModelPreferiti>()
        val inputData = arguments?.getStringArrayList("data")
        if (!inputData.isNullOrEmpty()) {
            binding.textView7.isVisible = false
            for (i in 0 until inputData.size) {
                val auto = inputData[i]
                val autoDetails = formatAutoDetails(auto)
                data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, autoDetails))
            }
        } else {
            binding.textView7.isVisible = true
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

    private fun formatAutoDetails(jsonString: String?): String {
        val jsonObject = JSONObject(jsonString)
        val nome_auto = jsonObject.getString("nome_auto")
        val citta = jsonObject.getString("citta")
        val data_inizio_disponibilita = jsonObject.getString("data_inizio_disponibilita")
        val data_fine_disponibilita = jsonObject.getString("data_fine_disponibilita")
        val prezzo_giornaliero = jsonObject.getString("prezzo_giornaliero")
        val formattedString = StringBuilder()
        formattedString.append("$nome_auto")
        formattedString.append("\nIn $citta")
        formattedString.append("\nDisponibile dal:\n$data_inizio_disponibilita\nal $data_fine_disponibilita")
        formattedString.append("\nPrezzo giornaliero:\n$prezzo_giornaliero")
        return formattedString.toString()
    }

    private fun setUpRecyclerViewCrociera() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val data = ArrayList<ItemsViewModelPreferiti>()
        val inputData = arguments?.getStringArrayList("data")
        if (!inputData.isNullOrEmpty()) {
            binding.textView7.isVisible = false
            for (i in 0 until inputData.size) {
                val crociera = inputData[i]
                val crocieraDetails = formatCrocieraDetails(crociera)
                data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, crocieraDetails))
            }
        } else {
            binding.textView7.isVisible = true
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
    private fun formatCrocieraDetails(jsonString: String?): String {
        val jsonObject = JSONObject(jsonString)
        val nome_crociera = jsonObject.getString("nome_crociera")
        val citta_partenza = jsonObject.getString("citta_partenza")
        val data_partenza = jsonObject.getString("data_partenza")
        val data_ritorno = jsonObject.getString("data_ritorno")
        val prezzo_viaggio = jsonObject.getString("prezzo_viaggio")
        val formattedString = StringBuilder()
        formattedString.append("$nome_crociera")
        formattedString.append("\nDa $citta_partenza")
        formattedString.append("\nDal: $data_partenza\nal $data_ritorno")
        formattedString.append("\nPrezzo viaggio: $prezzo_viaggio")
        return formattedString.toString()
    }
    private fun setUpRecyclerViewTaxi() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val data = ArrayList<ItemsViewModelPreferiti>()
        val inputData = arguments?.getStringArrayList("data")
        if (!inputData.isNullOrEmpty()) {
            binding.textView7.isVisible = false
            for (i in 0 until inputData.size) {
                val taxi = inputData[i]
                val taxiDetails = formatTaxiDetails(taxi)
                data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, taxiDetails))
            }
        } else {
            binding.textView7.isVisible = true
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
    private fun formatTaxiDetails(jsonString: String?): String {
        val jsonObject = JSONObject(jsonString)
        val citta = jsonObject.getString("citta")
        val data_disponibilita = jsonObject.getString("data_disponibilita")
        val orario_disponibilita = jsonObject.getString("orario_disponibilita")
        val prezzo_orario = jsonObject.getString("prezzo_orario")
        val formattedString = StringBuilder()
        formattedString.append("Città: $citta")
        formattedString.append("\nData disponibilità $data_disponibilita")
        formattedString.append("\nOrario disponibilità: ${orario_disponibilita.substring(0, 5)}")
        formattedString.append("\nPrezzo orario: $prezzo_orario")
        return formattedString.toString()
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
            binding.textView7.isVisible = false
            for (i in 0 until inputData.size) {
                val soggiorno = inputData[i]
                val soggiorniDetails = formatSoggiorniDetails(soggiorno)
                data.add(ItemsViewModelPreferiti(R.drawable.pacchetto_famiglia, soggiorniDetails))
            }
        } else {
            binding.textView7.isVisible = true
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
        val dataInizio = jsonObject.getString("data_inizio_disponibilita")
        val dataRilascio = jsonObject.getString("data_fine_disponibilita")
        val costoGiornaliero = jsonObject.getString("costo_giornaliero")
        val numOspiti = jsonObject.getString("num_ospiti")

        val formattedString = StringBuilder()
        formattedString.append("$nomeAlloggio")
        formattedString.append("\nCittà: $citta")
        formattedString.append("\nDisponibile dal\n$dataInizio \nal $dataRilascio")
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
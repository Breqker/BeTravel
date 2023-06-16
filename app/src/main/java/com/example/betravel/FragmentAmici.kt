package com.example.betravel

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.FragmentFamigliaBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentAmici : Fragment() {

    private lateinit var binding: FragmentFamigliaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return super.onCreateView(inflater, container, savedInstanceState)
        } else {
            binding = FragmentFamigliaBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.volo.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            val dataVolo = ArrayList<ItemsViewModel>()
            val inputDataVolo = arguments?.getStringArrayList("data")
            if (!inputDataVolo.isNullOrEmpty()) {
                for (i in 0 until inputDataVolo.size) {
                    val volo = inputDataVolo[i]
                    val flightDetails = formatFlightDetails(volo)
                    dataVolo.add(ItemsViewModel(R.drawable.aereo, flightDetails))
                }
            }

            val adapterVolo = CustomAdapterPacchetti(dataVolo)
            binding.volo.adapter = adapterVolo

            val stringListVolo = dataVolo.map { it.text }
            val arrayAdapterVolo = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1, stringListVolo)

            voli(arrayAdapterVolo)

            binding.alloggio.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            val dataAlloggio = ArrayList<ItemsViewModel>()
            val inputDataAlloggio = arguments?.getStringArrayList("data")
            if (!inputDataAlloggio.isNullOrEmpty()) {
                for (i in 0 until inputDataAlloggio.size) {
                    val volo = inputDataAlloggio[i]
                    val flightDetails = formatSoggiorniDetails(volo)
                    dataAlloggio.add(ItemsViewModel(R.drawable.resort_santa_flavia, flightDetails))
                }
            }

            val adapterAlloggio = CustomAdapterPacchetti(dataAlloggio)
            binding.alloggio.adapter = adapterAlloggio

            val stringListAlloggio = dataAlloggio.map { it.text }
            val arrayAdapterAlloggio = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1, stringListAlloggio)

            alloggi(arrayAdapterAlloggio)

            binding.crociera.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            val dataCrociera = ArrayList<ItemsViewModel>()
            val inputDataCrociera = arguments?.getStringArrayList("data")
            if (!inputDataCrociera.isNullOrEmpty()) {
                for (i in 0 until inputDataCrociera.size) {
                    val crociera = inputDataCrociera[i]
                    val crocieraDetails = formatCrocieraDetails(crociera)
                    dataCrociera.add(ItemsViewModel(R.drawable.crociera, crocieraDetails))
                }
            }

            val adapterCrociera = CustomAdapterPacchetti(dataCrociera)
            binding.crociera.adapter = adapterCrociera

            val stringListCrociera = dataCrociera.map { it.text }
            val arrayAdapterCrociera = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1, stringListCrociera)

            crociere(arrayAdapterCrociera)

            binding.taxi.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            val dataTaxi = ArrayList<ItemsViewModel>()
            val inputDataTaxi = arguments?.getStringArrayList("data")
            if (!inputDataTaxi.isNullOrEmpty()) {
                for (i in 0 until inputDataTaxi.size) {
                    val taxies = inputDataTaxi[i]
                    val taxiDetails = formatTaxiDetails(taxies)
                    dataTaxi.add(ItemsViewModel(R.drawable.aereo, taxiDetails))
                }
            }

            val adapterTaxi = CustomAdapterPacchetti(dataTaxi)
            binding.taxi.adapter = adapterTaxi

            val stringListTaxi = dataTaxi.map { it.text }
            val arrayAdapterTaxi = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1, stringListTaxi)

            taxi(arrayAdapterTaxi)

            binding.auto.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            val dataAuto = ArrayList<ItemsViewModel>()
            val inputDataAuto = arguments?.getStringArrayList("data")
            if (!inputDataAuto.isNullOrEmpty()) {
                for (i in 0 until inputDataAuto.size) {
                    val car = inputDataAuto[i]
                    val carDetails = formatAutoDetails(car)
                    dataAuto.add(ItemsViewModel(R.drawable.aereo, carDetails))
                }
            }

            val adapterAuto = CustomAdapterPacchetti(dataTaxi)
            binding.auto.adapter = adapterAuto

            val stringListAuto = dataAuto.map { it.text }
            val arrayAdapterAuto = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1, stringListAuto)

            auto(arrayAdapterAuto)


            binding.buttonPrenota.setOnClickListener {
                if(dataVolo.any{it.isItemSelected} && dataAlloggio.any{it.isItemSelected}){
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,FragmentPagamento())
                        .addToBackStack(null)
                        .commit()
                }else{
                    showMessage("Seleziona un volo e/o un alloggio")
                }
            }

            return view

        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun voli(adapter : ArrayAdapter<String>){
        val query = "SELECT * FROM webmobile.Volo"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val voli = responseBody.getAsJsonArray("queryset")

                        if (voli.size() > 0) {
                            val flightList = mutableListOf<String>()
                            for (i in 0 until voli.size()) {
                                val volo = voli[i].toString()
                                val flight = volo.substringAfter(":").trim()
                                flightList.add(flight.substring(1, flight.length - 2))
                            }

                            activity?.runOnUiThread {
                                adapter.clear()
                                adapter.addAll(flightList)
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessun volo trovato")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero degli aeroporti di partenza")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                requireActivity().runOnUiThread {
                    showMessage("Errore di connessione: ${t.message}")
                }
            }

        })
    }

    private fun alloggi(adapter : ArrayAdapter<String>){
        val query = "SELECT * FROM webmobile.Alloggio"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val alloggi = responseBody.getAsJsonArray("queryset")

                        if (alloggi.size() > 0) {
                            val soggiornoList = mutableListOf<String>()
                            for (i in 0 until alloggi.size()) {
                                val alloggio = alloggi[i].toString()
                                val soggiorno = alloggio.substringAfter(":").trim()
                                soggiornoList.add(soggiorno.substring(1, soggiorno.length - 2))
                            }

                            activity?.runOnUiThread {
                                adapter.clear()
                                adapter.addAll(soggiornoList)
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessun alloggio trovato")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero degli aeroporti di partenza")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                requireActivity().runOnUiThread {
                    showMessage("Errore di connessione: ${t.message}")
                }
            }

        })
    }

    private fun crociere(adapter : ArrayAdapter<String>){
        val query = "SELECT * FROM webmobile.Crociera"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val crociere = responseBody.getAsJsonArray("queryset")

                        if (crociere.size() > 0) {
                            val cruiseList = mutableListOf<String>()
                            for (i in 0 until crociere.size()) {
                                val crociera = crociere[i].toString()
                                val cruise = crociera.substringAfter(":").trim()
                                cruiseList.add(cruise.substring(1, cruise.length - 2))
                            }

                            activity?.runOnUiThread {
                                adapter.clear()
                                adapter.addAll(cruiseList)
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessuna crociera trovata")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero degli aeroporti di partenza")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                requireActivity().runOnUiThread {
                    showMessage("Errore di connessione: ${t.message}")
                }
            }

        })
    }

    private fun taxi(adapter : ArrayAdapter<String>){
        val query = "SELECT * FROM webmobile.Taxi"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val taxi = responseBody.getAsJsonArray("queryset")

                        if (taxi.size() > 0) {
                            val taxiList = mutableListOf<String>()
                            for (i in 0 until taxi.size()) {
                                val taxies = taxi[i].toString()
                                val taxii = taxies.substringAfter(":").trim()
                                taxiList.add(taxii.substring(1, taxii.length - 2))
                            }

                            activity?.runOnUiThread {
                                adapter.clear()
                                adapter.addAll(taxiList)
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessun taxi trovato")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero degli aeroporti di partenza")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                requireActivity().runOnUiThread {
                    showMessage("Errore di connessione: ${t.message}")
                }
            }

        })
    }

    private fun auto(adapter : ArrayAdapter<String>){
        val query = "SELECT * FROM webmobile.Auto"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val auto = responseBody.getAsJsonArray("queryset")

                        if (auto.size() > 0) {
                            val autoList = mutableListOf<String>()
                            for (i in 0 until auto.size()) {
                                val car = auto[i].toString()
                                val cars = car.substringAfter(":").trim()
                                autoList.add(cars.substring(1, cars.length - 2))
                            }

                            activity?.runOnUiThread {
                                adapter.clear()
                                adapter.addAll(autoList)
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessuna auto trovata")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero degli aeroporti di partenza")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                requireActivity().runOnUiThread {
                    showMessage("Errore di connessione: ${t.message}")
                }
            }

        })
    }

    private fun formatSoggiorniDetails(jsonString: String): String {
        val jsonObject = JSONObject(jsonString)
        val codice_alloggio = jsonObject.getInt("codice_alloggio")
        val nomeAlloggio = jsonObject.getString("nome_alloggio")
        val citta = jsonObject.getString("citta")
        val dataInizio = jsonObject.getString("data_inizio_disponibilita")
        val dataRilascio = jsonObject.getString("data_fine_disponibilita")
        val costoGiornaliero = jsonObject.getString("costo_giornaliero")
        val numOspiti = jsonObject.getString("num_ospiti")

        val formattedString = StringBuilder()
        formattedString.append("$codice_alloggio\n")
        formattedString.append("$nomeAlloggio")
        formattedString.append("\nCittà: $citta")
        formattedString.append("\nDisponibile dal\n$dataInizio \nal $dataRilascio")
        formattedString.append("\nCosto giornaliero: $costoGiornaliero")
        formattedString.append("\nNumero ospiti: $numOspiti")

        return formattedString.toString()
    }

    private fun formatTaxiDetails(jsonString: String): String {
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

    private fun formatCrocieraDetails(jsonString: String): String {
        val jsonObject = JSONObject(jsonString)
        val codice_crociera = jsonObject.getInt("codice_crociera")
        val nome_crociera = jsonObject.getString("nome_crociera")
        val citta_partenza = jsonObject.getString("citta_partenza")
        val data_partenza = jsonObject.getString("data_partenza")
        val data_ritorno = jsonObject.getString("data_ritorno")
        val prezzo_viaggio = jsonObject.getString("prezzo_viaggio")

        val formattedString = StringBuilder()
        formattedString.append("$codice_crociera\n")
        formattedString.append("$nome_crociera")
        formattedString.append("\nDa $citta_partenza")
        formattedString.append("\nDal: $data_partenza\nal $data_ritorno")
        formattedString.append("\nPrezzo viaggio: $prezzo_viaggio")
        return formattedString.toString()
    }

    private fun formatAutoDetails(jsonString: String): String {
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
}
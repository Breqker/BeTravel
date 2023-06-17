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
import com.example.betravel.databinding.FragmentFamigliaOrizzontaleBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentAmici : Fragment() {

    private lateinit var binding: FragmentFamigliaBinding
    private lateinit var bindingOrizzontale : FragmentFamigliaOrizzontaleBinding
    private var flightListt: ArrayList<ItemsViewModel> = ArrayList<ItemsViewModel>()
    private var alloggioListt: ArrayList<ItemsViewModel> = ArrayList<ItemsViewModel>()
    private var cruiseListt : ArrayList<ItemsViewModel> = ArrayList<ItemsViewModel>()
    private var taxiListt: ArrayList<ItemsViewModel> = ArrayList<ItemsViewModel>()
    private var autoListt: ArrayList<ItemsViewModel> = ArrayList<ItemsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale = FragmentFamigliaOrizzontaleBinding.inflate(inflater, container, false)
            val view = bindingOrizzontale.root

            bindingOrizzontale.volo.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            voli{
                val dataVolo = getFlightList()
                val adapterVolo = CustomAdapterRisultati(dataVolo)
                bindingOrizzontale.volo.adapter = adapterVolo
            }

            bindingOrizzontale.alloggio.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            alloggi{
                val dataAlloggio = getAlloggioList()
                val adapterAlloggio = CustomAdapterRisultati(dataAlloggio)
                bindingOrizzontale.alloggio.adapter = adapterAlloggio
            }

            bindingOrizzontale.crociera.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            crociere {
                val dataCrociera = getCruiseList()
                val adapterCrociera = CustomAdapterRisultati(dataCrociera)
                bindingOrizzontale.crociera.adapter = adapterCrociera
            }

            bindingOrizzontale.taxi.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            taxi{
                val dataTaxi = getTaxiList()
                val adapterTaxi = CustomAdapterRisultati(dataTaxi)
                bindingOrizzontale.taxi.adapter = adapterTaxi
            }

            bindingOrizzontale.auto.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            auto {
                val dataAuto = getAutoList()
                val adapterAuto = CustomAdapterRisultati(dataAuto)
                bindingOrizzontale.auto.adapter = adapterAuto
            }



            bindingOrizzontale.buttonPrenota.setOnClickListener {
                val selectVolo = getFlightList().find { it.isItemSelected }
                val selectedAlloggio = getAlloggioList().find { it.isItemSelected }
                val selectedCrociera = getCruiseList().find { it.isItemSelected }
                val selectedTaxi = getTaxiList().find { it.isItemSelected }
                val selectedAuto = getAutoList().find { it.isItemSelected }

                val dataList = ArrayList<String>()

                if(selectVolo != null && selectedAlloggio != null){

                    dataList.add(selectVolo.toString())
                    dataList.add(selectedAlloggio.toString())
                    dataList.add(selectedCrociera.toString())
                    dataList.add(selectedTaxi.toString())
                    dataList.add(selectedAuto.toString())

                    val fragmentPagamentoPacchetto = FragmentPagamentoPacchetto.newPagamentoInstance(dataList)

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,fragmentPagamentoPacchetto)
                        .addToBackStack(null)
                        .commit()
                }else{
                    showMessage("Seleziona un volo e un alloggio")
                }
            }

            return view        } else {
            binding = FragmentFamigliaBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.volo.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            voli{
                val dataVolo = getFlightList()
                val adapterVolo = CustomAdapterRisultati(dataVolo)
                binding.volo.adapter = adapterVolo
            }

            binding.alloggio.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            alloggi{
                val dataAlloggio = getAlloggioList()
                val adapterAlloggio = CustomAdapterRisultati(dataAlloggio)
                binding.alloggio.adapter = adapterAlloggio
            }

            binding.crociera.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            crociere {
                val dataCrociera = getCruiseList()
                val adapterCrociera = CustomAdapterRisultati(dataCrociera)
                binding.crociera.adapter = adapterCrociera
            }

            binding.taxi.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            taxi{
                val dataTaxi = getTaxiList()
                val adapterTaxi = CustomAdapterRisultati(dataTaxi)
                binding.taxi.adapter = adapterTaxi
            }

            binding.auto.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            auto {
                val dataAuto = getAutoList()
                val adapterAuto = CustomAdapterRisultati(dataAuto)
                binding.auto.adapter = adapterAuto
            }

            binding.buttonPrenota.setOnClickListener {
                val selectVolo = getFlightList().find { it.isItemSelected }
                val selectedAlloggio = getAlloggioList().find { it.isItemSelected }
                val selectedCrociera = getCruiseList().find { it.isItemSelected }
                val selectedTaxi = getTaxiList().find { it.isItemSelected }
                val selectedAuto = getAutoList().find { it.isItemSelected }

                val dataList = ArrayList<String>()

                if(selectVolo != null && selectedAlloggio != null){

                    dataList.add(selectVolo.toString())
                    dataList.add(selectedAlloggio.toString())
                    dataList.add(selectedCrociera.toString())
                    dataList.add(selectedTaxi.toString())
                    dataList.add(selectedAuto.toString())

                    val fragmentPagamentoPacchetto = FragmentPagamentoPacchetto.newPagamentoInstance(dataList)

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,fragmentPagamentoPacchetto)
                        .addToBackStack(null)
                        .commit()
                }else{
                    showMessage("Seleziona un volo e un alloggio")
                }
            }

            return view

        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun setFlightList(flightList: ArrayList<ItemsViewModel>){
        flightListt = flightList
    }
    fun getFlightList(): ArrayList<ItemsViewModel>{
        return flightListt
    }

    fun setAlloggioList(alloggioList: ArrayList<ItemsViewModel>){
        alloggioListt = alloggioList
    }

    fun getAlloggioList(): ArrayList<ItemsViewModel>{
        return alloggioListt
    }

    fun setCruiseList(cruiseList: ArrayList<ItemsViewModel>){
        cruiseListt = cruiseList
    }
    fun getCruiseList(): ArrayList<ItemsViewModel>{
        return cruiseListt
    }

    fun setTaxiList(taxiList: ArrayList<ItemsViewModel>){
        taxiListt = taxiList
    }

    fun getTaxiList(): ArrayList<ItemsViewModel>{
        return taxiListt
    }

    fun setAutoList(autoList: ArrayList<ItemsViewModel>){
        autoListt = autoList
    }

    fun getAutoList(): ArrayList<ItemsViewModel>{
        return autoListt
    }
    private fun voli(callback: () -> Unit){
        val query = "SELECT * FROM webmobile.Volo"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val voli = responseBody.getAsJsonArray("queryset")
                        val flightList = ArrayList<ItemsViewModel>()

                        if (voli.size() > 0) {
                            for (i in 0 until voli.size()) {
                                val volo = voli[i].toString()
                                val flight = formatFlightDetails(volo)
                                flightList.add(ItemsViewModel(R.drawable.aereo,flight))
                            }
                            setFlightList(flightList)
                            callback.invoke()

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

    private fun alloggi(callback: () -> Unit){
        val query = "SELECT * FROM webmobile.Alloggio"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val alloggi = responseBody.getAsJsonArray("queryset")
                        val alloggiList = ArrayList<ItemsViewModel>()

                        if (alloggi.size() > 0) {
                            for (i in 0 until alloggi.size()) {
                                val alloggio = alloggi[i].toString()
                                val soggiorno = formatSoggiorniDetails(alloggio)
                                alloggiList.add(ItemsViewModel(R.drawable.resort_santa_flavia,soggiorno))
                            }
                            setAlloggioList(alloggiList)
                            callback.invoke()

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

    private fun crociere(callback: () -> Unit){
        val query = "SELECT * FROM webmobile.Crociera"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val crociere = responseBody.getAsJsonArray("queryset")
                        val cruiseList = ArrayList<ItemsViewModel>()
                        if (crociere.size() > 0) {
                            for (i in 0 until crociere.size()) {
                                val crociera = crociere[i].toString()
                                val cruise = formatCrocieraDetails(crociera)
                                cruiseList.add(ItemsViewModel(R.drawable.costa_fantastica,cruise))
                            }

                            setCruiseList(cruiseList)
                            callback.invoke()
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

    private fun taxi(callback: () -> Unit){
        val query = "SELECT * FROM webmobile.Taxi"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val taxi = responseBody.getAsJsonArray("queryset")
                        val taxiList = ArrayList<ItemsViewModel>()
                        if (taxi.size() > 0) {
                            for (i in 0 until taxi.size()) {
                                val taxies = taxi[i].toString()
                                val taxii = formatTaxiDetails(taxies)
                                taxiList.add(ItemsViewModel(R.drawable.taxi2,taxii))
                            }

                            setTaxiList(taxiList)
                            callback.invoke()
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

    private fun auto(callback: () -> Unit){
        val query = "SELECT * FROM webmobile.Auto"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val auto = responseBody.getAsJsonArray("queryset")
                        val autoList = ArrayList<ItemsViewModel>()

                        if (auto.size() > 0) {
                            for (i in 0 until auto.size()) {
                                val car = auto[i].toString()
                                val cars = formatAutoDetails(car)
                                autoList.add(ItemsViewModel(R.drawable.fiat_grande_punto,cars))
                            }

                            setAutoList(autoList)
                            callback.invoke()
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
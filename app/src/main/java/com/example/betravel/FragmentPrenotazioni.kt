package com.example.betravel

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.FragmentPrenotazioneBinding
import com.example.betravel.databinding.FragmentPrenotazioneOrizzontaleBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentPrenotazioni: Fragment() {

    private lateinit var binding: FragmentPrenotazioneBinding
    private lateinit var bindingOrizzontale : FragmentPrenotazioneOrizzontaleBinding

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
            bindingOrizzontale = FragmentPrenotazioneOrizzontaleBinding.inflate(inflater, container, false)
            val view = bindingOrizzontale.root

            setUpRecyclerViewOrizzontale()

            return view
        }else{
            binding = FragmentPrenotazioneBinding.inflate(inflater, container, false)
            val view = binding.root

            setUpRecyclerViewVerticale()

            return view
        }

    }

    private fun setUpRecyclerViewVerticale() {
        val prenotazioneList = ArrayList<ItemsViewModel>()
        val id = Utente.getId()
        binding.text.isVisible = false
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val query = "SELECT 'Alloggio' AS tipo, a.nome_alloggio AS nome, a.citta, a.data_inizio_disponibilita AS data_inizio, a.data_fine_disponibilita AS data_fine, a.costo_giornaliero\n" +
                "FROM Alloggio a\n" +
                "JOIN Prenotazione p ON p.codice_alloggio = a.codice_alloggio\n" +
                "WHERE p.id_utente = '$id'\n" +
                "UNION ALL\n" +
                "SELECT 'Auto' AS tipo, a.nome_auto AS nome, a.citta, a.data_inizio_disponibilita AS data_inizio, a.data_fine_disponibilita AS data_fine, a.prezzo_giornaliero\n" +
                "FROM Auto a\n" +
                "JOIN Prenotazione p ON p.id_auto = a.id_auto\n" +
                "WHERE p.id_utente = '$id'\n" +
                "UNION ALL\n" +
                "SELECT 'Taxi' AS tipo, '' AS nome, t.citta, t.data_disponibilita AS data_inizio, t.orario_disponibilita AS data_fine, t.prezzo_orario\n" +
                "FROM Taxi t\n" +
                "JOIN Prenotazione p ON p.id_taxi = t.id_taxi\n" +
                "WHERE p.id_utente = '$id'\n" +
                "UNION ALL\n" +
                "SELECT 'Crociera' AS tipo, c.nome_crociera AS nome, c.citta_partenza AS data_inizio, c.data_partenza AS data_fine, c.data_ritorno, c.prezzo_viaggio\n" +
                "FROM Crociera c\n" +
                "JOIN Prenotazione p ON p.codice_crociera = c.codice_crociera\n" +
                "WHERE p.id_utente = '$id'\n" +
                "UNION ALL\n" +
                "SELECT 'Volo' AS tipo, v.nome_volo AS nome, v.aeroporto_partenza, v.aeroporto_arrivo, v.data_partenza AS data_inizio, v.data_ritorno AS data_fine\n" +
                "FROM Volo v\n" +
                "JOIN Prenotazione p ON p.id_volo = v.codice\n" +
                "WHERE p.id_utente = '$id';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val prenotazioniData = responseBody.getAsJsonArray("queryset")

                        if (prenotazioniData.size() > 0) {
                            for (i in 0 until prenotazioniData.size()) {
                                val prenotazione = prenotazioniData[i].asJsonObject
                                val tipo = prenotazione.get("tipo").asString
                                val nome = prenotazione.get("nome").asString
                                val citta = prenotazione.get("citta").asString
                                val dataInizio = prenotazione.get("data_inizio").asString
                                val dataFine = prenotazione.get("data_fine").asString
                                val costo = if (prenotazione.get("costo_giornaliero").isJsonNull) {
                                    "N/A"
                                } else {
                                    prenotazione.get("costo_giornaliero").asString
                                }

                                if (tipo=="Alloggio"){
                                    val item = ItemsViewModel(R.drawable.soggiorno, "$nome\n$citta\nDal $dataInizio\nal ${dataFine.subSequence(0,10)} \nCosto giornaliero: $costo €")
                                    prenotazioneList.add(item)
                                } else if(tipo=="Volo") {
                                    val item = ItemsViewModel(R.drawable.volo, "$nome\nDa $citta\na $dataInizio\nPartenza${dataFine.subSequence(0,10)}\nRitorno: $costo")
                                    prenotazioneList.add(item)
                                } else if(tipo=="Taxi") {
                                    val item = ItemsViewModel(R.drawable.taxi, "$nome\n$citta\n$dataInizio\n${dataFine.subSequence(0,10)}\nOrario: ${dataFine.subSequence(0,10)}: ${dataFine.subSequence(0,10)}\nCosto: $costo €")
                                    prenotazioneList.add(item)
                                } else if(tipo=="Crociera") {
                                    val item = ItemsViewModel(R.drawable.crociera, "$nome\n$citta\nPartenza: $dataInizio\nRitorno: ${dataFine.subSequence(0,10)}\n$costo €")
                                    prenotazioneList.add(item)
                                } else {
                                    val item = ItemsViewModel(R.drawable.noleggio_auto, "$nome\n$citta\n$dataInizio\n${dataFine.subSequence(0,10)}")
                                    prenotazioneList.add(item)
                                }
                                val adapter = CustomAdapterPreferiti(prenotazioneList)
                                binding.recyclerView.adapter = adapter

                            }
                        } else {
                            binding.text.isVisible = true
                        }

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

    private fun setUpRecyclerViewOrizzontale() {
        val prenotazioneList = ArrayList<ItemsViewModel>()
        val id = Utente.getId()
        bindingOrizzontale.text.isVisible = false
        bindingOrizzontale.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val query = "SELECT 'Alloggio' AS tipo, a.nome_alloggio AS nome, a.citta, a.data_inizio_disponibilita AS data_inizio, a.data_fine_disponibilita AS data_fine, a.costo_giornaliero\n" +
                "FROM Alloggio a\n" +
                "JOIN Prenotazione p ON p.codice_alloggio = a.codice_alloggio\n" +
                "WHERE p.id_utente = '$id'\n" +
                "UNION ALL\n" +
                "SELECT 'Auto' AS tipo, a.nome_auto AS nome, a.citta, a.data_inizio_disponibilita AS data_inizio, a.data_fine_disponibilita AS data_fine, a.prezzo_giornaliero\n" +
                "FROM Auto a\n" +
                "JOIN Prenotazione p ON p.id_auto = a.id_auto\n" +
                "WHERE p.id_utente = '$id'\n" +
                "UNION ALL\n" +
                "SELECT 'Taxi' AS tipo, '' AS nome, t.citta, t.data_disponibilita AS data_inizio, t.orario_disponibilita AS data_fine, t.prezzo_orario\n" +
                "FROM Taxi t\n" +
                "JOIN Prenotazione p ON p.id_taxi = t.id_taxi\n" +
                "WHERE p.id_utente = '$id'\n" +
                "UNION ALL\n" +
                "SELECT 'Crociera' AS tipo, c.nome_crociera AS nome, c.citta_partenza AS data_inizio, c.data_partenza AS data_fine, c.data_ritorno, c.prezzo_viaggio\n" +
                "FROM Crociera c\n" +
                "JOIN Prenotazione p ON p.codice_crociera = c.codice_crociera\n" +
                "WHERE p.id_utente = '$id'\n" +
                "UNION ALL\n" +
                "SELECT 'Volo' AS tipo, v.nome_volo AS nome, v.aeroporto_partenza, v.aeroporto_arrivo, v.data_partenza AS data_inizio, v.data_ritorno AS data_fine\n" +
                "FROM Volo v\n" +
                "JOIN Prenotazione p ON p.id_volo = v.codice\n" +
                "WHERE p.id_utente = '$id';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val prenotazioniData = responseBody.getAsJsonArray("queryset")

                        if (prenotazioniData.size() > 0) {
                            for (i in 0 until prenotazioniData.size()) {
                                val prenotazione = prenotazioniData[i].asJsonObject
                                val tipo = prenotazione.get("tipo").asString
                                val nome = prenotazione.get("nome").asString
                                val citta = prenotazione.get("citta").asString
                                val dataInizio = prenotazione.get("data_inizio").asString
                                val dataFine = prenotazione.get("data_fine").asString
                                val costo = if (prenotazione.get("costo_giornaliero").isJsonNull) {
                                    "N/A"
                                } else {
                                    prenotazione.get("costo_giornaliero").asString
                                }

                                if (tipo=="Alloggio"){
                                    val item = ItemsViewModel(R.drawable.soggiorno, "$nome\n$citta\nDal $dataInizio\nal ${dataFine.subSequence(0,10)} \nCosto giornaliero: $costo €")
                                    prenotazioneList.add(item)
                                } else if(tipo=="Volo") {
                                    val item = ItemsViewModel(R.drawable.volo, "$nome\nDa $citta\na $dataInizio\nPartenza${dataFine.subSequence(0,10)}\nRitorno: $costo")
                                    prenotazioneList.add(item)
                                } else if(tipo=="Taxi") {
                                    val item = ItemsViewModel(R.drawable.taxi, "$nome\n$citta\n$dataInizio\n${dataFine.subSequence(0,10)}\nOrario: ${dataFine.subSequence(0,10)}: ${dataFine.subSequence(0,10)}\nCosto: $costo €")
                                    prenotazioneList.add(item)
                                } else if(tipo=="Crociera") {
                                    val item = ItemsViewModel(R.drawable.crociera, "$nome\n$citta\nPartenza: $dataInizio\nRitorno: ${dataFine.subSequence(0,10)}\n$costo €")
                                    prenotazioneList.add(item)
                                } else {
                                    val item = ItemsViewModel(R.drawable.noleggio_auto, "$nome\n$citta\n$dataInizio\n${dataFine.subSequence(0,10)}")
                                    prenotazioneList.add(item)
                                }
                                val adapter = CustomAdapterPreferiti(prenotazioneList)
                                bindingOrizzontale.recyclerView.adapter = adapter

                            }
                        } else {
                            bindingOrizzontale.text.isVisible = true
                        }

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

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
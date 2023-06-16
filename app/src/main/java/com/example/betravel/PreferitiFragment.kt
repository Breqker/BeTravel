package com.example.betravel

import Utente
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.FragmentPreferitiBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreferitiFragment : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentPreferitiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreferitiBinding.inflate(inflater, container, false)
        val view = binding.root

        prendiPreferiti()

        return view
    }

    private fun prendiPreferiti(): ArrayList<ItemsViewModel> {
        val id = Utente.getId()
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val preferiti = ArrayList<ItemsViewModel>()

        val query = "SELECT distinct 'Alloggio' AS tipo, nome_alloggio AS nome, citta, data_inizio_disponibilita, data_fine_disponibilita, costo_giornaliero\n" +
                "            FROM Alloggio a, Preferito p\n" +
                "            WHERE p.id_utente = '$id' AND a.codice_alloggio = p.codice_alloggio \n" +
                "            UNION ALL\n" +
                "SELECT distinct 'Auto' AS tipo, nome_auto AS nome, citta, data_inizio_disponibilita, data_fine_disponibilita, prezzo_giornaliero\n" +
                "            FROM Auto a, Preferito p\n" +
                "            WHERE p.id_utente = '$id' AND a.id_auto = p.id_auto\n" +
                "            UNION ALL\n" +
                "SELECT distinct 'Taxi' AS tipo, '' AS nome, citta, data_disponibilita, orario_disponibilita, prezzo_orario\n" +
                "            FROM Taxi t, Preferito p\n" +
                "            WHERE p.id_utente = '$id' AND t.id_taxi=p.id_taxi\n" +
                "            UNION ALL\n" +
                "SELECT distinct 'Crociera' AS tipo, nome_crociera AS nome, citta_partenza, data_partenza, data_ritorno, prezzo_viaggio\n" +
                "            FROM Crociera c, Preferito p\n" +
                "            WHERE p.id_utente = '$id' AND c.codice_crociera = p.codice_crociera\n" +
                "            UNION ALL\n" +
                "SELECT distinct 'Volo' AS tipo, nome_volo AS nome, aeroporto_partenza, aeroporto_arrivo, data_partenza, costo_biglietto\n" +
                "            FROM Volo v, Preferito p\n" +
                "            WHERE p.id_utente = '$id' AND v.codice = p.id_volo\n"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val preferitiData = responseBody.getAsJsonArray("queryset")

                        if (preferitiData.size() > 0) {
                            binding.textView .isVisible = false
                            for (i in 0 until preferitiData.size()) {
                                val preferito = preferitiData[i].asJsonObject
                                val tipo = preferito.get("tipo").asString
                                val nome = preferito.get("nome").asString
                                val citta = preferito.get("citta").asString
                                val dataInizio = preferito.get("data_inizio_disponibilita").asString
                                val dataFine = preferito.get("data_fine_disponibilita").asString
                                val costo = preferito.get("costo_giornaliero").asString

                                if (tipo=="Alloggio"){
                                    val item = ItemsViewModel(R.drawable.soggiorno, "$nome\n$citta\nDal $dataInizio\nal ${dataFine.subSequence(0,10)} \nCosto giornaliero: $costo €")
                                    preferiti.add(item)
                                } else if(tipo=="Volo") {
                                    val item = ItemsViewModel(R.drawable.volo, "$nome\nDa $citta\na $dataInizio\nPartenza${dataFine.subSequence(0,10)}\nRitorno: $costo")
                                    preferiti.add(item)
                                } else if(tipo=="Taxi") {
                                    val item = ItemsViewModel(R.drawable.taxi, "$nome\n$citta\n$dataInizio\n${dataFine.subSequence(0,10)}\nOrario: ${dataFine.subSequence(0,10)}: ${dataFine.subSequence(11,16)}\nCosto: $costo €")
                                    preferiti.add(item)
                                } else if(tipo=="Crociera") {
                                    val item = ItemsViewModel(R.drawable.crociera, "$nome\n$citta\nPartenza: $dataInizio\nRitorno: ${dataFine.subSequence(0,10)}\n$costo €")
                                    preferiti.add(item)
                                } else {
                                    val item = ItemsViewModel(R.drawable.noleggio_auto, "$nome\n$citta\n$dataInizio\n${dataFine.subSequence(0,10)}")
                                    preferiti.add(item)
                                }
                                val adapter = CustomAdapterPreferiti(preferiti)
                                binding.recyclerView.adapter = adapter

                            }
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

        return preferiti
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

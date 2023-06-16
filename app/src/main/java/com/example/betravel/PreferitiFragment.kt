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
                "SELECT distinct 'Volo' AS tipo, nome_volo AS nome, aeroporto_partenza, aeroporto_arrivo, data_partenza, data_ritorno\n" +
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


    /*private fun prendiPreferitiAlloggio():ArrayList<ItemsViewModel> {
        val id = Utente.getId()
        val preferiti = ArrayList<ItemsViewModel>()

        val query = "SELECT nome_alloggio, citta, data_inizio_disponibilita, data_fine_disponibilita, costo_giornaliero " +
                "FROM Alloggio a, Preferito p " +
                "WHERE p.id_utente = '$id' AND a.codice_alloggio = p.codice_alloggio"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val alloggio = responseBody.getAsJsonArray("queryset")

                        if (alloggio.size() > 0) {
                            for (i in 0 until alloggio.size()) {
                                val alloggio = alloggio[i].asJsonObject
                                val nome_alloggio = alloggio.get("nome_alloggio").asString
                                val citta = alloggio.get("citta").asString
                                val data_inizio_disponibilita = alloggio.get("data_inizio_disponibilita").asString
                                val data_fine_disponibilita = alloggio.get("data_fine_disponibilita").asString
                                val costo_giornaliero = alloggio.get("costo_giornaliero").asString

                                val item = ItemsViewModel(
                                    R.drawable.pacchetto_famiglia,
                                    "$nome_alloggio\nCittà: $citta\nDisponible dal\n$data_inizio_disponibilita\n" +
                                            "al: $data_fine_disponibilita\n" +
                                            "Costo giornaliero: $costo_giornaliero\n"
                                )
                                preferiti.add(item)
                                val adapter = CustomAdapterPreferiti(preferiti)
                                binding.recyclerView.adapter = adapter

                            }

                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessun auto preferita trovata")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero delle auto preferite")
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

    private fun prendiPreferitiAuto():ArrayList<ItemsViewModel> {
        val id = Utente.getId()
        val preferiti = ArrayList<ItemsViewModel>()

        val query = "SELECT nome_auto, citta, data_inizio_disponibilita, data_fine_disponibilita, prezzo_giornaliero " +
                "FROM Auto a, Preferito p " +
                "WHERE p.id_utente = '$id' AND a.id_auto = p.id_auto"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val auto = responseBody.getAsJsonArray("queryset")

                        if (auto.size() > 0) {
                            for (i in 0 until auto.size()) {
                                val auto = auto[i].asJsonObject
                                val nome_auto = auto.get("nome_auto").asString
                                val citta = auto.get("citta").asString
                                val data_inizio_disponibilita = auto.get("data_inizio_disponibilita").asString
                                val data_fine_disponibilita = auto.get("data_fine_disponibilita").asString
                                val prezzo_giornaliero = auto.get("prezzo_giornaliero").asString

                                val item = ItemsViewModel(
                                    R.drawable.pacchetto_famiglia,
                                    "$nome_auto\nCittà: $citta\nDisponible dal\n$data_inizio_disponibilita\n" +
                                            "al: $data_fine_disponibilita\n" +
                                            "Prezzo: $prezzo_giornaliero\n"
                                )
                                preferiti.add(item)
                            }
                            val adapter = CustomAdapterPreferiti(preferiti)
                            binding.recyclerView.adapter = adapter

                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessun auto preferita trovata")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero delle auto preferite")
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

    private fun prendiPreferitiTaxi():ArrayList<ItemsViewModel> {
        val id = Utente.getId()
        val preferiti = ArrayList<ItemsViewModel>()

        val query = "SELECT citta, data_disponibilita, orario_disponibilita, prezzo_orario " +
                "FROM Taxi t, Preferito p " +
                "WHERE p.id_utente = '$id' AND t.id_taxi=p.id_taxi"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val taxi = responseBody.getAsJsonArray("queryset")

                        if (taxi.size() > 0) {
                            for (i in 0 until taxi.size()) {
                                val taxi = taxi[i].asJsonObject
                                val citta = taxi.get("citta").asString
                                val data_disponibilita = taxi.get("data_disponibilita").asString
                                val orario_disponibilita = taxi.get("orario_disponibilita").asString
                                val prezzo_orario = taxi.get("prezzo_orario").asString

                                val item = ItemsViewModel(
                                    R.drawable.pacchetto_famiglia,
                                    "$citta\nDisponible il\n$data_disponibilita\n" +
                                            "Alle: $orario_disponibilita\n" +
                                            "Prezzo: $prezzo_orario\n"
                                )
                                preferiti.add(item)

                            }
                            val adapter = CustomAdapterPreferiti(preferiti)
                            binding.recyclerView.adapter = adapter

                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessun taxi preferito trovato")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero dei taxi preferiti")
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

    private fun prendiPreferitiCrociera():ArrayList<ItemsViewModel> {
        val id = Utente.getId()
        val preferiti = ArrayList<ItemsViewModel>()

        val query = "SELECT nome_crociera, citta_partenza, data_partenza, data_ritorno, prezzo_viaggio " +
                "FROM Crociera c, Preferito p " +
                "WHERE p.id_utente = '$id' AND c.codice_crociera = p.codice_crociera"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val crociera = responseBody.getAsJsonArray("queryset")

                        if (crociera.size() > 0) {
                            for (i in 0 until crociera.size()) {
                                val crociera = crociera[i].asJsonObject
                                val nome_crociera = crociera.get("nome_crociera").asString
                                val citta_partenza = crociera.get("citta_partenza").asString
                                val data_partenza = crociera.get("data_partenza").asString
                                val data_ritorno = crociera.get("data_ritorno").asString
                                val prezzo_viaggio = crociera.get("prezzo_viaggio").asString

                                val item = ItemsViewModel(
                                    R.drawable.pacchetto_famiglia,
                                    "$nome_crociera\nPartenza: $citta_partenza\n" +
                                            "Data partenza: $data_partenza\nData ritorno: $data_ritorno\n" +
                                            "Prezzo: $prezzo_viaggio\n"
                                )
                                preferiti.add(item)
                            }
                            val adapter = CustomAdapterPreferiti(preferiti)
                            binding.recyclerView.adapter = adapter
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessun crociera preferito trovata")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero delle crociere preferite")
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

    private fun prendiPreferitiVolo(): ArrayList<ItemsViewModel> {
        val id = Utente.getId()
        val preferiti = ArrayList<ItemsViewModel>()

        val query = "SELECT nome_volo, aeroporto_partenza, aeroporto_arrivo, data_partenza, data_ritorno, ora_partenza, ora_arrivo, costo_biglietto " +
                "FROM Volo v, Preferito p " +
                "WHERE p.id_utente = '$id' AND v.codice = p.id_volo"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val voli = responseBody.getAsJsonArray("queryset")

                        if (voli.size() > 0) {
                            for (i in 0 until voli.size()) {
                                val volo = voli[i].asJsonObject
                                val nomeVolo = volo.get("nome_volo").asString
                                val aeroportoPartenza = volo.get("aeroporto_partenza").asString
                                val aeroportoArrivo = volo.get("aeroporto_arrivo").asString
                                val dataPartenza = volo.get("data_partenza").asString
                                val dataRitorno = volo.get("data_ritorno").asString
                                val oraPartenza = volo.get("ora_partenza").asString
                                val oraArrivo = volo.get("ora_arrivo").asString
                                val costoBiglietto = volo.get("costo_biglietto").asDouble

                                val item = ItemsViewModel(
                                    R.drawable.pacchetto_famiglia,
                                    "Volo: $nomeVolo\nPartenza: $aeroportoPartenza\nRitorno: $aeroportoArrivo\n" +
                                            "Data partenza: $dataPartenza\nData ritorno: $dataRitorno\n" +
                                            "Ora partenza: $oraPartenza\nOra arrivo: $oraArrivo\n" +
                                            "Costo biglietto: $costoBiglietto"
                                )
                                preferiti.add(item)
                            }
                            val adapter = CustomAdapterPreferiti(preferiti)
                            binding.recyclerView.adapter = adapter
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessun volo preferito trovato")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero dei voli preferiti")
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
    }*/


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

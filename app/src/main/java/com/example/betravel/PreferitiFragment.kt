package com.example.betravel

import Utente
import android.content.res.Configuration
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
import com.example.betravel.databinding.FragmentPreferitiOrizzontaleBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreferitiFragment : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentPreferitiBinding
    private lateinit var bindingOrizzontale : FragmentPreferitiOrizzontaleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale = FragmentPreferitiOrizzontaleBinding.inflate(inflater, container, false)
            val view = bindingOrizzontale.root

            prendiPreferiti()

            return view
        }else{
            binding = FragmentPreferitiBinding.inflate(inflater, container, false)
            val view = binding.root

            prendiPreferiti()

            return view
        }
    }

    private fun gestisciClick() {
        val adapter = binding.recyclerView.adapter as? CustomAdapterPreferiti
        adapter?.setOnItemClickListener(object : CustomAdapterPreferiti.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val listItem = prendiPreferiti()

                val selectedItem = listItem[position]

                Log.d("ITEM SELEZIONATO", "$selectedItem")
            }
        })
    }

    private fun prendiPreferiti(): ArrayList<ItemsViewModel> {
        val id = Utente.getId()
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale.recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }else{
            binding.recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
        val preferiti = ArrayList<ItemsViewModel>()

        val query = "SELECT distinct 'Alloggio' AS tipo, a.codice_alloggio AS codice, nome_alloggio AS nome, citta, data_inizio_disponibilita, data_fine_disponibilita, costo_giornaliero\n" +
                "            FROM Alloggio a, Preferito p\n" +
                "            WHERE p.id_utente = '$id' AND a.codice_alloggio = p.codice_alloggio \n" +
                "            UNION ALL\n" +
                "SELECT distinct 'Auto' AS tipo, a.id_auto AS codice, nome_auto AS nome, citta, data_inizio_disponibilita, data_fine_disponibilita, prezzo_giornaliero\n" +
                "            FROM Auto a, Preferito p\n" +
                "            WHERE p.id_utente = '$id' AND a.id_auto = p.id_auto\n" +
                "            UNION ALL\n" +
                "SELECT distinct 'Taxi' AS tipo, t.id_taxi AS codice, '' AS nome, citta, data_disponibilita, orario_disponibilita, prezzo_orario\n" +
                "            FROM Taxi t, Preferito p\n" +
                "            WHERE p.id_utente = '$id' AND t.id_taxi=p.id_taxi\n" +
                "            UNION ALL\n" +
                "SELECT distinct 'Crociera' AS tipo, c.codice_crociera AS codice, nome_crociera AS nome, citta_partenza, data_partenza, data_ritorno, prezzo_viaggio\n" +
                "            FROM Crociera c, Preferito p\n" +
                "            WHERE p.id_utente = '$id' AND c.codice_crociera = p.codice_crociera\n" +
                "            UNION ALL\n" +
                "SELECT distinct 'Volo' AS tipo, v.codice AS codice, nome_volo AS nome, aeroporto_partenza, aeroporto_arrivo, data_partenza, costo_biglietto\n" +
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
                            val orientation = resources.configuration.orientation
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                bindingOrizzontale.textView .isVisible = false
                            }else{
                                binding.textView .isVisible = false
                            }
                            for (i in 0 until preferitiData.size()) {
                                val preferito = preferitiData[i].asJsonObject
                                val tipo = preferito.get("tipo").asString
                                val codice = preferito.get("codice").asString
                                val nome = preferito.get("nome").asString
                                val citta = preferito.get("citta").asString
                                val dataInizio = preferito.get("data_inizio_disponibilita").asString
                                val dataFine = preferito.get("data_fine_disponibilita").asString
                                val costo = preferito.get("costo_giornaliero").asString

                                if (tipo=="Alloggio"){
                                    val item = ItemsViewModel(R.drawable.soggiorno, "$codice\n$nome\n$citta\nDal $dataInizio\nal ${dataFine.subSequence(0,10)} \nCosto giornaliero: $costo €")
                                    preferiti.add(item)
                                } else if(tipo=="Volo") {
                                    val item = ItemsViewModel(R.drawable.volo, "$codice\n$nome\nDa $citta\na $dataInizio\nPartenza${dataFine.subSequence(0,10)}\nRitorno: $costo")
                                    preferiti.add(item)
                                } else if(tipo=="Taxi") {
                                    val item = ItemsViewModel(R.drawable.taxi, "$codice\n$nome\n$citta\n$dataInizio\n${dataFine.subSequence(0,10)}\nOrario: ${dataFine.subSequence(0,10)}: ${dataFine.subSequence(11,16)}\nCosto: $costo €")
                                    preferiti.add(item)
                                } else if(tipo=="Crociera") {
                                    val item = ItemsViewModel(R.drawable.crociera, "$codice\n$nome\n$citta\nPartenza: $dataInizio\nRitorno: ${dataFine.subSequence(0,10)}\n$costo €")
                                    preferiti.add(item)
                                } else {
                                    val item = ItemsViewModel(R.drawable.noleggio_auto, "$codice\n$nome\n$citta\n$dataInizio\n${dataFine.subSequence(0,10)}")
                                    preferiti.add(item)
                                }
                            }
                            val adapter = CustomAdapterPreferiti(preferiti)
                            val orientation = resources.configuration.orientation
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                bindingOrizzontale.recyclerView.adapter = adapter
                            adapter?.setOnItemClickListener(object : CustomAdapterPreferiti.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val selectedItem = preferiti[position]

                                    Log.d("ITEM SELEZIONATO", "$selectedItem")

                                    Log.d("ALLOGGIO", "${selectedItem.toString().contains("Costo giornaliero")}")
                                    if (selectedItem.toString().contains("Costo giornaliero")) {
                                        val detailsFragment = FragmentDettagli.newDettagliInstance(selectedItem.toString(), "FragmentPreferiti")

                                        val fragmentManager = requireActivity().supportFragmentManager
                                        fragmentManager.beginTransaction()
                                            .replace(R.id.fragmentContainerView, detailsFragment)
                                            .addToBackStack(null)
                                            .commit()
                                    } else if (selectedItem.toString().contains("Orario")) {
                                        val detailsFragment = FragmentDettagli.newDettagliInstance(selectedItem.toString(), "FragmentPreferiti")

                                        val fragmentManager = requireActivity().supportFragmentManager
                                        fragmentManager.beginTransaction()
                                            .replace(R.id.fragmentContainerView, detailsFragment)
                                            .addToBackStack(null)
                                            .commit()
                                    } else if (selectedItem.toString().contains("Partenza") && selectedItem.toString().contains("€")) {
                                        val detailsFragment = FragmentDettagli.newDettagliInstance(selectedItem.toString(), "FragmentPreferiti")

                                        val fragmentManager = requireActivity().supportFragmentManager
                                        fragmentManager.beginTransaction()
                                            .replace(R.id.fragmentContainerView, detailsFragment)
                                            .addToBackStack(null)
                                            .commit()
                                    } else if (selectedItem.toString().contains("Partenza")) {
                                        val detailsFragment = FragmentDettagli.newDettagliInstance(selectedItem.toString(), "FragmentPreferiti")

                                        val fragmentManager = requireActivity().supportFragmentManager
                                        fragmentManager.beginTransaction()
                                            .replace(R.id.fragmentContainerView, detailsFragment)
                                            .addToBackStack(null)
                                            .commit()
                                    } else {
                                        val detailsFragment = FragmentDettagli.newDettagliInstance(selectedItem.toString(), "FragmentPreferiti")

                                        val fragmentManager = requireActivity().supportFragmentManager
                                        fragmentManager.beginTransaction()
                                            .replace(R.id.fragmentContainerView, detailsFragment)
                                            .addToBackStack(null)
                                            .commit()
                                    }
                                }
                            })
                        } else {
                                binding.recyclerView.adapter = adapter
                                adapter?.setOnItemClickListener(object : CustomAdapterPreferiti.OnItemClickListener {
                                    override fun onItemClick(position: Int) {
                                        val selectedItem = preferiti[position]

                                        Log.d("ITEM SELEZIONATO", "$selectedItem")

                                        Log.d(
                                            "ALLOGGIO",
                                            "${
                                                selectedItem.toString()
                                                    .contains("Costo giornaliero")
                                            }"
                                        )
                                        if (selectedItem.toString().contains("Costo giornaliero")) {
                                            val detailsFragment =
                                                FragmentDettagli.newDettagliInstance(
                                                    selectedItem.toString(),
                                                    "FragmentPreferiti"
                                                )

                                            val fragmentManager =
                                                requireActivity().supportFragmentManager
                                            fragmentManager.beginTransaction()
                                                .replace(
                                                    R.id.fragmentContainerView,
                                                    detailsFragment
                                                )
                                                .addToBackStack(null)
                                                .commit()
                                        } else if (selectedItem.toString().contains("Orario")) {
                                            val detailsFragment =
                                                FragmentDettagli.newDettagliInstance(
                                                    selectedItem.toString(),
                                                    "FragmentPreferiti"
                                                )

                                            val fragmentManager =
                                                requireActivity().supportFragmentManager
                                            fragmentManager.beginTransaction()
                                                .replace(
                                                    R.id.fragmentContainerView,
                                                    detailsFragment
                                                )
                                                .addToBackStack(null)
                                                .commit()
                                        } else if (selectedItem.toString()
                                                .contains("Partenza") && selectedItem.toString()
                                                .contains("€")
                                        ) {
                                            val detailsFragment =
                                                FragmentDettagli.newDettagliInstance(
                                                    selectedItem.toString(),
                                                    "FragmentPreferiti"
                                                )

                                            val fragmentManager =
                                                requireActivity().supportFragmentManager
                                            fragmentManager.beginTransaction()
                                                .replace(
                                                    R.id.fragmentContainerView,
                                                    detailsFragment
                                                )
                                                .addToBackStack(null)
                                                .commit()
                                        } else if (selectedItem.toString().contains("Partenza")) {
                                            val detailsFragment =
                                                FragmentDettagli.newDettagliInstance(
                                                    selectedItem.toString(),
                                                    "FragmentPreferiti"
                                                )

                                            val fragmentManager =
                                                requireActivity().supportFragmentManager
                                            fragmentManager.beginTransaction()
                                                .replace(
                                                    R.id.fragmentContainerView,
                                                    detailsFragment
                                                )
                                                .addToBackStack(null)
                                                .commit()
                                        } else {
                                            val detailsFragment =
                                                FragmentDettagli.newDettagliInstance(
                                                    selectedItem.toString(),
                                                    "FragmentPreferiti"
                                                )

                                            val fragmentManager =
                                                requireActivity().supportFragmentManager
                                            fragmentManager.beginTransaction()
                                                .replace(
                                                    R.id.fragmentContainerView,
                                                    detailsFragment
                                                )
                                                .addToBackStack(null)
                                                .commit()
                                        }
                                    }
                                }
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

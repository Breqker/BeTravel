package com.example.betravel

import CustomAdapterReview
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.FragmentDettagliBinding
import com.example.betravel.databinding.FragmentDettagliOrizzontaleBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentDettagli : Fragment() {

    private lateinit var binding: FragmentDettagliBinding
    private lateinit var bindingOrizzontale : FragmentDettagliOrizzontaleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val id = Utente.getId()

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            bindingOrizzontale = FragmentDettagliOrizzontaleBinding.inflate(inflater,container,false)
            val view = bindingOrizzontale.root

            val data = arguments?.getString(ARG_DATA)

            if (data != null) {
                bindingOrizzontale.textViewDettagli.text = data
            }


            bindingOrizzontale.prenota.setOnClickListener {
                val textViewDettagli = bindingOrizzontale.textViewDettagli.text.toString()
                val fragment = FragmentPagamento.newPagamentoInstance(textViewDettagli)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }


            return view
        }else{
            binding = FragmentDettagliBinding.inflate(inflater,container,false)
            val view = binding.root

            val data = arguments?.getString(ARG_DATA)

            if (data != null) {
                binding.textViewDettagli.text = data
            }


            binding.prenota.setOnClickListener {
                val textViewDettagli = binding.textViewDettagli.text.toString()
                val fragment = FragmentPagamento.newPagamentoInstance(textViewDettagli)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }


            return view
        }
    }

    private fun preferitiVolo(id: Int,codice: Int){

        val insertQuery = "INSERT INTO webmobile.Preferito (id_utente,id_volo) values ('$id','$codice');"

        val insertCall = ClientNetwork.retrofit.insert(insertQuery)
        insertCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()?.get("queryset") as JsonArray

                    if(data.size() < 0){
                        showMessage("Errore durante l'inserimento")
                    }
                } else {
                    showMessage("Risposta dal server vuota")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })

    }

    private fun preferitiAlloggio(id: Int,codice: Int){

        val insertQuery = "INSERT INTO webmobile.Preferito (id_utente,codice_alloggio) values ('$id','$codice');"

        val insertCall = ClientNetwork.retrofit.insert(insertQuery)
        insertCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()?.get("queryset") as JsonArray

                    if(data.size() < 0){
                        showMessage("Errore durante l'inserimento")
                    }
                } else {
                    showMessage("Risposta dal server vuota")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })

    }

    private fun preferitiCrociera(id: Int,codice: Int){

        val insertQuery = "INSERT INTO webmobile.Preferito (id_utente,codice_crociera) values ('$id','$codice');"

        val insertCall = ClientNetwork.retrofit.insert(insertQuery)
        insertCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()?.get("queryset") as JsonArray

                    if(data.size() < 0){
                        showMessage("Errore durante l'inserimento")
                    }
                } else {
                    showMessage("Risposta dal server vuota")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })

    }

    private fun preferitiTaxi(id: Int,codice: Int){

        val insertQuery = "INSERT INTO webmobile.Preferito (id_utente,id_taxi) values ('$id','$codice');"

        val insertCall = ClientNetwork.retrofit.insert(insertQuery)
        insertCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()?.get("queryset") as JsonArray

                    if(data.size() < 0){
                        showMessage("Errore durante l'inserimento")
                    }
                } else {
                    showMessage("Risposta dal server vuota")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })

    }

    private fun preferitiAuto(id: Int,codice: Int){

        val insertQuery = "INSERT INTO webmobile.Preferito (id_utente,id_auto) values ('$id','$codice');"

        val insertCall = ClientNetwork.retrofit.insert(insertQuery)
        insertCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()?.get("queryset") as JsonArray

                    if(data.size() < 0){
                        showMessage("Errore durante l'inserimento")
                    }
                } else {
                    showMessage("Risposta dal server vuota")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })

    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun recensioniSoggiorno(id: Int,codice: Int){
        val query = "SELECT descrizione,stelle,id_utente,codice_alloggio from webmobile.Prenotazione where id_utente = '$id' and codice_alloggio = '$codice';"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val data = responseBody.getAsJsonArray("queryset")

                        if (data.size() > 0) {
                            val utente = data.get(0).asJsonObject

                            val descrizione = utente.get("descrizione").asString
                            val stelle = utente.get("stelle").asFloat

                            val reviewItem = ItemsViewModelReview(descrizione,stelle)

                            val list = mutableListOf<ItemsViewModelReview>()
                            list.add(reviewItem)

                            val adapter = CustomAdapterReview(list)

                            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                //bindingOrizzontale.recensioni.adapter = adapter
                            } else {
                                binding.recensioni.adapter = adapter
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessuna recensione trovata")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero dei dati")
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

    private fun recensioniCrociera(id: Int,codice: Int){
        val query = "SELECT descrizione,stelle,id_utente,codice_crociera from webmobile.Prenotazione where id_utente = '$id' and codice_crociera = '$codice';"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val data = responseBody.getAsJsonArray("queryset")

                        if (data.size() > 0) {
                            val utente = data.get(0).asJsonObject

                            val descrizione = utente.get("descrizione").asString
                            val stelle = utente.get("stelle").asFloat

                            val reviewItem = ItemsViewModelReview(descrizione,stelle)

                            val list = mutableListOf<ItemsViewModelReview>()
                            list.add(reviewItem)

                            val adapter = CustomAdapterReview(list)

                            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                //bindingOrizzontale.recensioni.adapter = adapter
                            } else {
                                binding.recensioni.adapter = adapter
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessuna recensione trovata")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero dei dati")
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

    companion object {
        private const val ARG_DATA = "data"
        fun newDettagliInstance(data: String,id: Int,codice: Int): FragmentDettagli {
            val fragment = FragmentDettagli()
            val bundle = Bundle()
            bundle.putString(ARG_DATA, data)
            bundle.putInt(ARG_DATA,id)
            bundle.putInt(ARG_DATA,codice)
            fragment.arguments = bundle
            return fragment
        }
    }
}
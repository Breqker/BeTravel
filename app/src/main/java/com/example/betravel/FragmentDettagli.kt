package com.example.betravel

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

                val primoElemento = data[0].toInt()
                val secondoElemento = data[1].toString()

                when(secondoElemento){
                    "nome_volo" -> preferitiVolo(id,primoElemento)
                    "codice_alloggio" -> {
                        preferitiAlloggio(id,primoElemento)
                        recensioniSoggiorno()
                    }
                    "id_taxi" -> preferitiTaxi(id,primoElemento)
                    "id_auto" -> preferitiAuto(id,primoElemento)
                    "codice_crociera" -> {
                        preferitiCrociera(id,primoElemento)
                        recensioniCrociera()
                    }
                }
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

                val primoElemento = data[0].toInt()
                val secondoElemento = data[1].toString()

                when(secondoElemento){
                    "nome_volo" -> preferitiVolo(id,primoElemento)
                    "codice_alloggio" -> {
                        preferitiAlloggio(id,primoElemento)
                        recensioniSoggiorno()
                    }
                    "id_taxi" -> preferitiTaxi(id,primoElemento)
                    "id_auto" -> preferitiAuto(id,primoElemento)
                    "codice_crociera" -> {
                        preferitiCrociera(id,primoElemento)
                        recensioniCrociera()
                    }
                }
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

    private fun preferitiVolo(id: Int?,codice: Int){

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

    private fun preferitiAlloggio(id: Int?,codice: Int){

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

    private fun preferitiCrociera(id: Int?,codice: Int){

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

    private fun preferitiTaxi(id: Int?,codice: Int){

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

    private fun preferitiAuto(id: Int?,codice: Int){

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

    private fun recensioniSoggiorno() {
        val query = "SELECT descrizione, stelle, id_utente, codice_alloggio FROM webmobile.Recensione WHERE codice_crociera is NULL';"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val data = responseBody.getAsJsonArray("queryset")

                        if (data.size() > 0) {
                            val recensioniList = ArrayList<ItemsViewModelReview>()

                            for (i in 0 until data.size()) {
                                val recensioneObject = data.get(i).asJsonObject

                                val descrizione = recensioneObject.get("descrizione").asString
                                val stelle = recensioneObject.get("stelle").asFloat

                                val reviewItem = ItemsViewModelReview(descrizione, stelle)
                                recensioniList.add(reviewItem)
                            }

                            recensioniSoggiornoList(recensioniList)
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

    private fun recensioniSoggiornoList(recensioniList: List<ItemsViewModelReview>) {
        val adapter = CustomAdapterReview(recensioniList)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale.recensioni.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            bindingOrizzontale.recensioni.adapter = adapter
        } else {
            binding.recensioni.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            binding.recensioni.adapter = adapter
        }
    }

    private fun recensioniCrociera() {
        val query = "SELECT descrizione, stelle, id_utente, codice_crociera FROM webmobile.Recensione WHERE codice_alloggio is NULL';"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val data = responseBody.getAsJsonArray("queryset")

                        if (data.size() > 0) {
                            val recensioniList = ArrayList<ItemsViewModelReview>()

                            for (i in 0 until data.size()) {
                                val recensioneObject = data.get(i).asJsonObject

                                val descrizione = recensioneObject.get("descrizione").asString
                                val stelle = recensioneObject.get("stelle").asFloat

                                val reviewItem = ItemsViewModelReview(descrizione, stelle)
                                recensioniList.add(reviewItem)
                            }

                            recensioniCrocieraList(recensioniList)
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

    private fun recensioniCrocieraList(recensioniList: List<ItemsViewModelReview>) {
        val adapter = CustomAdapterReview(recensioniList)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale.recensioni.adapter = adapter
        } else {
            binding.recensioni.adapter = adapter
        }
    }


    companion object {
        private const val ARG_DATA = "data"
        fun newDettagliInstance(data: String): FragmentDettagli {
            val fragment = FragmentDettagli()
            val bundle = Bundle()
            bundle.putString(ARG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}
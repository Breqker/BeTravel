package com.example.betravel

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.betravel.databinding.FragmentDettagliBinding
import com.example.betravel.databinding.FragmentDettagliOrizzontaleBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentDettagli : Fragment() {

    private lateinit var binding: FragmentDettagliBinding
    private lateinit var bindingOrizzontale : FragmentDettagliOrizzontaleBinding
    private lateinit var editText: EditText
    private lateinit var ratingBar: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            bindingOrizzontale = FragmentDettagliOrizzontaleBinding.inflate(inflater,container,false)
            val view = bindingOrizzontale.root

            val data = arguments?.getString(ARG_DATA)
            val tipo = arguments?.getString(ARG_TIPO)

            if (data != null) {
                bindingOrizzontale.textViewDettagli.text = data

                if (tipo=="FragmentCrociera"){
                    val id = getIdViaggio(data)
                    // metti preferiti crociera
                    setUpRecyclerViewRecensioniCrociera(id)
                } else if (tipo=="FragmentAlloggio"){
                    val id = getIdViaggio(data)
                    //metti preferiti soggiorni
                    setUpRecyclerViewRecensioniSoggiorni(id)
                }
            }


            bindingOrizzontale.prenota.setOnClickListener {
                var fragment = FragmentPagamento()
                if(tipo!=null && data!=null) {
                    fragment = FragmentPagamento.newPagamentoInstance(data, tipo)
                }
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

            bindingOrizzontale.preferiti.setOnClickListener {
                val idUtente = getIdUtente()
                if (data!=null && tipo!=null && idUtente!=null) {
                    controllaPreferiti(getIdViaggio(data), idUtente) { trovato ->
                        if (trovato) {
                            eliminaDaIPreferiti(getIdViaggio(data), idUtente) // altrimenti toglilo dai preferiti

                        } else {
                            // Il viaggio non è presente nei preferiti dell'utente
                            mettiTraIPreferiti(getIdViaggio(data), idUtente, tipo) // se non è già tra i preferiti allora inseriscilo
                        }
                    }
                }
            }

            return view
        }else{
            binding = FragmentDettagliBinding.inflate(inflater,container,false)
            val view = binding.root

            val data = arguments?.getString(ARG_DATA)
            val tipo = arguments?.getString(ARG_TIPO)

            if (data != null) {
                binding.textViewDettagli.text = getData(data)

                if (tipo=="FragmentCrociera"){
                    val id = getIdViaggio(data)
                    // metti preferiti crociera
                    setUpRecyclerViewRecensioniCrociera(id)
                } else if (tipo=="FragmentAlloggio"){
                    val id = getIdViaggio(data)
                    //metti preferiti soggiorni
                    setUpRecyclerViewRecensioniSoggiorni(id)
                }


            }

            binding.prenota.setOnClickListener {
                var fragment = FragmentPagamento()
                if(tipo!=null && data!=null) {
                    fragment = FragmentPagamento.newPagamentoInstance(data, tipo)
                }
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

            binding.preferiti.setOnClickListener {
                val idUtente = getIdUtente()
                if (data!=null && tipo!=null && idUtente!=null) {
                    controllaPreferiti(getIdViaggio(data), idUtente) { trovato ->
                        if (trovato) {
                            eliminaDaIPreferiti(getIdViaggio(data), idUtente) // altrimenti toglilo dai preferiti
                        } else {
                            // Il viaggio non è presente nei preferiti dell'utente
                            mettiTraIPreferiti(getIdViaggio(data), idUtente, tipo) // se non è già tra i preferiti allora inseriscilo
                        }
                    }
                }
            }

            if (tipo == "FragmentAlloggio" || tipo == "FragmentCrociera") {
            binding.recensione.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val text = s.toString()
                    // Fai qualcosa con il testo
                    // ...
                }
            })


                binding.recensione.setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE || (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // L'utente ha premuto il tasto "Invio" o ha completato l'input con l'evento "KEYCODE_ENTER"
                        val text = binding.recensione.text.toString()
                        val rating = binding.reviewRating.rating
                        if (data!=null){
                            inserisciRecensione(text, rating, Utente.getId(), tipo, getIdViaggio(data))
                        }
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }

                binding.reviewRating.setOnRatingBarChangeListener { _, rating, _ ->
                    // Fai qualcosa con la valutazione
                    // ...
                }
            } else {
                binding.recensione.isVisible = false
                binding.reviewRating.isVisible = false
            }


            return view
        }
    }

    private fun inserisciRecensione(text: String, rating: Float, id: Int?, tipo: String, idViaggio: String) {
        var query = ""

        when (tipo) {
            "FragmentAlloggio" -> query = "insert into Recensione (descrizione, stelle, id_utente, codice_alloggio) values ('$text', '${rating.toInt()}', '$id', '$idViaggio')"
            "FragmentCrociera" -> query = "insert into Recensione (descrizione, stelle, id_utente, codice_crociera) values ('$text', '${rating.toInt()}', '$id', '$idViaggio')"
        }

        val queryCall = ClientNetwork.retrofit.insert(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    showMessage("Recensione inserita")
                } else {
                    showMessage("Errore durante l'inserimento della recensione")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                requireActivity().runOnUiThread {
                    showMessage("Errore di connessione: ${t.message}")
                }
            }
        })
    }

    private fun eliminaDaIPreferiti(idViaggio: String, idUtente: Int) {
        val query = "DELETE FROM Preferito\n" +
                "WHERE id_utente='$idUtente' and id_volo = '$idViaggio'\n" +
                "   OR codice_alloggio = '$idViaggio'\n" +
                "   OR codice_crociera = '$idViaggio'\n" +
                "   OR id_taxi = '$idViaggio'\n" +
                "   OR id_auto = '$idViaggio';"
        val queryCall = ClientNetwork.retrofit.remove(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    showMessage("Elemento rimosso dai preferiti")
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante la rimozione")
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

    private fun controllaPreferiti(idViaggio: String, idUtente: Int, callback: (Boolean) -> Unit) {
        val query = "SELECT *\n" +
                "FROM Preferito\n" +
                "WHERE id_utente='$idUtente' and id_volo = '$idViaggio'\n" +
                "   OR codice_alloggio = '$idViaggio'\n" +
                "   OR codice_crociera = '$idViaggio'\n" +
                "   OR id_taxi = '$idViaggio'\n" +
                "   OR id_auto = '$idViaggio';"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val trovato = responseBody != null
                    callback(trovato)
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero dei dati")
                    }
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                requireActivity().runOnUiThread {
                    showMessage("Errore di connessione: ${t.message}")
                }
                callback(false)
            }
        })
    }



    private fun mettiTraIPreferiti(idViaggio: String, idUtente: Int?, tipo: String) {
        var insertQuery = ""

        when(tipo) {
            "FragmentVolo" -> insertQuery = "INSERT INTO Preferito (id_utente, id_volo) values ('$idUtente','$idViaggio');"
            "FragmentAlloggio" -> insertQuery = "INSERT INTO Preferito (id_utente, codice_alloggio) values ('$idUtente','$idViaggio');"
            "FragmentTaxi" -> insertQuery = "INSERT INTO Preferito (id_utente, id_taxi) values ('$idUtente','$idViaggio');"
            "FragmentAuto" -> insertQuery = "INSERT INTO Preferito (id_utente, id_auto) values ('$idUtente','$idViaggio');"
            "FragmentCrociera" -> insertQuery = "INSERT INTO Preferito (id_utente, codice_crociera) values ('$idUtente','$idViaggio');"
        }
        val insertCall = ClientNetwork.retrofit.insert(insertQuery)
        insertCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    showMessage("Aggiunto ai preferiti")
                } else {
                    showMessage("Risposta dal server vuota")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun getIdUtente(): Int? {
        return Utente.getId()
    }

    private fun getData(data: String): String {
        return data.substringAfter("\n")

    }

    private fun setUpRecyclerViewRecensioniCrociera(id: String) {
        val query = "SELECT descrizione, stelle, id_utente, codice_crociera FROM webmobile.Recensione WHERE codice_alloggio is NULL and codice_crociera='$id';"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val data = responseBody.getAsJsonArray("queryset")

                        if (data.size() > 0) {
                            val recensioniList = ArrayList<ItemsViewModelReview>()
                            val layoutManager = LinearLayoutManager(requireContext())
                            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                bindingOrizzontale.recensioni.layoutManager = layoutManager
                            } else {
                                binding.recensioni.layoutManager = layoutManager
                            }

                            for (i in 0 until data.size()) {
                                val recensioneObject = data.get(i).asJsonObject

                                val descrizione = recensioneObject.get("descrizione").asString
                                val stelle = recensioneObject.get("stelle").asFloat

                                val reviewItem = ItemsViewModelReview(descrizione, stelle)
                                recensioniList.add(reviewItem)
                            }

                            val adapter = CustomAdapterReview(recensioniList)
                            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                bindingOrizzontale.recensioni.adapter = adapter
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

    fun getIdViaggio(soggiornoDetails: String): String {
        return soggiornoDetails.substringBefore("\n")
    }

    private fun setUpRecyclerViewRecensioniSoggiorni(id: String) {

        val query = "SELECT descrizione, stelle, id_utente, codice_alloggio FROM webmobile.Recensione WHERE codice_crociera is NULL and codice_alloggio='$id';"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val data = responseBody.getAsJsonArray("queryset")

                        if (data.size() > 0) {
                            val recensioniList = ArrayList<ItemsViewModelReview>()
                            val layoutManager = LinearLayoutManager(requireContext())
                            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                bindingOrizzontale.recensioni.layoutManager = layoutManager
                            } else {
                                binding.recensioni.layoutManager = layoutManager
                            }

                            for (i in 0 until data.size()) {
                                val recensioneObject = data.get(i).asJsonObject

                                val descrizione = recensioneObject.get("descrizione").asString
                                val stelle = recensioneObject.get("stelle").asFloat

                                val reviewItem = ItemsViewModelReview(descrizione, stelle)
                                recensioniList.add(reviewItem)
                            }

                            val adapter = CustomAdapterReview(recensioniList)
                            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                bindingOrizzontale.recensioni.adapter = adapter
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

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_DATA = "data"
        private const val ARG_TIPO = "tipo"

        fun newDettagliInstance(data: String, fragmentTipo: String): FragmentDettagli {
            val fragment = FragmentDettagli()
            val bundle = Bundle()
            bundle.putString(ARG_DATA, data)
            bundle.putString(ARG_TIPO,fragmentTipo)
            fragment.arguments = bundle
            return fragment
        }
    }
}
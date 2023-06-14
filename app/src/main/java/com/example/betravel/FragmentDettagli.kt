package com.example.betravel

import ReviewsAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.betravel.databinding.FragmentDettagliBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentDettagli : Fragment() {

    private lateinit var binding: FragmentDettagliBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            return super.onCreateView(inflater, container, savedInstanceState)
        }else{
            binding = FragmentDettagliBinding.inflate(inflater,container,false)
            val view = binding.root

            val data = arguments?.getString(ARG_DATA)
            if (data != null) {
                binding.textViewDettagli.text = data
            }

            recensioniSoggiorno()

            binding.preferiti.setOnClickListener {
                preferitiVolo(id)
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

    private fun preferitiVolo(id: Int){
        val insertQuery = "INSERT INTO webmobile.Preferito (id_volo) values ('$id');"

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

    private fun recensioniSoggiorno(){
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recensioni.layoutManager = layoutManager

        val reviewsList = listOf(
            Review(getString(R.string.resortSantaFlaviaAnnalisa),5f),
            Review(getString(R.string.resortSantaFlaviaGiovanni),4f),
            Review(getString(R.string.resortSantaFlaviaGiuseppa),5f),
            Review(getString(R.string.baglioDeiNebrodiAlessandro),3f),
            Review(getString(R.string.baglioDeiNebrodiVittoria),5f),
            Review(getString(R.string.bbGiovanniBiondoFederica),5f),
            Review(getString(R.string.bbGiovanniBiondoBenedetta),5f),
            Review(getString(R.string.costaSmeraldaLuigi),5f),
            Review(getString(R.string.costaSmeraldaVincenzo),4f),
            Review(getString(R.string.costaAzzurraCostanza),3f),
            Review(getString(R.string.costaAzzurraOfelia),5f),
            Review(getString(R.string.costaFavolosaAntonella),5f),
            Review(getString(R.string.costaFavolosaPietro),5f),
            Review(getString(R.string.costaFantasticaJessica),5f),
            Review(getString(R.string.costaFantasticaNoemi),5f),
        )

        val adapter = ReviewsAdapter(reviewsList)
        binding.recensioni.adapter = adapter
    }

    companion object {
        private const val ARG_DATA = "data"
        private const val DATA = "dataArray"
        fun newDettagliInstance(data: String): FragmentDettagli {
            val fragment = FragmentDettagli()
            val bundle = Bundle()
            bundle.putString(ARG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }


        fun newDettagliInstanceSoggiorno(data : String): FragmentDettagli {
            val fragment = FragmentDettagli()
            val bundle = Bundle()
            bundle.putString(ARG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}
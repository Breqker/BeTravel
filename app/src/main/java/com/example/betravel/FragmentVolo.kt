package com.example.betravel

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.betravel.databinding.FragmentVoloBinding
import com.example.betravel.databinding.FragmentVoloLandBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.sql.Time
import java.util.*

class FragmentVolo : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentVoloBinding
    private lateinit var bindingLand: FragmentVoloLandBinding
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingLand = FragmentVoloLandBinding.inflate(inflater, container, false)
            val view = bindingLand.root

            bindingLand.aeroportoPartenza.isFocusable = false
            bindingLand.aeroportoPartenza.isClickable = false

            bindingLand.aeroportoArrivo.isFocusable = false
            bindingLand.aeroportoArrivo.isClickable = false

            val aeroportoPartenzaSpinner: Spinner = bindingLand.aeroportoPartenzaSpinner
            val aeroportoPartenzaAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            aeroportoPartenzaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            aeroportoPartenzaSpinner.adapter = aeroportoPartenzaAdapter

            aeroportoPartenza(aeroportoPartenzaAdapter)

            val aeroportoArrivoSpinner: Spinner = bindingLand.aeroportoArrivoSpinner
            val aeroportoArrivoAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            aeroportoArrivoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            aeroportoArrivoSpinner.adapter = aeroportoArrivoAdapter

            aeroportoArrivo(aeroportoArrivoAdapter)

            bindingLand.dataPartenza.isFocusable = false
            bindingLand.dataPartenza.isClickable = true

            bindingLand.dataPartenza.setOnClickListener {
                showDatePicker(bindingLand.dataPartenza)
            }

            bindingLand.dataRitorno.isFocusable = false
            bindingLand.dataRitorno.isClickable = true

            bindingLand.dataRitorno.setOnClickListener {
                showDatePicker(bindingLand.dataRitorno)
            }

            val numbers = ArrayList<String>()
            for (i in 1..10) {
                numbers.add(i.toString())
            }

            bindingLand.numPersone.isFocusable = false
            bindingLand.numPersone.isClickable = false

            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numbers)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val numPersoneSpinner: Spinner = bindingLand.numPersoneSpinner
            numPersoneSpinner.adapter = adapter

            bindingLand.cerca.setOnClickListener {
                handleConfermaClick()
            }

            return view
        } else {
            binding = FragmentVoloBinding.inflate(inflater,container,false)
            val view = binding.root

            binding.aeroportoPartenza.isFocusable = false
            binding.aeroportoPartenza.isClickable = false

            binding.aeroportoArrivo.isFocusable = false
            binding.aeroportoArrivo.isClickable = false

            val aeroportoPartenzaSpinner: Spinner = binding.aeroportoPartenzaSpinner
            val aeroportoPartenzaAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            aeroportoPartenzaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            aeroportoPartenzaSpinner.adapter = aeroportoPartenzaAdapter

            aeroportoPartenza(aeroportoPartenzaAdapter)

            val aeroportoArrivoSpinner: Spinner = binding.aeroportoArrivoSpinner
            val aeroportoArrivoAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            aeroportoArrivoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            aeroportoArrivoSpinner.adapter = aeroportoArrivoAdapter

            aeroportoArrivo(aeroportoArrivoAdapter)

            binding.dataPartenza.isFocusable = false
            binding.dataPartenza.isClickable = true

            binding.dataPartenza.setOnClickListener {
                showDatePicker(binding.dataPartenza)
            }

            binding.dataRitorno.isFocusable = false
            binding.dataRitorno.isClickable = true

            binding.dataRitorno.setOnClickListener {
                showDatePicker(binding.dataRitorno)
            }

            val numbers = ArrayList<String>()
            for (i in 1..10) {
                numbers.add(i.toString())
            }

            binding.numPersone.isFocusable = false
            binding.numPersone.isClickable = false

            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numbers)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val numPersoneSpinner: Spinner = binding.numPersoneSpinner
            numPersoneSpinner.adapter = adapter

            binding.cerca.setOnClickListener {
                handleConfermaClick()
            }

            return view
        }
    }


    private fun showDatePicker(editText: EditText) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = formatDate(year, monthOfYear, dayOfMonth)
                editText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val formattedMonth = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
        val formattedDay = if (day < 10) "0$day" else "$day"
        return "$formattedDay/$formattedMonth/$year"
    }

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener("risultatiKey", this) { _, bundle ->
        }
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



    private fun showErrorMessage(message: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Errore")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    private fun showMessage(message: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Informazione")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }


    private fun aeroportoPartenza(adapter: ArrayAdapter<String>) {
        val query = "SELECT distinct aeroporto_partenza FROM webmobile.Volo;"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val airports = responseBody.getAsJsonArray("queryset")

                        if (airports.size() > 0) {
                            val airportList = mutableListOf<String>()
                            for (i in 0 until airports.size()) {
                                val airport = airports[i].toString()
                                val aeroporto = airport.substringAfter(":").trim()
                                airportList.add(aeroporto.substring(1, aeroporto.length - 2))
                            }

                            activity?.runOnUiThread {
                                adapter.clear()
                                adapter.addAll(airportList)
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showErrorMessage("Nessun aeroporto di partenza trovato")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showErrorMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showErrorMessage("Errore durante il recupero degli aeroporti di partenza")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                requireActivity().runOnUiThread {
                    showErrorMessage("Errore di connessione: ${t.message}")
                }
            }

        })
    }

    private fun aeroportoArrivo(adapter: ArrayAdapter<String>) {
        val query = "SELECT distinct aeroporto_arrivo FROM webmobile.Volo;"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val airports = responseBody.getAsJsonArray("queryset")

                        if (airports.size() > 0) {
                            val airportList = mutableListOf<String>()
                            for (i in 0 until airports.size()) {
                                val airport = airports[i].toString()
                                val aeroporto = airport.substringAfter(":").trim()
                                airportList.add(aeroporto.substring(1, aeroporto.length - 2))
                            }

                            activity?.runOnUiThread {
                                adapter.clear()
                                adapter.addAll(airportList)
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showErrorMessage("Nessun aeroporto di arrivo trovato")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showErrorMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showErrorMessage("Errore durante il recupero degli aeroporti di arrivo")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                requireActivity().runOnUiThread {
                    showErrorMessage("Errore di connessione: ${t.message}")
                }
            }

        })
    }

    private fun dataPartenza(data: Date,aeroporto : String){
        val query = "SELECT data_partenza from webmobile.Volo where data_partenza = '$data' and aeroporto_partenza = '$aeroporto';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray

                    if (responseBody.size() > 0) {

                    } else {
                        requireActivity().runOnUiThread {
                            showErrorMessage("Nessuna data di partenza trovata")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showErrorMessage("Errore durante il recupero delle date di partenza")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showErrorMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun dataArrivo(data: Date,aeroporto: String){
        val query = "SELECT data_ritorno from webmobile.Volo where data_ritorno = '$data' and aeroporto_arrivo = '$aeroporto';"
        val queryPrezzo = "SELECT IF(data_arrivo IS NULL, costo_biglietto, costo_biglietto * 1.5) AS costo_calcolato FROM webmobile.Volo"
        val callPrezzo = ClientNetwork.retrofit.select(queryPrezzo)
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray
                    val data = response.body()
                    if (responseBody.size() > 0) {
                        if(data == null){
                            callPrezzo.enqueue(object : Callback<JsonObject>{
                                override fun onResponse(
                                    call: Call<JsonObject>,
                                    response: Response<JsonObject>
                                ) {
                                    if(response.isSuccessful){
                                        val responseBody2 = response.body()?.get("queryset") as JsonArray
                                        if(responseBody2.size() > 0){

                                        }else{
                                            requireActivity().runOnUiThread {
                                                showErrorMessage("Nessuna prezzo trovato")
                                            }
                                        }
                                    }else{
                                        requireActivity().runOnUiThread {
                                            showErrorMessage("Errore durante il recupero dei prezzi")
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                    showErrorMessage("Errore di connessione: ${t.message}")
                                }
                            })
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showErrorMessage("Nessuna data di arrivo trovata")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showErrorMessage("Errore durante il recupero delle date di arrivo")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showErrorMessage("Errore di connessione: ${t.message}")
            }
        })
    }



    private fun handleConfermaClick() {
        val aeroportoPartenza: Spinner
        val aeroportoArrivo: Spinner
        val dataPartenza: EditText
        val dataRitorno: EditText
        val numPersone: Spinner

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            aeroportoPartenza = bindingLand.aeroportoPartenzaSpinner
            aeroportoArrivo = bindingLand.aeroportoArrivoSpinner
            dataPartenza = bindingLand.dataPartenza
            dataRitorno = bindingLand.dataRitorno
            //numPersone = bindingLand.numPersoneSpinner
        } else {
            aeroportoPartenza = binding.aeroportoPartenzaSpinner
            aeroportoArrivo = binding.aeroportoArrivoSpinner
            dataPartenza = binding.dataPartenza
            dataRitorno = binding.dataRitorno
            //numPersone = binding.numPersoneSpinner
        }

        val selectedPartenza = aeroportoPartenza.selectedItem.toString()
        val selectedArrivo = aeroportoArrivo.selectedItem.toString()
        //val selectedPersone = numPersone.selectedItem.toString()


        if (selectedPartenza == selectedArrivo) {
            // Gli aeroporti di partenza e ritorno sono uguali, mostra un messaggio di errore
            showErrorMessage("Gli aeroporti di partenza e ritorno devono essere diversi.")
            return
        }

        val partenzaDate = dataPartenza.text.toString()
        val ritornoDate = dataRitorno.text.toString()

        if (partenzaDate.isEmpty()) {
            showErrorMessage("Seleziona una data di partenza.")
            return
        }

        val partenzaSqlDate = convertToSqlDate(partenzaDate)
        dataPartenza(partenzaSqlDate, selectedPartenza)

        if (ritornoDate.isEmpty()) {
            cercaVoliSoloAndata(selectedPartenza, selectedArrivo, partenzaSqlDate)
        } else {
            val ritornoSqlDate = convertToSqlDate(ritornoDate)

            if (ritornoSqlDate < partenzaSqlDate) {
                showErrorMessage("La data di arrivo deve essere o nello stesso giorno o nei giorni successivi alla data di partenza")
                return
            }

            dataArrivo(ritornoSqlDate, selectedArrivo)
            cercaVoli(selectedPartenza, selectedArrivo, partenzaSqlDate, ritornoSqlDate)
        }

    }

    private fun cercaVoliSoloAndata(aeroporto_partenza: String, aeroporto_arrivo: String, data_partenza: Date) {
        val query = "SELECT nome_volo, aeroporto_partenza, aeroporto_arrivo, data_partenza, ora_partenza, ora_arrivo, costo_biglietto from webmobile.Volo where aeroporto_partenza = '$aeroporto_partenza' AND aeroporto_arrivo = '$aeroporto_arrivo' AND data_partenza = '$data_partenza' AND data_ritorno is null;"
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val risultatiVoli = responseBody.getAsJsonArray("queryset")
                        val stringList = ArrayList<String>()
                        for (i in 0 until risultatiVoli.size()) {
                            val volo = risultatiVoli[i].toString()
                            stringList.add(volo)
                        }

                        //val bundle = Bundle()
                        //bundle.putStringArrayList("data", stringList)
                        val fragment = FragmentRisultati.newInstance(stringList)
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragment_container, fragment)
                        transaction.addToBackStack(null) // Aggiungi il fragment al back stack
                        transaction.commit()

                    } else {
                        requireActivity().runOnUiThread {
                            showErrorMessage("Nessun risultato trovato")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showErrorMessage("Errore durante la query al database")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showErrorMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun cercaVoli(aeroporto_partenza: String, aeroporto_arrivo: String, data_partenza: Date, data_ritorno: Date) {
        val query = "SELECT nome_volo, aeroporto_partenza, aeroporto_arrivo, data_partenza, data_ritorno, ora_partenza, ora_arrivo, costo_biglietto from webmobile.Volo where aeroporto_partenza = '$aeroporto_partenza' AND aeroporto_arrivo = '$aeroporto_arrivo' AND data_partenza = '$data_partenza' AND data_ritorno = '$data_ritorno';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val risultatiVoli = responseBody.getAsJsonArray("queryset")
                        val stringList = ArrayList<String>()
                        for (i in 0 until risultatiVoli.size()) {
                            val volo = risultatiVoli[i].toString()
                            stringList.add(volo)
                        }

                        //val bundle = Bundle()
                        //bundle.putStringArrayList("data", stringList)
                        val fragment = FragmentRisultati.newInstance(stringList)
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragment_container, fragment)
                        transaction.addToBackStack(null) // Aggiungi il fragment al back stack
                        transaction.commit()

                    } else {
                        requireActivity().runOnUiThread {
                            showErrorMessage("Nessun risultato trovato")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showErrorMessage("Errore durante la query al database")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showErrorMessage("Errore di connessione: ${t.message}")
            }
        })
    }



    private fun convertToSqlDate(dateString: String): Date {
        val parts = dateString.split("/")
        val day = parts[0].toInt()
        val month = parts[1].toInt() - 1
        val year = parts[2].toInt()
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return Date(calendar.timeInMillis)
    }


}
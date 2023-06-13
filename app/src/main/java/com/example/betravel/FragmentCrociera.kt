package com.example.betravel

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.FragmentCrocieraBinding
import com.example.betravel.databinding.FragmentCrocieraLandBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.util.ArrayList
import java.util.Calendar

class FragmentCrociera: Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentCrocieraBinding
    private lateinit var bindingLand: FragmentCrocieraLandBinding
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingLand = FragmentCrocieraLandBinding.inflate(inflater, container, false)
            val view = bindingLand.root

            bindingLand.cittaPartenza.isFocusable = false
            bindingLand.cittaPartenza.isClickable = false

            val cittaPartenzaSpinner: Spinner = bindingLand.cittaPartenzaSpinner
            val cittaPartenzaAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            cittaPartenzaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cittaPartenzaSpinner.adapter = cittaPartenzaAdapter

            citta(cittaPartenzaAdapter)

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

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numbers)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val numPersoneSpinner: Spinner = bindingLand.numPersoneSpinner
            numPersoneSpinner.adapter = adapter


            bindingLand.cerca.setOnClickListener {
                handleConfermaClick()
            }

            return view
        }else{
            binding = FragmentCrocieraBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.cittaPartenza.isFocusable = false
            binding.cittaPartenza.isClickable = false

            val cittaPartenzaSpinner: Spinner = binding.cittaPartenzaSpinner
            val cittaPartenzaAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            cittaPartenzaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cittaPartenzaSpinner.adapter = cittaPartenzaAdapter

            citta(cittaPartenzaAdapter)

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
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,FragmentRisultati())
                    .commit()
            }

            return view
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Verifica se ci sono fragment nello stack di backstack
                if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    isEnabled = false // Disabilita il callback
                    requireActivity().onBackPressed() // Esegui il comportamento di default del tasto indietro
                }
            }
        }

    }

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
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

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun citta(adapter: ArrayAdapter<String>) {
        val query = "SELECT distinct citta_partenza FROM webmobile.Crociera;"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val cities = responseBody.getAsJsonArray("queryset")

                        if (cities.size() > 0) {
                            val cityList = mutableListOf<String>()
                            for (i in 0 until cities.size()) {
                                val city = cities[i].toString()
                                val citta = city.substringAfter(":").trim()
                                cityList.add(citta.substring(1, citta.length - 2))
                            }

                            activity?.runOnUiThread {
                                adapter.clear()
                                adapter.addAll(cityList)
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessuna città trovata")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero delle città")
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

    private fun dataPartenza(data: Date){
        val query = "SELECT data_partenza from webmobile.Crociera where data_partenza = '$data';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray

                    if (responseBody.size() > 0) {

                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Nessuna data di partenza trovata")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero delle date di partenza")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun dataRitorno(data: Date){
        val query = "SELECT data_ritorno from webmobile.Crociera where data_ritorno = '$data';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray
                    if (responseBody.size() > 0) {

                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Nessuna data di ritorno trovata")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero delle date di ritorno")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun handleConfermaClick() {
        val dataPartenza: EditText
        val dataRitorno: EditText

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dataPartenza = bindingLand.dataPartenza
            dataRitorno = bindingLand.dataRitorno
        } else {
            dataPartenza = binding.dataPartenza
            dataRitorno = binding.dataRitorno
        }


        val partenzaDate = dataPartenza.text.toString()
        val ritornoDate = dataRitorno.text.toString()

        if (partenzaDate.isEmpty() || ritornoDate.isEmpty()) {
            showMessage("Seleziona una data di partenza e di ritorno.")
            return
        }

        if(ritornoDate < partenzaDate){
            showMessage("La data di ritorno deve essere o nello stesso giorno o nei giorni successivi alla data di inizio")
            return
        }

        val partenzaSqlDate = convertToSqlDate(partenzaDate)
        val ritornoSqlDate = convertToSqlDate(ritornoDate)

        dataPartenza(partenzaSqlDate)
        dataRitorno(ritornoSqlDate)

        // Continua con il resto del codice per gestire la conferma del volo
        // ...
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
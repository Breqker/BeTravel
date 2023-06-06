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
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.FragmentVoloBinding
import com.example.betravel.databinding.FragmentVoloLandBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
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
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,FragmentRisultati())
                    .commit()
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
        val query = "SELECT data_arrivo from webmobile.Volo where data_arrivo = '$data' and aeroporto_ritorno = '$aeroporto';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray

                    if (responseBody.size() > 0) {

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

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            aeroportoPartenza = bindingLand.aeroportoPartenzaSpinner
            aeroportoArrivo = bindingLand.aeroportoArrivoSpinner
            dataPartenza = bindingLand.dataPartenza
            dataRitorno = bindingLand.dataRitorno
        } else {
            aeroportoPartenza = binding.aeroportoPartenzaSpinner
            aeroportoArrivo = binding.aeroportoArrivoSpinner
            dataPartenza = binding.dataPartenza
            dataRitorno = binding.dataRitorno
        }

        val selectedPartenza = aeroportoPartenza.selectedItem.toString()
        val selectedArrivo = aeroportoArrivo.selectedItem.toString()

        if (selectedPartenza == selectedArrivo) {
            // Gli aeroporti di partenza e ritorno sono uguali, mostra un messaggio di errore
            showErrorMessage("Gli aeroporti di partenza e ritorno devono essere diversi.")
            return
        }

        val partenzaDate = dataPartenza.text.toString()
        val ritornoDate = dataRitorno.text.toString()

        if (partenzaDate.isEmpty() || ritornoDate.isEmpty()) {
            showErrorMessage("Seleziona una data di partenza e di arrivo.")
            return
        }

        if(ritornoDate < partenzaDate){
            showErrorMessage("La data di arrivo deve essere o nello stesso giorno o nei giorni successivi alla data di partenza")
            return
        }

        val partenzaSqlDate = convertToSqlDate(partenzaDate)
        val arrivoSqlDate = convertToSqlDate(ritornoDate)

        dataPartenza(partenzaSqlDate, selectedPartenza)
        dataArrivo(arrivoSqlDate, selectedArrivo)

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
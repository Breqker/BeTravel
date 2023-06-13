package com.example.betravel

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.example.betravel.databinding.FragmentTaxiBinding
import com.example.betravel.databinding.FragmentTaxiLandBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.sql.Time
import java.util.Calendar

class FragmentTaxi : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentTaxiBinding
    private lateinit var bindingLand: FragmentTaxiLandBinding
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingLand = FragmentTaxiLandBinding.inflate(inflater, container, false)
            val view = bindingLand.root

            bindingLand.citta.isFocusable = false
            bindingLand.citta.isClickable = false

            val cittaSpinner: Spinner = bindingLand.cittaSpinner
            val cittaAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            cittaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cittaSpinner.adapter = cittaAdapter

            citta(cittaAdapter)

            bindingLand.data.isFocusable = false
            bindingLand.data.isClickable = true

            bindingLand.data.setOnClickListener {
                showDatePicker(bindingLand.data)
            }

            bindingLand.orario.isFocusable = false
            bindingLand.orario.isClickable = true

            bindingLand.orario.setOnClickListener {
                showTimePicker(bindingLand.orario)
            }

            bindingLand.cerca.setOnClickListener {
                handleConfermaClick()
            }

            return view
        } else {
            binding = FragmentTaxiBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.citta.isFocusable = false
            binding.citta.isClickable = false

            val cittaSpinner: Spinner = binding.cittaSpinner
            val cittaAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            cittaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cittaSpinner.adapter = cittaAdapter

            citta(cittaAdapter)

            binding.data.isFocusable = false
            binding.data.isClickable = true

            binding.data.setOnClickListener {
                showDatePicker(binding.data)
            }

            binding.orario.isFocusable = false
            binding.orario.isClickable = true

            binding.orario.setOnClickListener {
                showTimePicker(binding.orario)
            }

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
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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

    private fun showTimePicker(editText: EditText) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                editText.setText(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

        timePickerDialog.show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun citta(adapter: ArrayAdapter<String>) {
        val query = "SELECT distinct citta FROM webmobile.Taxi;"

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

    private fun dataPrenotazione(data: Date) {
        val query =
            "SELECT data_prenotazione from webmobile.Taxi where data_prenotazione = '$data';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray

                    if (responseBody.size() > 0) {

                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Nessuna data trovata")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero delle date")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }

        })
    }

    private fun orarioPrenotazione(orario: Time) {
        val query =
            "SELECT orario_prenotazione from webmobile.Taxi where orario_prenotazione = '$orario';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray

                    if (responseBody.size() > 0) {

                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Nessun orario trovato")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero degli orari")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }

        })
    }

    private fun handleConfermaClick() {
        val data: EditText
        val orario: EditText

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            data = bindingLand.data
            orario = bindingLand.orario
        } else {
            data = binding.data
            orario = binding.orario
        }


        val dataDate = data.text.toString()
        val orarioTime = orario.text.toString()

        if (dataDate.isEmpty()) {
            showMessage("Seleziona una data.")
            return
        }

        if (orarioTime.isEmpty()){
            showMessage("Seleziona un'orario.")
            return
        }

        val dataSqlDate = convertToSqlDate(dataDate)
        val orarioSqlTime = convertToSqlTime(orarioTime)

        dataPrenotazione(dataSqlDate)
        orarioPrenotazione(orarioSqlTime)

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

    private fun convertToSqlTime(timeString: String): Time {
        val parts = timeString.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()
        val seconds = 0
        return Time(hour, minute, seconds)
    }


}
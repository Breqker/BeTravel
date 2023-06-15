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
import com.example.betravel.databinding.FragmentSoggiornoBinding
import com.example.betravel.databinding.FragmentSoggiornoLandBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.util.ArrayList
import java.util.Calendar

class FragmentSoggiorno: Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentSoggiornoBinding
    private lateinit var bindingLand: FragmentSoggiornoLandBinding
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingLand = FragmentSoggiornoLandBinding.inflate(inflater, container, false)
            val view = bindingLand.root

            bindingLand.citta.isFocusable = false
            bindingLand.citta.isClickable = false

            val cittaSpinner: Spinner = bindingLand.cittaSpinner
            val cittaAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            cittaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cittaSpinner.adapter = cittaAdapter

            citta(cittaAdapter)

            bindingLand.dataInizio.isFocusable = false
            bindingLand.dataInizio.isClickable = true

            bindingLand.dataInizio.setOnClickListener {
                showDatePicker(bindingLand.dataInizio)
            }

            bindingLand.dataRilascio.isFocusable = false
            bindingLand.dataRilascio.isClickable = true

            bindingLand.dataRilascio.setOnClickListener {
                showDatePicker(bindingLand.dataRilascio)
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
            binding = FragmentSoggiornoBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.citta.isFocusable = false
            binding.citta.isClickable = false

            val cittaSpinner: Spinner = binding.cittaSpinner
            val cittaAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
            cittaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cittaSpinner.adapter = cittaAdapter

            citta(cittaAdapter)

            binding.dataInizio.isFocusable = false
            binding.dataInizio.isClickable = true

            binding.dataInizio.setOnClickListener {
                showDatePicker(binding.dataInizio)
            }

            binding.dataRilascio.isFocusable = false
            binding.dataRilascio.isClickable = true

            binding.dataRilascio.setOnClickListener {
                showDatePicker(binding.dataRilascio)
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


    private fun citta(adapter: ArrayAdapter<String>) {
        val query = "SELECT distinct citta FROM webmobile.Soggiorno;"

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

    private fun dataInizio(data: Date){
        val query = "SELECT data_inizio_disponibilita from webmobile.Soggiorno where data_inizio_disponibilita = '$data';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray

                    if (responseBody.size() > 0) {

                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Nessuna data di inizio trovata")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero delle date di inizio")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun dataRilascio(data: Date){
        val query = "SELECT data_fine_disponibilita from webmobile.Soggiorno where data_fine_disponibilita = '$data';"

        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray
                    if (responseBody.size() > 0) {

                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Nessuna data di rilascio trovata")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero delle date di rilascio")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun handleConfermaClick() {
        val dataInizio: EditText
        val dataRilascio: EditText
        val citta: Spinner
        val numeroOspiti: Spinner

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dataInizio = bindingLand.dataInizio
            dataRilascio = bindingLand.dataRilascio
            citta = bindingLand.cittaSpinner
            numeroOspiti = bindingLand.numPersoneSpinner
        } else {
            dataInizio = binding.dataInizio
            dataRilascio = binding.dataRilascio
            citta = binding.cittaSpinner
            numeroOspiti = binding.numPersoneSpinner
        }

        val inizioDate = dataInizio.text.toString()
        val rilascioDate = dataRilascio.text.toString()


        if (inizioDate.isEmpty() || rilascioDate.isEmpty()) {
            showMessage("Seleziona una data di inizio e/o di arrivo")
            return
        }

        if(rilascioDate < inizioDate){
            showMessage("La data di ritorno deve essere o nello stesso giorno o nei giorni successivi alla data di inizio")
            return
        }

        val inizioSqlDate = convertToSqlDate(inizioDate)
        val rilascioSqlDate = convertToSqlDate(rilascioDate)

        //dataInizio(inizioSqlDate)
        //dataRilascio(rilascioSqlDate)

        cercaSoggiorni(citta.selectedItem.toString(), inizioSqlDate, rilascioSqlDate, numeroOspiti.selectedItem.toString())
    }

    private fun cercaSoggiorni(citta: String, inizioDate: Date, rilascioDate: Date, numeroOspiti: String) {
        val query = "SELECT nome_alloggio, citta, data_inizio_disponibilita, data_fine_disponibilita, costo_giornaliero, num_ospiti\n" +
                "FROM Alloggio\n" +
                "WHERE data_inizio_disponibilita <= '$inizioDate'\n" +
                "  AND data_fine_disponibilita >= '$rilascioDate'\n" +
                "  AND citta = '$citta'\n" +
                "  AND num_ospiti >= '$numeroOspiti'\n" +
                "  AND codice_alloggio NOT IN (\n" +
                "    SELECT codice_alloggio\n" +
                "    FROM Prenotazione\n" +
                "    WHERE \n" +
                "      (data_inizio_prenotazione <= '$rilascioDate'\n" +
                "      AND data_fine_prenotazione >= '$inizioDate')\n" +
                "      OR\n" +
                "      (data_inizio_prenotazione >= '$inizioDate'\n" +
                "      AND data_fine_prenotazione <= '$rilascioDate')\n" +
                "  );\n"
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val risultatiSoggiorni = responseBody.getAsJsonArray("queryset")
                        val stringList = ArrayList<String>()
                        for (i in 0 until risultatiSoggiorni.size()) {
                            val soggiorno = risultatiSoggiorni[i].toString()
                            stringList.add(soggiorno)
                        }

                        val fragment = FragmentRisultati.newInstanceSoggiorno(stringList)
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragment_container, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()

                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Nessun risultato trovato")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante la query al database")
                    }
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
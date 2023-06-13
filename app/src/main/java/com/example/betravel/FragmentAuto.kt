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
import com.example.betravel.databinding.FragmentAutoBinding
import com.example.betravel.databinding.FragmentAutoLandBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.util.ArrayList
import java.util.Calendar

class FragmentAuto: Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentAutoBinding
    private lateinit var bindingLand: FragmentAutoLandBinding
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingLand = FragmentAutoLandBinding.inflate(inflater, container, false)
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
            for (i in 18..99) {
                numbers.add(i.toString())
            }

            bindingLand.etaConducente.isFocusable = false
            bindingLand.etaConducente.isClickable = false

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numbers)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val numPersoneSpinner: Spinner = bindingLand.etaConducenteSpinner
            numPersoneSpinner.adapter = adapter


            bindingLand.cerca.setOnClickListener {
                handleConfermaClick()
            }

            return view
        }else{
            binding = FragmentAutoBinding.inflate(inflater, container, false)
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
            for (i in 18..99) {
                numbers.add(i.toString())
            }

            binding.etaConducente.isFocusable = false
            binding.etaConducente.isClickable = false

            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numbers)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val numPersoneSpinner: Spinner = binding.etaConducenteSpinner
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
                if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
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
        val query = "SELECT distinct citta FROM webmobile.Auto;"

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
        val query = "SELECT data_inizio from webmobile.Auto where data_inizio = '$data';"

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
        val query = "SELECT data_rilascio from webmobile.Auto where data_rilascio = '$data';"

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

    private fun etaConducente(eta: Int){
        val query = "SELECT CASE WHEN eta_conducente < 26 THEN prezzo_giornaliero + (prezzo_giornaliero * 0.1) ELSE prezzo_giornaliero END AS nuovo_prezzo_giornaliero FROM webmobile.Auto WHERE eta_conducente = '$eta';"
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful) {
                    val responseBody = response.body()?.get("queryset") as JsonArray
                    if (responseBody.size() > 0) {

                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Nessuna costo giornaliero trovato")
                        }
                    }
                }else{
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero del costo giornaliero")
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

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dataInizio = bindingLand.dataInizio
            dataRilascio = bindingLand.dataRilascio
            citta = bindingLand.cittaSpinner
        } else {
            citta = binding.cittaSpinner
            dataInizio = binding.dataInizio
            dataRilascio = binding.dataRilascio
        }


        val inizioDate = dataInizio.text.toString()
        val rilascioDate = dataRilascio.text.toString()

        if (inizioDate.isEmpty() || rilascioDate.isEmpty()) {
            showMessage("Seleziona una data di inizio e/o di arrivo.")
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

        cercaAuto(citta.selectedItem.toString(), inizioSqlDate, rilascioSqlDate)
    }

    private fun cercaAuto(citta: String, inizioSqlDate: Date, rilascioSqlDate: Date) {
        val query = "SELECT nome_auto, citta, data_inizio_disponibilita, data_fine_disponibilita, prezzo_giornaliero\n" +
                "FROM Auto\n" +
                "WHERE citta = '$citta' AND data_inizio_disponibilita = '$inizioSqlDate' AND data_fine_disponibilita = '$rilascioSqlDate';\n"
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val risultatiAuto = responseBody.getAsJsonArray("queryset")
                        val stringList = ArrayList<String>()
                        for (i in 0 until risultatiAuto.size()) {
                            val auto = risultatiAuto[i].toString()
                            stringList.add(auto)
                        }
                        val fragment = FragmentRisultati.newInstance(stringList)
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragmentContainerView, fragment)
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
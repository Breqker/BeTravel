package com.example.betravel

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import java.util.*

class FragmentVolo : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentVoloBinding
    private lateinit var bindingLand: FragmentVoloLandBinding
    private val calendar = Calendar.getInstance()

    private lateinit var airportAdapter: ArrayAdapter<String>
    private lateinit var airportAdapter2: ArrayAdapter<String>
    private var listaPartenza: List<String> = emptyList()
    private var listaArrivo: List<String> = emptyList()
    private var selectedAirport: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingLand = FragmentVoloLandBinding.inflate(inflater, container, false)
            val view = bindingLand.root

            initAirportAdapter()
            aeroportoPartenza("")
            bindingLand.aeroportoPartenza.setAdapter(airportAdapter)

            bindingLand.aeroportoPartenza.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val searchText = s.toString()
                    val filteredList = listaPartenza.filter { it.contains(searchText, ignoreCase = true) }
                    airportAdapter.clear()
                    airportAdapter.addAll(filteredList)
                    airportAdapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            bindingLand.aeroportoArrivo.setOnItemClickListener { _, _, position, _ ->
                selectedAirport = airportAdapter.getItem(position)
            }

            bindingLand.dataPartenza.isFocusable = false
            bindingLand.dataPartenza.isClickable = true

            bindingLand.dataPartenza.setOnClickListener {
                showDatePicker(bindingLand.dataPartenza)
            }

            bindingLand.dataArrivo.isFocusable = false
            bindingLand.dataArrivo.isClickable = true

            bindingLand.dataArrivo.setOnClickListener {
                showDatePicker(bindingLand.dataArrivo)
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

            return view
        } else {
            binding = FragmentVoloBinding.inflate(inflater, container, false)
            val view = binding.root

            initAirportAdapter()
            aeroportoPartenza("")
            binding.aeroportoPartenza.setAdapter(airportAdapter)

            binding.aeroportoPartenza.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val searchText = s.toString()
                    val filteredList = listaPartenza.filter { it.contains(searchText, ignoreCase = true) }
                    airportAdapter.clear()
                    airportAdapter.addAll(filteredList)
                    airportAdapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            binding.aeroportoPartenza.setOnItemClickListener { _, _, position, _ ->
                selectedAirport = airportAdapter.getItem(position)
            }

            initAirportAdapter()
            aeroportoArrivo("")
            binding.aeroportoArrivo.setAdapter(airportAdapter)

            binding.aeroportoArrivo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val searchText = s.toString()
                    val filteredList = listaArrivo.filter { it.contains(searchText, ignoreCase = true) }
                    airportAdapter.clear()
                    airportAdapter.addAll(filteredList)
                    airportAdapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            binding.aeroportoArrivo.setOnItemClickListener { _, _, position, _ ->
                selectedAirport = airportAdapter.getItem(position)
            }

            binding.dataPartenza.isFocusable = false
            binding.dataPartenza.isClickable = true

            binding.dataPartenza.setOnClickListener {
                showDatePicker(binding.dataPartenza)
            }

            binding.dataArrivo.isFocusable = false
            binding.dataArrivo.isClickable = true

            binding.dataArrivo.setOnClickListener {
                showDatePicker(binding.dataArrivo)
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

    private fun initAirportAdapter() {
        airportAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
    }

    private fun initAirportAdapter2() {
        airportAdapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
    }

    private fun aeroportoPartenza(aeroportoPartenza: String) {
        val query = "select aeroporto_partenza from webmobile.Volo where aeroporto_partenza = '$aeroportoPartenza';"

        val call = ClientNetwork.retrofit.select(query)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    val airportsArray = data?.getAsJsonArray("aeroporto_partenza")
                    if (airportsArray != null && airportsArray.size() > 0) {
                        val aeroporti = ArrayList<String>()
                        for (i in 0 until airportsArray.size()) {
                            val aeroport = airportsArray[i].asString
                            aeroporti.add(aeroport)
                        }
                        listaPartenza = aeroporti
                        airportAdapter.clear()
                        airportAdapter.addAll(aeroporti)
                        airportAdapter.notifyDataSetChanged()

                        if (aeroporti.isEmpty()) {
                            showErrorMessage("Aeroporto non presente")
                        }
                    }
                } else {
                    showErrorMessage("Errore durante la scelta dell'aeroporto")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showErrorMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun aeroportoArrivo(aeroportoArrivo: String) {
        val query = "select aeroporto_arrivo from webmobile.Volo where aeroporto_arrivo = '$aeroportoArrivo';"

        val call = ClientNetwork.retrofit.select(query)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    val airportsArray = data?.getAsJsonArray("aeroporto_arrivo")
                    if (airportsArray != null && airportsArray.size() > 0) {
                        val aeroporti = ArrayList<String>()
                        for (i in 0 until airportsArray.size()) {
                            val aeroport = airportsArray[i].asString
                            aeroporti.add(aeroport)
                        }
                        listaArrivo = aeroporti
                        airportAdapter2.clear()
                        airportAdapter2.addAll(aeroporti)
                        airportAdapter2.notifyDataSetChanged()

                        if (aeroporti.isEmpty()) {
                            showErrorMessage("Non sono presenti aeroporti")
                        }
                    }
                } else {
                    showErrorMessage("Errore durante la scelta dell'aeroporto")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showErrorMessage("Errore di connessione: ${t.message}")
            }
        })
    }
    private fun isAirportSelected(): Boolean {
        return selectedAirport != null && selectedAirport!!.isNotBlank()
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
}

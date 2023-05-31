package com.example.betravel

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.FragmentSoggiornoBinding
import com.example.betravel.databinding.FragmentSoggiornoLandBinding
import java.util.ArrayList
import java.util.Calendar

class FragmentSoggiorno: Fragment() {

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

            bindingLand.dataPartenza.isFocusable = false
            bindingLand.dataPartenza.isClickable = true

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

            bindingLand.barraRicerca.isFocusable = false
            bindingLand.barraRicerca.isClickable = true

            bindingLand.barraRicerca.setOnClickListener {

            }

            return view
        }else{
            binding = FragmentSoggiornoBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.dataPartenza.isFocusable = false
            binding.dataPartenza.isClickable = true

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

            binding.barraRicerca.isFocusable = false
            binding.barraRicerca.isClickable = true

            binding.barraRicerca.setOnClickListener {

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

}
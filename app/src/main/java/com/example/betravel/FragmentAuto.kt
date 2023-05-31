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
import com.example.betravel.databinding.FragmentAutoBinding
import com.example.betravel.databinding.FragmentAutoLandBinding
import java.util.ArrayList
import java.util.Calendar

class FragmentAuto: Fragment() {

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
            for (i in 18..99) {
                numbers.add(i.toString())
            }

            bindingLand.etaConducente.isFocusable = false
            bindingLand.etaConducente.isClickable = false

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numbers)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val numPersoneSpinner: Spinner = bindingLand.etaConducenteSpinner
            numPersoneSpinner.adapter = adapter

            bindingLand.barraRicerca.isFocusable = false
            bindingLand.barraRicerca.isClickable = true

            bindingLand.barraRicerca.setOnClickListener {

            }

            return view
        }else{
            binding = FragmentAutoBinding.inflate(inflater, container, false)
            val view = binding.root

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
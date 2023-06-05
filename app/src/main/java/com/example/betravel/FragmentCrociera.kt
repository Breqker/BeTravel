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
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.FragmentCrocieraBinding
import com.example.betravel.databinding.FragmentCrocieraLandBinding
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


            bindingLand.cerca.setOnClickListener {

            }

            return view
        }else{
            binding = FragmentCrocieraBinding.inflate(inflater, container, false)
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

}
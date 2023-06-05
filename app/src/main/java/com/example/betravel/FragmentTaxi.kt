package com.example.betravel

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.FragmentTaxiBinding
import com.example.betravel.databinding.FragmentTaxiLandBinding
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

            }

            return view
        }else{
            binding = FragmentTaxiBinding.inflate(inflater,container,false)
            val view = binding.root

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

}
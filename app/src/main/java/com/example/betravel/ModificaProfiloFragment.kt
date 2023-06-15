package com.example.betravel

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.example.betravel.databinding.FragmentModificaProfiloBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ModificaProfiloFragment : Fragment(){

    private lateinit var binding: FragmentModificaProfiloBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            /*gestire il binding orizzontale*/
            return super.onCreateView(inflater, container, savedInstanceState)
        }else{
            binding = FragmentModificaProfiloBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.saveButton.setOnClickListener {
                var nome = binding.editName.text.toString()
                var cognome = binding.editCognome.text.toString()
                var email = binding.editEmail.text.toString()
                var password = binding.editPassword.text.toString()

                modificaDati(nome,cognome, email, password)
            }

            return view
        }

    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun showMessageConferma(message: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Attenzione")
            .setMessage(message)
            .setPositiveButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .setNegativeButton("Si") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    private fun modificaDati(nome: String,cognome: String,email: String,password: String){
        val id = Utente.getId()
        val updateFields = mutableListOf<String>()

        if (!nome.isEmpty()) {
            updateFields.add("nome = '$nome'")
        }
        if (!cognome.isEmpty()) {
            updateFields.add("cognome = '$cognome'")
        }
        if (!email.isEmpty()) {
            updateFields.add("email = '$email'")
        }
        if (!password.isEmpty()) {
            updateFields.add("password = '$password'")
        }


        val updateQuery = "UPDATE Utente SET ${updateFields.joinToString(", ")} WHERE id = '$id';"

        val updateCall = ClientNetwork.retrofit.update(updateQuery)
        updateCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    showMessage("Modifica avvenuta con successo")
                } else {
                    showMessage("Risposta dal server non riuscita")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}
package com.example.betravel

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
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
                val nome = binding.editName.text.toString()
                val cognome = binding.editCognome.text.toString()
                val email = binding.editEmail.text.toString()
                val password = binding.editPassword.text.toString()

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

        val updateQuery = "UPDATE Utente\n" +
                "SET \n" +
                "    nome = COALESCE('$nome', nome),\n" +
                "    cognome = COALESCE('$cognome', cognome),\n" +
                "    email = COALESCE('$email', email),\n" +
                "    password = COALESCE('$password', password)\n" +
                "WHERE id = '$id'; "

        val updateCall = ClientNetwork.retrofit.update(updateQuery)
        updateCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val data = response.body()?.get("queryset") as JsonArray

                    if(data.size() > 0){
                        showMessageConferma("Sei sicuro delle modifiche effettuate ?")
                        showMessage("Modifica avvenuta con successo")
                    }else{
                        showMessage("Errore durante la modifica")
                    }
                } else {
                    showMessage("Risposta dal server vuota")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }
}
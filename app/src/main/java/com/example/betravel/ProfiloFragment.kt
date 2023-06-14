package com.example.betravel

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AlertDialog
import com.example.betravel.databinding.FragmentProfiloBinding
import com.example.betravel.databinding.FragmentProfiloOrizzontaleBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfiloFragment : Fragment(), OnBackPressedDispatcherOwner {

    private lateinit var binding: FragmentProfiloBinding
    private lateinit var bindingOrizzontale: FragmentProfiloOrizzontaleBinding
    private var currentFragment: Fragment? = null
    companion object{
        private const val idUtente = "id"
        fun newInstance(id: Int): ProfiloFragment {
            val fragment = ProfiloFragment()
            val bundle = Bundle()
            bundle.putInt(idUtente, id)
            fragment.arguments = bundle
            return fragment
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
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val id = Utente.getId()

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale = FragmentProfiloOrizzontaleBinding.inflate(inflater, container, false)
            val view = bindingOrizzontale.root

            datiProfilo(id)


            bindingOrizzontale.editButton.setOnClickListener {
                val fragmentProfilo = ModificaProfiloFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container_orizzontale, fragmentProfilo)
                    .addToBackStack(null)
                    .commit()
                currentFragment = fragmentProfilo
            }

            bindingOrizzontale.logoutButton.setOnClickListener {
                showMessaggeLogout("Sei sicuro di voler effettuare il logout?")
            }

            return view
        } else {
            binding = FragmentProfiloBinding.inflate(inflater, container, false)
            val view = binding.root

            datiProfilo(id)

            binding.editButton.setOnClickListener {
                val fragmentProfilo = ModificaProfiloFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragmentProfilo)
                    .addToBackStack(null)
                    .commit()
                currentFragment = fragmentProfilo
            }

            binding.logoutButton.setOnClickListener {
                showMessaggeLogout("Sei sicuro di voler effettuare il logout?")
            }

            return view
        }
    }

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
    }

    private fun datiProfilo(id: Int?){
        val query = "SELECT nome, cognome, email, password FROM webmobile.Utente WHERE id = '$id';"

        val queryCall = ClientNetwork.retrofit.select(query)
        queryCall.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val data = responseBody.getAsJsonArray("queryset")

                        if (data.size() > 0) {
                            val utente = data.get(0).asJsonObject

                            val nomeUtente = utente.get("nome").asString
                            val cognomeUtente = utente.get("cognome").asString
                            val emailUtente = utente.get("email").asString
                            val passwordUtente = utente.get("password").asString

                            requireActivity().runOnUiThread {
                                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                    bindingOrizzontale.profileName.text = nomeUtente
                                    bindingOrizzontale.profileCognome.text = cognomeUtente
                                    bindingOrizzontale.profileEmail.text = emailUtente
                                    bindingOrizzontale.profilePassword.text = passwordUtente
                                } else {
                                    binding.profileName.text = nomeUtente
                                    binding.profileCognome.text = cognomeUtente
                                    binding.profileEmail.text = emailUtente
                                    binding.profilePassword.text = passwordUtente
                                }
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                showMessage("Nessun profilo trovato")
                            }
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            showMessage("Risposta del server vuota")
                        }
                    }
                } else {
                    requireActivity().runOnUiThread {
                        showMessage("Errore durante il recupero dei dati")
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

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showMessaggeLogout(message: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Attenzione")
            .setMessage(message)
            .setPositiveButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .setNegativeButton("Si") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                logout()
            }
            .create()
        alertDialog.show()
    }


    private fun logout(){
        val intent = Intent(requireContext(),Login::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}
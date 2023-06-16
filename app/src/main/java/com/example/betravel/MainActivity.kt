package com.example.betravel

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.ActivityMainBinding
import com.example.betravel.databinding.ActivityMainOrizzontaleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding
        private lateinit var bindingOrizzontale: ActivityMainOrizzontaleBinding
        private lateinit var bottomNavigationView: BottomNavigationView
        private var currentFragment: Fragment? = null
        companion object {
            const val EXTRA_ID_UTENTE = "id_utente"
            fun createIntent(context: Context, idUtente: Int): Intent {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra(EXTRA_ID_UTENTE, idUtente)
                return intent
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMainBinding.inflate(layoutInflater)
            bindingOrizzontale = ActivityMainOrizzontaleBinding.inflate(layoutInflater)

            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setContentView(bindingOrizzontale.root)
                setupHorizontalRecyclerView1()
                setupHorizontalRecyclerView2()
                setupBottomNavigationLand()
                setupEditText()
            } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                setContentView(binding.root)
                setupRecyclerView1()
                setupRecyclerView2()
                setupBottomNavigation()
                setupEditText()


            }
        }

    private fun setupEditText() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale.editText.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    val inputText = bindingOrizzontale.editText.text.toString()
                    val categoriaViaggio = getCategorieDiViaggio(inputText)
                    val località = getLocalità(inputText)

                    if (categoriaViaggio=="Volo") {
                        cercaVoliBarraRicerca(località)
                    } else if(categoriaViaggio=="Crociera") {
                        cercaCrociereBarraRicerca(località)
                    } else if(categoriaViaggio=="Taxi") {
                        cercaTaxiBarraRicerca(località)
                    } else if(categoriaViaggio=="Auto") {
                        cercaAutoBarraRicerca(località)
                    } else {
                        cercaAlloggioBarraRicerca(località)
                    }
                    true
                } else {
                    false
                }
            }
        }else{
            binding.editText.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    val inputText = binding.editText.text.toString()
                    val categoriaViaggio = getCategorieDiViaggio(inputText)
                    val località = getLocalità(inputText)

                    if (categoriaViaggio=="Volo") {
                        cercaVoliBarraRicerca(località)
                    } else if(categoriaViaggio=="Crociera") {
                        cercaCrociereBarraRicerca(località)
                    } else if(categoriaViaggio=="Taxi") {
                        cercaTaxiBarraRicerca(località)
                    } else if(categoriaViaggio=="Auto") {
                        cercaAutoBarraRicerca(località)
                    } else {
                        cercaAlloggioBarraRicerca(località)
                    }
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun cercaAlloggioBarraRicerca(località: String?) {
        val query = "SELECT codice_alloggio, nome_alloggio, citta, data_inizio_disponibilita, data_fine_disponibilita, costo_giornaliero, num_ospiti from Alloggio where citta = '$località';"
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val risultatiAlloggio = responseBody.getAsJsonArray("queryset")
                        val stringList = java.util.ArrayList<String>()
                        for (i in 0 until risultatiAlloggio.size()) {
                            val alloggio = risultatiAlloggio[i].toString()
                            stringList.add(alloggio)
                        }
                        val fragment = FragmentRisultati.newInstance(stringList, "MainActivity")
                        val transaction = supportFragmentManager.beginTransaction()
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            transaction.replace(R.id.fragment_container_orizzontale, fragment)
                        }else{
                            transaction.replace(R.id.fragment_container, fragment)
                        }
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun cercaAutoBarraRicerca(località: String?) {
        val query = "SELECT nome_auto, citta, data_inizio_disponibilita, data_fine_disponibilita, prezzo_giornaliero from Auto where citta = '$località';"
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val risultatiAuto = responseBody.getAsJsonArray("queryset")
                        val stringList = java.util.ArrayList<String>()
                        for (i in 0 until risultatiAuto.size()) {
                            val auto = risultatiAuto[i].toString()
                            stringList.add(auto)
                        }
                        val fragment = FragmentRisultati.newInstance(stringList, "MainActivity")
                        val transaction = supportFragmentManager.beginTransaction()
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            transaction.replace(R.id.fragment_container_orizzontale, fragment)
                        }else{
                            transaction.replace(R.id.fragment_container, fragment)
                        }
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun cercaTaxiBarraRicerca(località: String?) {
        val query = "SELECT id_taxi, citta, data_disponibilita, orario_disponibilita, prezzo_orario from Taxi where citta = '$località';"
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val risultatiTaxi = responseBody.getAsJsonArray("queryset")
                        val stringList = java.util.ArrayList<String>()
                        for (i in 0 until risultatiTaxi.size()) {
                            val taxi = risultatiTaxi[i].toString()
                            stringList.add(taxi)
                        }
                        val fragment = FragmentRisultati.newInstance(stringList, "MainActivity")
                        val transaction = supportFragmentManager.beginTransaction()
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            transaction.replace(R.id.fragment_container_orizzontale, fragment)
                        }else{
                            transaction.replace(R.id.fragment_container, fragment)
                        }
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun cercaCrociereBarraRicerca(località: String?) {
        val query = "SELECT codice_crociera, nome_crociera, citta_partenza, data_partenza, data_ritorno, prezzo_viaggio from Crociera where citta_partenza = '$località';"
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val risultatiCrociera = responseBody.getAsJsonArray("queryset")
                        val stringList = java.util.ArrayList<String>()
                        for (i in 0 until risultatiCrociera.size()) {
                            val crociera = risultatiCrociera[i].toString()
                            stringList.add(crociera)
                        }
                        val fragment = FragmentRisultati.newInstance(stringList, "MainActivity")
                        val transaction = supportFragmentManager.beginTransaction()
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            transaction.replace(R.id.fragment_container_orizzontale, fragment)
                        }else{
                            transaction.replace(R.id.fragment_container, fragment)
                        }
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun cercaVoliBarraRicerca(località: String?) {
        val query = "SELECT codice, nome_volo, aeroporto_partenza, aeroporto_arrivo, data_partenza, ora_partenza, ora_arrivo, costo_biglietto from webmobile.Volo where aeroporto_arrivo = '$località';"
        val call = ClientNetwork.retrofit.select(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val risultatiVoli = responseBody.getAsJsonArray("queryset")
                        val stringList = java.util.ArrayList<String>()
                        for (i in 0 until risultatiVoli.size()) {
                            val volo = risultatiVoli[i].toString()
                            stringList.add(volo)
                        }
                        val fragment = FragmentRisultati.newInstance(stringList, "MainActivity")
                        val transaction = supportFragmentManager.beginTransaction()
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            transaction.replace(R.id.fragment_container_orizzontale, fragment)
                        }else{
                            transaction.replace(R.id.fragment_container, fragment)
                        }
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                showMessage("Errore di connessione: ${t.message}")
            }
        })
    }

    private fun showMessage(message: String) {
        //Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getCategorieDiViaggio(inputString: String): String {
        var categories: String = ""

        val lowerCaseInput = inputString.lowercase(Locale.getDefault())

        if (lowerCaseInput.contains("volo") || lowerCaseInput.contains("voli") || lowerCaseInput.contains("aereo")) {
            categories = "Volo"
        }
        if (lowerCaseInput.contains("crociera") || lowerCaseInput.contains("crociere")) {
            categories = "Crociera"
        }
        if (lowerCaseInput.contains("noleggio auto") || lowerCaseInput.contains("auto")) {
            categories = "Auto"
        }
        if (lowerCaseInput.contains("taxi")) {
            categories = "Taxi"
        }
        if (lowerCaseInput.contains("alloggio") || lowerCaseInput.contains("case") || lowerCaseInput.contains("stanza") || lowerCaseInput.contains("casa") || lowerCaseInput.contains("camera")) {
            categories = "Alloggio"
        }
        return categories
    }


    fun getLocalità(inputString: String): String? {
        val words = inputString.trim().split(" ")

        return if (words.isNotEmpty()) {
            words.last()
        } else {
            null
        }
    }



    private fun setupHorizontalRecyclerView1() {
            bindingOrizzontale.recyclerview1.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data = ArrayList<ItemsViewModel>()

            data.add(ItemsViewModel(R.drawable.volo, "Scopri tutti i voli"))
            data.add(ItemsViewModel(R.drawable.soggiorno, "Soggiorno"))
            data.add(ItemsViewModel(R.drawable.crociera, "Scopri le crociere"))
            data.add(ItemsViewModel(R.drawable.taxi, "Prenota taxi"))
            data.add(ItemsViewModel(R.drawable.noleggio_auto, "Noleggia un auto"))

            val adapter1 = CustomAdapter(data)
            bindingOrizzontale.recyclerview1.adapter = adapter1

            adapter1.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        0 -> {
                            // Scopri tutti i voli
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_orizzontale,FragmentVolo())
                                .addToBackStack(null)
                                .commit()
                        }
                        1 -> {
                            // Soggiorno
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_orizzontale,FragmentSoggiorno())
                                .addToBackStack(null)
                                .commit()
                        }
                        2 -> {
                            // Scopri le crociere
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_orizzontale,FragmentCrociera())
                                .addToBackStack(null)
                                .commit()
                        }
                        3 -> {
                            // Prenota taxi
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_orizzontale,FragmentTaxi())
                                .addToBackStack(null)
                                .commit()
                        }
                        4 -> {
                            //Noleggia un auto
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_orizzontale,FragmentAuto())
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            })
        }
        private fun setupHorizontalRecyclerView2() {
            bindingOrizzontale.recyclerview2.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data2 = ArrayList<ItemsViewModel>()

            data2.add(ItemsViewModel(R.drawable.personalizzato, "Personalizza un tuo viaggio"))
            data2.add(ItemsViewModel(R.drawable.pacchetto_famiglia, "Pacchetti famiglia"))
            data2.add(ItemsViewModel(R.drawable.coppia, "Pacchetti coppia"))
            data2.add(ItemsViewModel(R.drawable.amici, "Pacchetti amici"))

            val adapter2 = CustomAdapterPacchetti(data2)
            bindingOrizzontale.recyclerview2.adapter = adapter2

            adapter2.setOnItemClickListener(object : CustomAdapterPacchetti.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        0 -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_orizzontale,FragmentPacchetto())
                                .addToBackStack(null)
                                .commit()
                        }
                        1-> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_orizzontale,FragmentFamiglia())
                                .addToBackStack(null)
                                .commit()
                        }
                        2 -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_orizzontale,FragmentCoppia())
                                .addToBackStack(null)
                                .commit()
                        }
                        3 -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_orizzontale,FragmentAmici())
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            })
        }

        private fun setupRecyclerView1() {
            binding.recyclerview1.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data = ArrayList<ItemsViewModel>()

            data.add(ItemsViewModel(R.drawable.volo, "Scopri tutti i voli"))
            data.add(ItemsViewModel(R.drawable.soggiorno, "Soggiorno"))
            data.add(ItemsViewModel(R.drawable.crociera, "Scopri le crociere"))
            data.add(ItemsViewModel(R.drawable.taxi, "Prenota taxi"))
            data.add(ItemsViewModel(R.drawable.noleggio_auto, "Noleggia un auto"))

            val adapter1 = CustomAdapter(data)
            binding.recyclerview1.adapter = adapter1

            adapter1.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        0 -> {
                            // Scopri tutti i voli
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,FragmentVolo())
                                .addToBackStack(null)
                                .commit()
                        }
                        1 -> {
                            // Soggiorno
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,FragmentSoggiorno())
                                .addToBackStack(null)
                                .commit()
                        }
                        2 -> {
                            // Scopri le crociere
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,FragmentCrociera())
                                .addToBackStack(null)
                                .commit()
                        }
                        3 -> {
                            // Prenota taxi
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,FragmentTaxi())
                                .addToBackStack(null)
                                .commit()
                        }
                        4 -> {
                            //Noleggia un auto
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,FragmentAuto())
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            })
        }

        private fun setupRecyclerView2() {
            binding.recyclerview2.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data2 = ArrayList<ItemsViewModel>()

            data2.add(ItemsViewModel(R.drawable.personalizzato, "Personalizza un tuo viaggio"))
            data2.add(ItemsViewModel(R.drawable.pacchetto_famiglia, "Pacchetti famiglia"))
            data2.add(ItemsViewModel(R.drawable.coppia, "Pacchetti coppia"))
            data2.add(ItemsViewModel(R.drawable.amici, "Pacchetti amici"))

            val adapter2 = CustomAdapterPacchetti(data2)
            binding.recyclerview2.adapter = adapter2

            adapter2.setOnItemClickListener(object : CustomAdapterPacchetti.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        0 -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,FragmentPacchetto())
                                .addToBackStack(null)
                                .commit()
                        }
                        1 -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,FragmentFamiglia())
                                .addToBackStack(null)
                                .commit()
                        }
                        2 -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,FragmentCoppia())
                                .addToBackStack(null)
                                .commit()
                        }
                        3 -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,FragmentAmici())
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            })
        }

        private fun setupBottomNavigation() {
            bottomNavigationView = binding.bottomNavigation
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.Home -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.Profilo -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ProfiloFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }

                    R.id.Preferiti -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,PreferitiFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.Prenotazioni -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, FragmentPrenotazioni())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    else -> false
                }
        }
    }
    private fun setupBottomNavigationLand() {
        bottomNavigationView = bindingOrizzontale.bottomNavigationOrizzontale
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Profilo -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_orizzontale, ProfiloFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }

                R.id.Preferiti -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_orizzontale, PreferitiFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }

                R.id.Prenotazioni -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_orizzontale, FragmentPrenotazioni())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
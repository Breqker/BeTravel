package com.example.betravel

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.ActivityMainBinding
import com.example.betravel.databinding.ActivityMainOrizzontaleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        binding.editText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                val inputText = binding.editText.text.toString()
                val categoriaViaggio = getCategorieDiViaggio(inputText)
                val località = getLocalità(inputText)
                Log.d("MainActivity", "Categoria viaggio: $categoriaViaggio")
                Log.d("MainActivity", "Località: $località")
                true
            } else {
                false
            }
        }
    }


    private fun getCategorieDiViaggio(inputString: String): List<String> {
        val categories = mutableListOf<String>()

        val lowerCaseInput = inputString.lowercase(Locale.getDefault())

        // Verifica la presenza delle categorie di viaggio nella stringa di input
        if (lowerCaseInput.contains("volo") || lowerCaseInput.contains("voli") || lowerCaseInput.contains("aereo")) {
            categories.add("volo")
        }
        if (lowerCaseInput.contains("crociera") || lowerCaseInput.contains("crociere")) {
            categories.add("crociera")
        }
        if (lowerCaseInput.contains("noleggio auto") || lowerCaseInput.contains("auto")) {
            categories.add("noleggio auto")
        }
        if (lowerCaseInput.contains("taxi")) {
            categories.add("taxi")
        }
        if (lowerCaseInput.contains("alloggio") || lowerCaseInput.contains("case") || lowerCaseInput.contains("stanza") || lowerCaseInput.contains("casa") || lowerCaseInput.contains("camera")) {
            categories.add("alloggio")
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
            val data = ArrayList<ItemsViewModelCategorie>()

            data.add(ItemsViewModelCategorie(R.drawable.volo, "Scopri tutti i voli"))
            data.add(ItemsViewModelCategorie(R.drawable.soggiorno, "Soggiorno"))
            data.add(ItemsViewModelCategorie(R.drawable.crociera, "Scopri le crociere"))
            data.add(ItemsViewModelCategorie(R.drawable.taxi, "Prenota taxi"))
            data.add(ItemsViewModelCategorie(R.drawable.noleggio_auto, "Noleggia un auto"))

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
            val data2 = ArrayList<ItemsViewModelPacchetti>()

            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri tutti i voli"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Soggiorno"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Prenota taxi"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri le crociere"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Noleggia un auto"))

            val adapter2 = CustomAdapterPacchetti(data2)
            bindingOrizzontale.recyclerview2.adapter = adapter2

            adapter2.setOnItemClickListener(object : CustomAdapterPacchetti.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        //
                    }
                }
            })
        }

        private fun setupRecyclerView1() {
            binding.recyclerview1.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data = ArrayList<ItemsViewModelCategorie>()

            data.add(ItemsViewModelCategorie(R.drawable.volo, "Scopri tutti i voli"))
            data.add(ItemsViewModelCategorie(R.drawable.soggiorno, "Soggiorno"))
            data.add(ItemsViewModelCategorie(R.drawable.crociera, "Scopri le crociere"))
            data.add(ItemsViewModelCategorie(R.drawable.taxi, "Prenota taxi"))
            data.add(ItemsViewModelCategorie(R.drawable.noleggio_auto, "Noleggia un auto"))

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
            val data2 = ArrayList<ItemsViewModelPacchetti>()

            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri tutti i voli"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Soggiorno"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Prenota taxi"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri le crociere"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Noleggia un auto"))

            val adapter2 = CustomAdapterPacchetti(data2)
            binding.recyclerview2.adapter = adapter2

            adapter2.setOnItemClickListener(object : CustomAdapterPacchetti.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        //
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
                        val idUtente = intent.getIntExtra(EXTRA_ID_UTENTE, -1)
                        val fragment = ProfiloFragment.newInstance(idUtente)
                        val transaction = supportFragmentManager.beginTransaction()
                        val bundle = Bundle()
                        bundle.putInt("id_utente", idUtente)
                        fragment.arguments = bundle
                        transaction.replace(R.id.fragment_container, fragment)
                        transaction.addToBackStack(null) // Aggiungi il fragment al back stack
                        transaction.commit()
                        true
                    }

                    R.id.Preferiti -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,PreferitiFragment())
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
                    val idUtente = intent.getIntExtra(EXTRA_ID_UTENTE, -1)
                    val fragment = ProfiloFragment.newInstance(idUtente)
                    val transaction = supportFragmentManager.beginTransaction()
                    val bundle = Bundle()
                    bundle.putInt("id_utente", idUtente)
                    fragment.arguments = bundle
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.addToBackStack(null) // Aggiungi il fragment al back stack
                    transaction.commit()
                    true
                }

                R.id.Preferiti -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_orizzontale, PreferitiFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}
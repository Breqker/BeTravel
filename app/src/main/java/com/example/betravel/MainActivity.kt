package com.example.betravel

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.ActivityMainBinding
import com.example.betravel.databinding.ActivityMainOrizzontaleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingOrizzontale: ActivityMainOrizzontaleBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    private var currentFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingOrizzontale = ActivityMainOrizzontaleBinding.inflate(layoutInflater)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(bindingOrizzontale.root)
            bindingOrizzontale.recyclerview1.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data = ArrayList<ItemsViewModelCategorie>()

            data.add(ItemsViewModelCategorie(R.drawable.volo, "Scopri tutti i voli"))
            data.add(ItemsViewModelCategorie(R.drawable.soggiorno, "Soggiorno"))
            data.add(ItemsViewModelCategorie(R.drawable.taxi, "Prenota taxi"))
            data.add(ItemsViewModelCategorie(R.drawable.crociera, "Scopri le crociere"))
            data.add(ItemsViewModelCategorie(R.drawable.noleggio_auto,"Noleggia un auto"))

            val adapter1 = CustomAdapter(data)
            bindingOrizzontale.recyclerview1.adapter = adapter1

            adapter1.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        0 -> {
                            // Scopri tutti i voli
                            val intent = Intent(this@MainActivity, ActivityCategoria::class.java)
                            intent.putExtra("position",position)
                            startActivity(intent)
                        }
                        1 -> {
                            // Soggiorno
                            val intent = Intent(this@MainActivity, ActivityCategoria::class.java)
                            intent.putExtra("position",position)
                            startActivity(intent)
                        }
                        2 -> {
                            // Prenota taxi
                            val intent = Intent(this@MainActivity, ActivityCategoria::class.java)
                            intent.putExtra("position",position)
                            startActivity(intent)
                        }
                        3 -> {
                            // Scopri le crociere
                            val intent = Intent(this@MainActivity, ActivityCategoria::class.java)
                            intent.putExtra("position",position)
                            startActivity(intent)
                        }
                        /*
                         */
                    }
                }
            })

            bindingOrizzontale.recyclerview2.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data2 = ArrayList<ItemsViewModelPacchetti>()

            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri tutti i voli"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Soggiorno"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Prenota taxi"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri le crociere"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia,"Noleggia un auto"))

            val adapter2 = CustomAdapterPacchetti(data2)
            bindingOrizzontale.recyclerview2.adapter = adapter2

            adapter2.setOnItemClickListener(object : CustomAdapterPacchetti.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        //
                    }
                }
            })

        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
            binding.recyclerview1.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data = ArrayList<ItemsViewModelCategorie>()

            data.add(ItemsViewModelCategorie(R.drawable.volo, "Scopri tutti i voli"))
            data.add(ItemsViewModelCategorie(R.drawable.soggiorno, "Soggiorno"))
            data.add(ItemsViewModelCategorie(R.drawable.taxi, "Prenota taxi"))
            data.add(ItemsViewModelCategorie(R.drawable.crociera, "Scopri le crociere"))
            data.add(ItemsViewModelCategorie(R.drawable.noleggio_auto, "Noleggia un auto"))

            val adapter1 = CustomAdapter(data)
            binding.recyclerview1.adapter = adapter1

            adapter1.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        0 -> {
                            // Scopri tutti i voli
                            val intent = Intent(this@MainActivity, ActivityCategoria::class.java)
                            intent.putExtra("position",position)
                            startActivity(intent)
                        }
                        1 -> {
                            // Soggiorno
                            val intent = Intent(this@MainActivity, ActivityCategoria::class.java)
                            intent.putExtra("position",position)
                            startActivity(intent)
                        }
                        2 -> {
                            // Prenota taxi
                            val intent = Intent(this@MainActivity, ActivityCategoria::class.java)
                            intent.putExtra("position",position)
                            startActivity(intent)
                        }
                        3 -> {
                            // Scopri le crociere
                            val intent = Intent(this@MainActivity, ActivityCategoria::class.java)
                            intent.putExtra("position",position)
                            startActivity(intent)
                        }
                        /*
                         */
                    }
                }
            })

            binding.recyclerview2.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data2 = ArrayList<ItemsViewModelPacchetti>()

            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri tutti i voli"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Soggiorno"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Prenota taxi"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri le crociere"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia,"Noleggia un auto"))

            val adapter2 = CustomAdapterPacchetti(data2)
            binding.recyclerview2.adapter = adapter2

            adapter2.setOnItemClickListener(object : CustomAdapterPacchetti.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    when (position) {
                        //
                    }
                }
            })
            val bottomNavigationFragment = BottomNavigationFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, bottomNavigationFragment)
                .commit()

            bottomNavigationView = findViewById(R.id.bottom_navigation)
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.Home -> {
                        // Chiudi il fragment aperto precedentemente
                        if (currentFragment != null) {
                            supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
                            currentFragment = null
                        }
                        true
                    }

                    R.id.Profilo -> {
                        // Chiudi il fragment aperto precedentemente
                        if (currentFragment != null) {
                            supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
                            currentFragment = null
                        }
                        // Apri il fragment ProfiloFragement
                        val fragmentProfilo = ProfiloFragment()
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, fragmentProfilo).commit()
                        currentFragment = fragmentProfilo
                        true
                    }

                    R.id.Preferiti -> {
                        // Chiudi il fragment aperto precedentemente
                        if (currentFragment != null) {
                            supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
                            currentFragment = null
                        }
                        val fragmentPreferiti = PreferitiFragment()
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, fragmentPreferiti).commit()
                        currentFragment = fragmentPreferiti
                        true
                    }
                    R.id.Impostazioni -> {
                        // Chiudi il fragment aperto precedentemente
                        if (currentFragment != null) {
                            supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
                            currentFragment = null
                        }
                        val fragmentImpostazioni = ImpostazioniFragment()
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, fragmentImpostazioni).commit()
                        currentFragment = fragmentImpostazioni
                        true
                    }
                    else -> false
                }
            }
        }
    }
}
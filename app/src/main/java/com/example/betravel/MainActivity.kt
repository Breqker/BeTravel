package com.example.betravel

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.ActivityMainBinding
import com.example.betravel.databinding.ActivityMainOrizzontaleBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingOrizzontale: ActivityMainOrizzontaleBinding

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

            val adapter1 = CustomAdapter(data)
            bindingOrizzontale.recyclerview1.adapter = adapter1

            bindingOrizzontale.recyclerview2.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data2 = ArrayList<ItemsViewModelPacchetti>()

            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri tutti i voli"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Soggiorno"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Prenota taxi"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri le crociere"))

            val adapter2 = CustomAdapterPacchetti(data2)
            bindingOrizzontale.recyclerview2.adapter = adapter2

        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
            binding.recyclerview1.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data = ArrayList<ItemsViewModelCategorie>()

            data.add(ItemsViewModelCategorie(R.drawable.volo, "Scopri tutti i voli"))
            data.add(ItemsViewModelCategorie(R.drawable.soggiorno, "Soggiorno"))
            data.add(ItemsViewModelCategorie(R.drawable.taxi, "Prenota taxi"))
            data.add(ItemsViewModelCategorie(R.drawable.crociera, "Scopri le crociere"))
            data.add(ItemsViewModelCategorie(R.drawable.noleggio_auto,"Noleggia un auto"))

            val adapter1 = CustomAdapter(data)
            binding.recyclerview1.adapter = adapter1

            binding.recyclerview2.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            val data2 = ArrayList<ItemsViewModelPacchetti>()

            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri tutti i voli"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Soggiorno"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Prenota taxi"))
            data2.add(ItemsViewModelPacchetti(R.drawable.pacchetto_famiglia, "Scopri le crociere"))

            val adapter2 = CustomAdapterPacchetti(data2)
            binding.recyclerview2.adapter = adapter2
        }


    }
}

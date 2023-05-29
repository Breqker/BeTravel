package com.example.betravel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.PacchettiRowItemsBinding

class CustomAdapter(private val myList: List<ItemsViewModelCategorie>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(binding: PacchettiRowItemsBinding): RecyclerView.ViewHolder(binding.root){
        val imageView = binding.imagePacchetto
        val textView = binding.categoriaPacchetto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = PacchettiRowItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myList[position]
        holder.imageView.setImageResource(item.image)
        holder.textView.text = item.text
    }
}

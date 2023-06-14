package com.example.betravel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.PacchettiRowItemsBinding

class CustomAdapterPacchetti(private val myList: List<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapterPacchetti.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    class ViewHolder(binding: PacchettiRowItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.imagePacchetto
        val textView = binding.categoriaPacchetto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PacchettiRowItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myList[position]
        holder.imageView.setImageResource(item.image)
        holder.textView.text = item.text

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

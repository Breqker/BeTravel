package com.example.betravel

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.databinding.PacchettiRowItemsBinding
import com.example.betravel.databinding.PreferitiElementBinding

class CustomAdapterRisultati(private val myList: List<ItemsViewModelPreferiti>) : RecyclerView.Adapter<CustomAdapterRisultati.ViewHolder>() {

    // Utilizzo ItemsViewModelPreferiti perché c'è una ImageView e un TextView.
    // In caso si crea un altro ItemViewModel

    private var listener: OnItemClickListener? = null

    class ViewHolder(binding: PreferitiElementBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.imagePreferiti
        val textView = binding.textView4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PreferitiElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myList[position]
        holder.imageView.setImageResource(item.image)
        holder.textView.text = item.text

        // Imposta la dimensione del carattere desiderata
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

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

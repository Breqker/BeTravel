import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.betravel.Review
import com.example.betravel.databinding.ItemReviewBinding

class ReviewsAdapter(private val myList: List<Review>) : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val reviewText = binding.reviewText
        val reviewRating = binding.reviewRating
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myList[position]
        holder.reviewText.text = item.text
        holder.reviewRating.rating = item.rating
    }

    override fun getItemCount(): Int {
        return myList.size
    }
}

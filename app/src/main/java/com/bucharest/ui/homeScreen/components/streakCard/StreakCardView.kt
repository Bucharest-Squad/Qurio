import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.StreakDayCardBinding
import com.bucharest.ui.homeScreen.components.streakCard.StreakDayUiState

class StreakDayAdapter(
    private val items: List<StreakDayUiState>
) : RecyclerView.Adapter<StreakDayAdapter.StreakViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreakViewHolder {
        val binding = StreakDayCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StreakViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StreakViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class StreakViewHolder(private val binding: StreakDayCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StreakDayUiState) {
            binding.dayLabel.text = item.day
            binding.circle.setCardBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (item.isSelected) R.color.orange_variant else R.color.surface
                )
            )
            binding.fire.visibility = if (item.isSelected) View.VISIBLE else View.GONE
        }
    }
}

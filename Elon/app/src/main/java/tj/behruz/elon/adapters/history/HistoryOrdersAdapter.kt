package tj.behruz.elon.adapters.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import tj.behruz.elon.R
import tj.behruz.elon.databinding.HistoryItemBinding
import tj.behruz.elon.helpers.Utils
import tj.behruz.elon.models.History

class HistoryOrdersAdapter(val context: Context, private var histories: ArrayList<History>) :
    RecyclerView.Adapter<HistoryOrdersAdapter.HistoryOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HistoryOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryOrderViewHolder, position: Int) {
        val history=histories[position]
        holder.setHistory(history)
    }

    fun dataChanged(newList: ArrayList<History>) {
        histories = newList
        notifyDataSetChanged()
    }

    override fun getItemCount()=histories.size
    inner class HistoryOrderViewHolder(private val historyItemBinding: HistoryItemBinding) :
        RecyclerView.ViewHolder(historyItemBinding.root) {

        fun setHistory(history: History) {
            historyItemBinding.currentDate.text = history.add_date!!.split(" ")[0]
            historyItemBinding.title.text=history.text
            historyItemBinding.phoneNumber.text = history.phones!!.split(";")[0]
            historyItemBinding.status.text = Utils.getStatus(history.status.toString())
            Picasso.get().load(history.tv_img).placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user).noFade().into(historyItemBinding.tvImage)
            historyItemBinding.executePendingBindings()
        }

    }


}
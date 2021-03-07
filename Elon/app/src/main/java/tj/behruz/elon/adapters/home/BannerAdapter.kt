package tj.behruz.elon.adapters.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import tj.behruz.elon.R
import tj.behruz.elon.databinding.BannerItemBinding
import tj.behruz.elon.models.Slider

class BannerAdapter(val context: Context, private val banners: List<Slider>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val bannerItemBinding = BannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BannerViewHolder(bannerItemBinding)

    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val item = banners[position].setting_value
        holder.setBanner(item)
    }

    override fun getItemCount() = banners.size


    inner class BannerViewHolder(private val bannerItemBinding: BannerItemBinding) : RecyclerView.ViewHolder(bannerItemBinding.root) {

        fun setBanner(bannerUrl: String) {
            Picasso.get().load(bannerUrl).placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user).noFade().into(bannerItemBinding.bannerItem)
            bannerItemBinding.executePendingBindings()
        }

    }


}
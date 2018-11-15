package com.beacheslav.market

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beacheslav.market.model.Banner
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_banner.view.*

class BannersAdapter (var bannersList : ArrayList<Banner>?) : RecyclerView.Adapter<BannersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannersAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_banner, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (bannersList != null) {
            bannersList!!.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: BannersAdapter.ViewHolder, position: Int) {
        if (bannersList != null) {
            holder.bindItems(bannersList!![position])
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(banner : Banner){
            Picasso.get().load(banner.image).into(itemView.imageBanner)
        }
    }


}
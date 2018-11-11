package com.beacheslav.market

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beacheslav.market.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_offer.view.*

class MarketAdapter(var offerList : ArrayList<Offer>?, var bannerList : ArrayList<Banner>?) : RecyclerView.Adapter<MarketAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_offer, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MarketAdapter.ViewHolder, position: Int) {
        holder.bindItems(offerList!![position])
    }

    override fun getItemCount(): Int {
        if (offerList != null){
            return offerList!!.size
        } else {
            return 0
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(offer: Offer) {
                itemView.textViewName.text = offer.name
                itemView.textViewDesc.text = offer.desc
                if(offer.price != 0){
                    itemView.textViewPrice.text = offer.price.toString()
                    itemView.textViewPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    itemView.textViewDiscountPrice.text = (offer.price * (1 - offer.discount)).toString()
                    itemView.textViewDiscount.text = (offer.discount * 100).toString()
                }else{
                    itemView.textViewDiscount.visibility = View.GONE
                }
                Picasso.get().load(offer.image).into(itemView.imageView)
        }
    }
}
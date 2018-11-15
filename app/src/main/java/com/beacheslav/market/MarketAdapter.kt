package com.beacheslav.market

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.beacheslav.market.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_banners.view.*
import kotlinx.android.synthetic.main.list_item_category.view.*
import kotlinx.android.synthetic.main.list_item_offer.view.*

class MarketAdapter(var dataSet: ArrayList<RowType>?, val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        if (dataSet == null) {
            return -1
        }
        return when (dataSet!![position]) {
            is TopSliderType -> RowType.TOP_SLIDER_ROW_TYPE
            is CategoryType -> RowType.CATEGORY_ROW_TYPE
            is Offer -> RowType.OFFER_ROW_TYPE
            else -> -1
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (dataSet != null) {
            when (viewType) {
                RowType.TOP_SLIDER_ROW_TYPE -> {
                    val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_banners, parent, false)
                    return TopSliderViewHolder(v, context)
                }
                RowType.CATEGORY_ROW_TYPE -> {
                    val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false)
                    return CategoryViewHolder(v)
                }
                RowType.OFFER_ROW_TYPE -> {
                    val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_offer, parent, false)
                    return OfferViewHolder(v)
                }
            }
        }
        //дефолтный холдер, код не выполнится
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_offer, parent, false)
        return OfferViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (dataSet!= null) {
            when(holder){
                is OfferViewHolder -> (holder as OfferViewHolder).bindItems(dataSet!![position] as Offer)
                is TopSliderViewHolder -> (holder as TopSliderViewHolder).bindItems(dataSet!![position] as TopSliderType)
                is CategoryViewHolder -> (holder as CategoryViewHolder).bindItems((dataSet!![position]) as CategoryType)
            }
        }
    }

    override fun getItemCount(): Int {
        if (dataSet != null){
            return dataSet!!.size
        } else {
            return 0
        }
    }

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(offer: Offer) {
                itemView.textViewName.text = offer.name
                itemView.textViewDesc.text = offer.desc
                if(offer.price != 0){
                    itemView.textViewPrice.text = offer.price.toString()
                    itemView.textViewPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    itemView.textViewDiscountPrice.text = (offer.price * (1 - offer.discount)).toInt().toString()
                    itemView.textViewDiscount.text = (offer.discount * 100).toString()
                }else{
                    itemView.textViewDiscount.visibility = View.GONE
                }
                Picasso.get().load(offer.image).into(itemView.imageView)
        }
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(categoryType: CategoryType){
            itemView.textViewCategory.text = categoryType.category
        }
    }

    class TopSliderViewHolder(itemView: View, val  context : Context) : RecyclerView.ViewHolder(itemView) {
        var bannersAdapter = BannersAdapter(arrayListOf())

        init{
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(itemView.horizontalRecyclerView)
            with(itemView.horizontalRecyclerView) {
                onFlingListener = snapHelper
                layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                adapter = bannersAdapter
            }
        }


        fun bindItems(topSliderType: TopSliderType){
            bannersAdapter.bannersList = topSliderType.banners
        }
    }
}
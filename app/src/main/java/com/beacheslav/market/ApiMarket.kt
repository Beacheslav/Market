package com.beacheslav.market

import com.beacheslav.market.model.Banner
import com.beacheslav.market.model.Offer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiMarket {

    @GET("/sl.files/banners.json")
    fun searchBanners(): Call<List<Banner>>

    @GET("/sl.files/offers.json")
    fun searchOffers(): Call<List<Offer>>

    /**
     * Фабрика, создаём и возвращаем ApiMarket когда это необходимо
     */
    companion object Factory {
        fun create(): ApiMarket {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://s3.eu-central-1.amazonaws.com")
                    .build()

            return retrofit.create(ApiMarket::class.java)
        }
    }
}
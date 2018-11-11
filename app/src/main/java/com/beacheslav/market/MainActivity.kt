package com.beacheslav.market

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import com.beacheslav.market.model.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var mOffers : ArrayList<Offer>? = null
    var mBanners : ArrayList<Banner>? = null
    var mAdapterList : List<RowType>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val adapter = MarketAdapter(mOffers, mBanners)
        recyclerView.adapter = adapter

        val apiService = ApiMarket.create()
        apiService.searchOffers().enqueue(object : Callback<List<Offer>>{
            override fun onFailure(call: Call<List<Offer>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "An error occurred during networking" , Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Offer>>?, response: Response<List<Offer>>?) {
                if (response != null) {
                    mOffers = response.body() as ArrayList<Offer>?
                    adapter.offerList = mOffers
                    sortHolderType(mOffers, mBanners)
                    adapter.notifyDataSetChanged()
                }
            }
        })

//        apiService.searchBanners().enqueue(object : Callback<List<Banner>>{
//            override fun onFailure(call: Call<List<Banner>>?, t: Throwable?) {
//                Toast.makeText(this@MainActivity, "An error occurred during networking" , Toast.LENGTH_LONG).show()
//            }
//
//            override fun onResponse(call: Call<List<Banner>>?, response: Response<List<Banner>>?) {
//                if (response != null) {
//                    mBanners = response.body() as ArrayList<Banner>?
//                    adapter.bannerList = mBanners
//                    //чот сортируем
//                    adapter.notifyDataSetChanged()
//                }
//
//            }
//        })
    }

    fun sortHolderType(offers: ArrayList<Offer>?, banners: ArrayList<Banner>?): List<RowType>{
        var list: ArrayList<RowType> = ArrayList()
        list.add(0, TopSliderType(banners!!))                                   // в первом элементе всегда список баннеров
        list.add(1, CategoryType(offers!![0].groupName))                        // во втором элементе всегда объект категории предложений
        list.add(2, offers[0])                                                  // в третьем элементе первый элемент списка предложений
        var currentCategory : CategoryType = list[1] as CategoryType                  // переменная, по которой сортируем все предложения по данной категории
        offers.removeAt(0)
        var index : Int = 0

        while (banners.size != 0){
        //пробегаемся по всем элементам и записываем в список все элементы текущей категории
        for (offer in offers){
            if (offer.groupName.equals(currentCategory.category)){
                list.add(offer)
                offers.removeAt(index)
                index --                                                              //так как коллекция уменьшилась, уменьшаем индекс, чтобы не пропустить элемент
            }
            index ++                                                                  //увеличеваем индекс, чтобы знать, по какому индексу удалять элементы
        }
            if (list.size != 0) {
                currentCategory = CategoryType(offers[0].groupName)                   //прошлись по всем элементам массива и обновляем категорию
            }

        index = 0
        }
        return list
    }
}

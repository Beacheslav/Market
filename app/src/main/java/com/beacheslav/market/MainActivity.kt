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
    var mAdapterList : ArrayList<RowType>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val adapter = MarketAdapter(mAdapterList)
        recyclerView.adapter = adapter

        //        val snapHalper : SnapHelper = LinearSnapHelper()
        //        snapHalper.attachToRecyclerView(recyclerView)

        val apiService = ApiMarket.create()
        apiService.searchOffers().enqueue(object : Callback<List<Offer>>{
            override fun onFailure(call: Call<List<Offer>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "An error occurred during networking" , Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Offer>>?, response: Response<List<Offer>>?) {
                if (response != null) {
                    mOffers = response.body() as ArrayList<Offer>?
                    adapter.adapterList = sortHolderType(mOffers, mBanners)
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

    fun sortHolderType(offers: ArrayList<Offer>?, banners: ArrayList<Banner>?): ArrayList<RowType>{              //FIXME: offers==null
        var list: ArrayList<RowType> = ArrayList()                                    // лист, возвращаемый методом
        var secondList: ArrayList<Offer> = offers!!                                   // взпомогательный лист для сортировки

        if(banners != null) {
            list.add(0,TopSliderType(banners))                                  // в первом элементе всегда список баннеров
        }else {
            list.add(0,TopSliderType(ArrayList<Banner>()))
        }
        list.add(1, CategoryType(offers!![0].groupName))                        // во втором элементе всегда объект категории предложений
        var currentCategory : String = offers[0].groupName                            // переменная, по которой сортируем все предложения по текущей категории
        while (true){
            val pair : Pair<List<Offer>, List<Offer>> = secondList.partition { it.groupName.equals(currentCategory)}
            secondList = pair.second as ArrayList<Offer>                              // фильтруем лист, новые данные добавляем в возвращаемый лист
            list.addAll(pair.first)                                                   // оставшиеся данные записываем во вспомогательный лист
            if (secondList.isEmpty()){                                                // если больше данных не осталось выходим, возвращая лист
                return list
            }
            list.add(CategoryType(secondList[0].groupName))                           // иначе обновляем категорию и повторяем все действия, пока вспомогательный лист не будет пуст
            currentCategory = secondList[0].groupName
        }
    }
}

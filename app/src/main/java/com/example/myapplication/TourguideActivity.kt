package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.TourguideAdapter
import com.example.myapplication.retrofit_destinasi.ApiService
import com.example.myapplication.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TourguideActivity : AppCompatActivity() {
    val TAG : String = "TourguideActivity"

    private lateinit var tourguideAdapter: TourguideAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_guide)
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        getDataFromApi()

    }

    private fun setupRecyclerView() {
        val recyclerView by lazy { findViewById<RecyclerView>(R.id.rv_heroes_tourguide) }
        tourguideAdapter = TourguideAdapter(arrayListOf(), object : TourguideAdapter.OnAdapterListener {
                override fun onClick(results: DataTourguide) {
                    startActivity(
                        Intent(applicationContext, DetailTourguide::class.java)
                            .putExtra("name_tour", results.name)
                            .putExtra("price_tour", results.price.toString())
                            .putExtra("location_tour", results.city)
                            .putExtra("desc_tour", results.desc)
                            .putExtra("photo_tour", "http://192.168.100.7:3000${results.photo_tour}")
                    )
                }
            })

        val backButton: ImageButton = findViewById<ImageButton>(R.id.kembali)
        backButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("checkbox_status", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        recyclerView.apply {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = tourguideAdapter
        }
        }



        fun getDataFromApi() {
            val progressBar = findViewById<ProgressBar>(R.id.progressBar2)
            progressBar!!.visibility = View.VISIBLE
            com.example.myapplication.retrofit_tourguide.ApiService.endPoint.getData()
                .enqueue(object : Callback<TourguideModel>{
                    override fun onResponse(
                        call: Call<TourguideModel>,
                        response: Response<TourguideModel>)
                    {
                        progressBar.visibility = View.GONE
                        if (response.isSuccessful) {
                            if(response.body()!= null) showData(response.body()!!)
                        }
                    }

                    override fun onFailure(call: Call<TourguideModel>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        printLog("on Failure : $t")
                    }

                })
        }

        fun printLog(message: String) {
            Log.d(TAG, message)
        }

        fun showData(data: TourguideModel) {
            val results: ArrayList<DataTourguide> = arrayListOf()
            results.addAll(data.data)
            tourguideAdapter.setData(results)
        }
    }





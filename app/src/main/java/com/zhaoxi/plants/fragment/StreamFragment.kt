package com.zhaoxi.plants.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zhaoxi.plants.R
import com.zhaoxi.plants.adapter.PlantStreamAdapter
import com.zhaoxi.plants.network.NetworkUtil
import com.zhaoxi.plants.viewmodel.MainViewModel
import kotlinx.coroutines.*

class StreamFragment: Fragment(R.layout.stream_fragment) {

    lateinit var plantStream: RecyclerView
    private var viewModel: MainViewModel? = null
    private var adapter: PlantStreamAdapter? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //define view model
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        //initialize recycler view
        initRecyclerView()
        initScrollListener()
        //initialize refresh
        val swipeRefreshLayout: SwipeRefreshLayout = requireView().findViewById(R.id.plant_swipe)
        swipeRefreshLayout.setOnRefreshListener {
            NetworkUtil.instance.refreshPlantList(viewModel!!)
            checkNetworkConnection()
            GlobalScope.launch {
                delay(10000)
                if(swipeRefreshLayout.isRefreshing){
                    checkNetworkConnection()
                    swipeRefreshLayout.isRefreshing = false
                }

            }
        }
        NetworkUtil.instance.refreshPlantList(viewModel!!)
        //view model observes the live data
        viewModel?.isLoading?.observe(viewLifecycleOwner){
            if(it){
                loadMore()
            }
        }
        viewModel?.plantList?.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                adapter!!.updateStream(list)
            }else{
                Toast.makeText(activity, "Cannot refresh plant list, please check network connection!", Toast.LENGTH_SHORT).show()
            }
            if(swipeRefreshLayout.isRefreshing)
                swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initRecyclerView(){
        plantStream = requireView().findViewById(R.id.video_stream_view)
        adapter = activity?.let { PlantStreamAdapter(it, true) }
        plantStream.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        plantStream.layoutManager = linearLayoutManager
    }

    private fun initScrollListener(){
        plantStream.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager = plantStream.layoutManager as LinearLayoutManager
                if(viewModel?.getIsLoading() == false){
                    if(layoutManager.findLastCompletelyVisibleItemPosition() == (viewModel?.plantList?.value?.size ?: 1) - 1){
                        viewModel?.startLoading()
                    }
                }
            }
        })
    }

    private fun loadMore(){
        viewModel?.insertNull()
        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            delay(2000)
            viewModel?.let { NetworkUtil.instance.loadMore(it) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkNetworkConnection(){
        val connectManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectManager.getNetworkCapabilities(connectManager.activeNetwork)
        if(networkInfo == null)
            Toast.makeText(requireActivity(),"No Internet Connection!",Toast.LENGTH_LONG).show()
        networkInfo?.let {
            when {
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Toast.makeText(requireActivity(),"You are using mobile data",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
package com.zhaoxi.plants.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zhaoxi.plants.R
import com.zhaoxi.plants.adapter.PlantStreamAdapter
import com.zhaoxi.plants.model.Plant
import com.zhaoxi.plants.viewmodel.MainViewModel

class FavoriteFragment: Fragment(R.layout.stream_fragment) {
    private lateinit var plantStream: RecyclerView


    private var viewModel: MainViewModel? = null
    private var adapter: PlantStreamAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //define view model
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.plant_swipe)
        swipeRefreshLayout.isEnabled = false
        //initialize recycler view
        initRecyclerView()
        //add line
        plantStream.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        viewModel!!.favoritePlantList.observe(viewLifecycleOwner){
            adapter?.updateStream(it as ArrayList<Plant?>)
        }
        viewModel!!.favoritePlantList.value?.let { adapter?.updateStream(it as ArrayList<Plant?>) }

    }

    private fun initRecyclerView(){
        plantStream = requireView().findViewById(R.id.video_stream_view)
        adapter = activity?.let { PlantStreamAdapter(it, false) }
        plantStream.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        plantStream.layoutManager = linearLayoutManager
    }


}
package com.zhaoxi.plants.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zhaoxi.plants.R
import com.zhaoxi.plants.adapter.PlantFragmentPagerAdapter
import com.zhaoxi.plants.dao.FavoritePlantDao
import com.zhaoxi.plants.fragment.FavoriteFragment
import com.zhaoxi.plants.fragment.StreamFragment
import com.zhaoxi.plants.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var favoritePlantDao: FavoritePlantDao
    lateinit var streamFragment: StreamFragment
    lateinit var favoriteFragment: FavoriteFragment
    val viewModel: MainViewModel by viewModels()
    lateinit var menuItem: MenuItem
    lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolBar.navigationIcon = null
        setSupportActionBar(toolBar)
        bottomNavigation = findViewById(R.id.bottom_nav)
        menuItem = bottomNavigation.menu.getItem(0);
        val viewPager: ViewPager = findViewById(R.id.content)
        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.main_stream_page -> {viewPager.currentItem = 0; false}
                R.id.favorite_page -> {viewPager.currentItem = 1; false}
                else -> false
            }
        }
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                menuItem.isChecked = false
                menuItem = bottomNavigation.menu.getItem(position);
                menuItem.isChecked = true;
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        initViewPager(viewPager)
    }

    fun initViewPager(viewPager: ViewPager){
        streamFragment = StreamFragment()
        favoriteFragment = FavoriteFragment()
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(streamFragment)
        fragmentList.add(favoriteFragment)
        val plantFragmentAdapter = PlantFragmentPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = plantFragmentAdapter
    }



}
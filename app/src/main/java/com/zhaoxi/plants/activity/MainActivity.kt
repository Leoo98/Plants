package com.zhaoxi.plants.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zhaoxi.plants.R
import com.zhaoxi.plants.adapter.PlantFragmentPagerAdapter
import com.zhaoxi.plants.dao.FavoritePlantDao
import com.zhaoxi.plants.database.FavoritePlantDatabase
import com.zhaoxi.plants.fragment.FavoriteFragment
import com.zhaoxi.plants.fragment.StreamFragment
import com.zhaoxi.plants.viewmodel.MainViewModel
import com.zhaoxi.plants.viewmodel.MainViewModelFactory


class MainActivity : AppCompatActivity() {

    lateinit var streamFragment: StreamFragment
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var favoriteFragment: FavoriteFragment
    lateinit var viewModel: MainViewModel
    lateinit var favoritePlantDao: FavoritePlantDao
    lateinit var menuItem: MenuItem
    lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolBar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolBar.navigationIcon = null
        setSupportActionBar(toolBar)
        favoritePlantDao = FavoritePlantDatabase.getInstance(this).favoritePlantDao()!!
        viewModel = ViewModelProvider(this, MainViewModelFactory(favoritePlantDao)).get(MainViewModel::class.java)
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

//    private fun changeFragment(fragment: Fragment) {
//        fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.content_fragment, fragment)
//        fragmentTransaction.commit()
//    }


}
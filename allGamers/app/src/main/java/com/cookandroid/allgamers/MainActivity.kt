package com.cookandroid.allgamers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.cookandroid.allgamers.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    //멤버 변수 선언
    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment : SearchFragment
    private lateinit var binding : ActivityMainBinding

    companion object{
        const val TAG: String = "로그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d(TAG, "MainActivity - onCreate() called")

        binding.bottomNav.setOnNavigationItemSelectedListener(this)
        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragments_frame, homeFragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG,"MainActivity - onCreate() called")

        when(item.itemId){
            R.id.menu_home ->{

                homeFragment = HomeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragments_frame, homeFragment).commit()
            }
            R.id.menu_profile -> {

                profileFragment = SearchFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragments_frame, profileFragment).commit()
            }
        }
        return true
    }
}
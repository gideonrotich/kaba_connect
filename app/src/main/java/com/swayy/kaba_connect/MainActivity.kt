package com.swayy.kaba_connect

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.swayy.kaba_connect.Fragments.*

/**
 * Code written by Gideon Rotich
 */
class MainActivity : AppCompatActivity() {

    lateinit var feedFragment: FeedFragment
    lateinit var discoverFragment: discoverFragment
    lateinit var forumsFragment: forumsFragment
    lateinit var profileFragment: profileFragment
    lateinit var messageFragment: messageFragment
    lateinit var notificationFragment: notificationFragment


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.btn_nav)

        feedFragment = FeedFragment()
        supportFragmentManager
            .beginTransaction().replace(R.id.frame_layout, feedFragment).setTransition(
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN
            ).commit()


        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.navigation_home -> {
                    feedFragment = FeedFragment()
                    supportFragmentManager
                        .beginTransaction().replace(R.id.frame_layout, feedFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }

                R.id.navigation_not -> {
                    discoverFragment = discoverFragment()
                    supportFragmentManager
                        .beginTransaction().replace(R.id.frame_layout, discoverFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }

                R.id.navigation_dashboard -> {
                   startActivity(Intent(this,AddPostActivity::class.java))
                }
                R.id.navigation_message -> {
//                    startActivity(Intent(this,LatestMessagesActivity::class.java))
                    notificationFragment = notificationFragment()
                    supportFragmentManager
                        .beginTransaction().replace(R.id.frame_layout, notificationFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }

                R.id.navigation_profile -> {
                    profileFragment = profileFragment()
                    supportFragmentManager
                        .beginTransaction().replace(R.id.frame_layout, profileFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }
            }
            true
        }


    }
}
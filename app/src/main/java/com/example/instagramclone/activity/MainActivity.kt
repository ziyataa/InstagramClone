package com.example.instagramclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.instagramclone.R
import com.example.instagramclone.fragments.HomeFragment
import com.example.instagramclone.fragments.NotificationFragment
import com.example.instagramclone.fragments.ProfileFragment
import com.example.instagramclone.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId){
            R.id.nav_home ->{
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_search ->{
                moveToFragment(SearchFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_add_post ->{
                item.isChecked = false
                startActivity(Intent(this, AddPostActivity::class.java))
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_notification ->{
                moveToFragment(NotificationFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.nav_profile ->{
                moveToFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener  true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView : BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //agar home fragment menjadi default  saat pertama applikasinya dijalankan
        moveToFragment(HomeFragment())
    }

    //Method pindah ke fragment
    //dibuat private agar hanya metode ini hanya bisa diakses di MainActivity saja
    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()
    }
}
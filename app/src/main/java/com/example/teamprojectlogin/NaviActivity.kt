package com.example.teamprojectlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.teamprojectlogin.databinding.ActivityNaviBinding
import com.google.android.gms.maps.MapFragment


private const val TAG_CALENDER = "calender_fragment"
private const val TAG_MAP = "todo_fragment"
private const val TAG_MY_PAGE = "my_page_fragment"

class NaviActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_MAP, MapFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calenderFragment -> setFragment(TAG_CALENDER, CalendarFragment())
                R.id.homeFragment -> setFragment(TAG_MAP, MapFragment())
                R.id.todoFragment -> setFragment(TAG_MY_PAGE, TodoFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val calender = manager.findFragmentByTag(TAG_CALENDER)
        val home = manager.findFragmentByTag(TAG_MAP)
        val myPage = manager.findFragmentByTag(TAG_MY_PAGE)

        if (calender != null) {
            fragTransaction.hide(calender)
        }

        if (home != null) {
            fragTransaction.hide(home)
        }

        if (myPage != null) {
            fragTransaction.hide(myPage)
        }

        if (tag == TAG_CALENDER) {
            if (calender != null) {
                fragTransaction.show(calender)
            }
        } else if (tag == TAG_MAP) {
            if (home != null) {
                fragTransaction.show(home)
            }
        } else if (tag == TAG_MY_PAGE) {
            if (myPage != null) {
                fragTransaction.show(myPage)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}
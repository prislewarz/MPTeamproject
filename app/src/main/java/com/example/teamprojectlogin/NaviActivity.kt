package com.example.teamprojectlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.teamprojectlogin.databinding.ActivityNaviBinding
import com.google.android.gms.maps.MapFragment


private const val TAG_CALENDAR = "calendar_fragment"
private const val TAG_MAP = "map_fragment"
private const val TAG_TODO = "todo_fragment"

class NaviActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_MAP, com.example.teamprojectlogin.MapFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calendarFragment -> setFragment(TAG_CALENDAR, CalendarFragment())
                R.id.mapFragment -> setFragment(TAG_MAP,
                    com.example.teamprojectlogin.MapFragment()
                )
                R.id.todoFragment -> setFragment(TAG_TODO, TodoFragment())
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

        val calendar = manager.findFragmentByTag(TAG_CALENDAR)
        val map = manager.findFragmentByTag(TAG_MAP)
        val todo = manager.findFragmentByTag(TAG_TODO)

        if (calendar != null) {
            fragTransaction.hide(calendar)
        }

        if (map != null) {
            fragTransaction.hide(map)
        }

        if (todo != null) {
            fragTransaction.hide(todo)
        }

        if (tag == TAG_CALENDAR) {
            if (calendar != null) {
                fragTransaction.show(calendar)
            }
        } else if (tag == TAG_MAP) {
            if (map != null) {
                fragTransaction.show(map)
            }
        } else if (tag == TAG_TODO) {
            if (todo != null) {
                fragTransaction.show(todo)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}
package io.github.reservationbytom

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_test.*

class Test : AppCompatActivity() {

    object LogicValues {
        const val Fragment1 = 0
        const val Fragment2 = 1
        const val Fragment3 = 2
    }

    private var currentItem: MenuItem? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                // message.setText(R.string.title_home) // 実際にここにFragmentが入る
                viewPager.setCurrentItem(LogicValues.Fragment1, false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                // message.setText(R.string.title_dashboard)
                viewPager.setCurrentItem(LogicValues.Fragment2, false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                // message.setText(R.string.title_notifications)
                viewPager.setCurrentItem(LogicValues.Fragment3, false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        setupViewPager()
    }

    private fun setupViewPager(){
        // 実際にはここから呼び出している
        val fragment =  mutableListOf( Example3Fragment(), Example3Fragment(),  Example3Fragment())
        val adapter = ViewPagerAdapter(fragment,supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2 // データの保持に関して
        viewPager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageSelected(position: Int) {
                // ここでいうcurrentItemは...
                if (currentItem != null) {
                    (currentItem as MenuItem).isChecked = false // ここでMenuItemが何をしているのかを確認したいところ
                } else {
                    bottomNavigationView.menu.getItem(0).isChecked = false //
                }
                bottomNavigationView.menu.getItem(position).isChecked = true
                currentItem = bottomNavigationView.menu.getItem(position) // ここでcurrentItemをput
            }
        })
    }
}


class ViewPagerAdapter(
    private val fragment: List<Fragment>,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getCount() = fragment.size

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }
}


class BottomNavigationViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}
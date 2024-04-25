package com.example.travenor.screen

import android.view.View
import com.example.travenor.R
import com.example.travenor.databinding.ActivityMainBinding
import com.example.travenor.screen.home.HomeFragment
import com.example.travenor.utils.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mHomeFragment: HomeFragment

    override fun getLayoutRoot(): View {
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun initView() {
        mBinding.buttonSearchMenu.setOnClickListener {
            mBinding.containerBottomNavigation.selectedItemId = R.id.menu_bottom_search
        }

        openHomeFragment()

        mBinding.containerBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bottom_home -> openHomeFragment()
                else -> removeHomeFragment()
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun openHomeFragment() {
        mHomeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().addToBackStack(HomeFragment::class.java.name)
            .replace(R.id.container_fragment, mHomeFragment).commit()
    }

    private fun removeHomeFragment() {
        supportFragmentManager.beginTransaction().remove(mHomeFragment).commit()
    }
}

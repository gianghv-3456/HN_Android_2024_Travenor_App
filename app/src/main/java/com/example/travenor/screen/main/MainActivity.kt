package com.example.travenor.screen.main

import android.view.View
import com.example.travenor.databinding.ActivityMainBinding
import com.example.travenor.utils.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun getLayoutRoot(): View {
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun initView() {
        // TODO(IMPLEMENT)
    }
}

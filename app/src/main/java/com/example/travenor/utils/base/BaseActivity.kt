package com.example.travenor.utils.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRoot())
        enableUIDrawOnSystemBar()
        initView()
        initData()
    }

    abstract fun getLayoutRoot(): View
    abstract fun initView()

    open fun initData() {
        /* no-op if not needed */
    }

    /**
     * Help layout can draw over system status bar
     */
    private fun enableUIDrawOnSystemBar() {
        val decorView: View = window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    fun setWhiteStatusBar() {
        val decorView: View = window.decorView
        decorView.systemUiVisibility =
            (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )
    }
}

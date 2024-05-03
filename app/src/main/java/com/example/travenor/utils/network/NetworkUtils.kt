package com.example.travenor.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.http.HttpResponseCache
import android.util.Log
import java.io.File
import java.io.IOException

object NetworkUtils {
    fun enableHttpResponseCache(cacheDir: File) {
        try {
            val httpCacheDir = File(cacheDir, "http")
            val httpCacheSize = HTTP_CACHE_SIZE
            HttpResponseCache.install(httpCacheDir, httpCacheSize)
        } catch (e: IOException) {
            Log.i(TAG, "HTTP response cache installation failed:$e")
        }
    }

    /**
     * Check that device network is available
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        val check = activeNetworkInfo != null && activeNetworkInfo.isConnected
        Log.d(TAG, "Network check: $check")
        return check
    }

    private const val HTTP_CACHE_SIZE = 10L * 1024 * 1024 // 10 MiB
    private const val TAG = "NetUtils"
}

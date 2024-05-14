package com.example.travenor.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.travenor.R
import com.example.travenor.data.source.local.sharedpreference.SharedPreferencesManager
import com.example.travenor.databinding.ActivityMainBinding
import com.example.travenor.screen.favorite.FavoriteFragment
import com.example.travenor.screen.home.HomeFragment
import com.example.travenor.screen.nearby.NearbyFragment
import com.example.travenor.screen.search.SearchFragment
import com.example.travenor.utils.base.BaseActivity
import com.example.travenor.utils.network.NetworkUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun getLayoutRoot(): View {
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun initView() {
        NetworkUtils.enableHttpResponseCache(cacheDir)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mBinding.buttonSearchMenu.setOnClickListener {
            mBinding.containerBottomNavigation.selectedItemId = R.id.menu_bottom_search
        }

        mBinding.containerBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bottom_home -> openHomeFragment()
                R.id.menu_bottom_favorite -> openFavoriteFragment()
                R.id.menu_bottom_search -> openSearchFragment()
                R.id.menu_bottom_nearby -> openNearbyFragment()
            }
            return@setOnItemSelectedListener true
        }

        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            // Permission already granted, start location updates
            requestLastLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                requestLastLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()

                // open HomeFragment anyway
                openHomeFragment()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLastLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            if (it != null) {
                saveLastLocation(it.latitude, it.longitude)
                openHomeFragment()
                Log.d(TAG, "Location: ${it.latitude}, ${it.longitude}")
            } else {
                Log.d(TAG, "Location: null")
                openHomeFragment()
            }
        }
    }

    private fun openSearchFragment() {
        val searchFragment = SearchFragment.newInstance()
        supportFragmentManager.beginTransaction().addToBackStack(SearchFragment::class.java.name)
            .replace(R.id.container_fragment, searchFragment).commit()
    }

    private fun openHomeFragment() {
        val homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().addToBackStack(HomeFragment::class.java.name)
            .replace(R.id.container_fragment, homeFragment).commit()
    }

    private fun openFavoriteFragment() {
        val favoriteFragment = FavoriteFragment.newInstance()
        supportFragmentManager.beginTransaction().addToBackStack(FavoriteFragment::class.java.name)
            .replace(R.id.container_fragment, favoriteFragment).commit()
    }

    private fun openNearbyFragment() {
        val nearbyFragment = NearbyFragment.newInstance()
        supportFragmentManager.beginTransaction().addToBackStack(NearbyFragment::class.java.name)
            .replace(R.id.container_fragment, nearbyFragment).commit()
    }

    private fun saveLastLocation(lat: Double, long: Double) {
        SharedPreferencesManager.getInstance(this).saveLocation(lat, long)
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 111
        private const val TAG = "MainActivity"
    }
}

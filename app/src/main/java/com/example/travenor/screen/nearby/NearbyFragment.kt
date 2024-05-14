package com.example.travenor.screen.nearby

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import com.example.travenor.R
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.databinding.FragmentNearbyBinding
import com.example.travenor.screen.nearby.adapter.CustomInfoWindowAdapter
import com.example.travenor.utils.base.BaseFragment
import com.example.travenor.utils.ext.getBitmap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class NearbyFragment : BaseFragment<FragmentNearbyBinding>(), OnMapReadyCallback,
    NearbyContract.View {
    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var mHomeLat = 0.0
    private var mHomeLng = 0.0
    private val customInfoWindowAdapter by lazy { CustomInfoWindowAdapter(getLayoutInflater()) }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentNearbyBinding {
        return FragmentNearbyBinding.inflate(inflater)
    }

    override fun initData() {
    }

    override fun initView() {
        setLayoutBelowSystemBar()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onGetNearbyAttractionSuccess(placePhotos: List<PlacePhoto>, places: List<Place>) {
        customInfoWindowAdapter.addData(places, placePhotos)
        places.forEach {
            addMarker(
                it.latitude.toDouble(),
                it.longitude.toDouble(),
                it.name,
                it.locationId,
                R.drawable.ic_location_pin_attraction
            )
        }
    }

    override fun onGetNearbyHotelSuccess(placePhotos: List<PlacePhoto>, places: List<Place>) {
        customInfoWindowAdapter.addData(places, placePhotos)
        places.forEach {
            addMarker(
                it.latitude.toDouble(),
                it.longitude.toDouble(),
                it.name,
                it.locationId,
                R.drawable.ic_location_pin_hotel
            )
        }
    }

    override fun onGetNearbyRestaurantSuccess(placePhotos: List<PlacePhoto>, places: List<Place>) {
        customInfoWindowAdapter.addData(places, placePhotos)
        places.forEach {
            addMarker(
                it.latitude.toDouble(),
                it.longitude.toDouble(),
                it.name,
                it.locationId,
                R.drawable.ic_location_pin_restaurant
            )
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        // map settings
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.isBuildingsEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.isMyLocationEnabled = true

        mMap.setInfoWindowAdapter(customInfoWindowAdapter)

        requestLastLocation()

        zoom(DEFAULT_ZOOM)
    }

    @SuppressLint("MissingPermission")
    private fun requestLastLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            if (it != null) {
                mHomeLat = it.latitude
                mHomeLng = it.longitude

                moveToLocation(mHomeLat, mHomeLng)
                addMarker(
                    mHomeLat,
                    mHomeLng,
                    "You", "",
                    R.drawable.ic_home_location
                )?.tag = CustomInfoWindowAdapter.TAG_NO_DISPLAY
            }
        }
    }

    private fun addMarker(
        lat: Double, lng: Double, title: String, snippet: String, drawableId: Int
    ): Marker? {
        val drawable = AppCompatResources.getDrawable(requireContext(), drawableId)

        if (drawable != null) {
            val bitmap = drawable.getBitmap(requireContext())
            val markerOpt = MarkerOptions().position(LatLng(lat, lng)).title(title).icon(
                BitmapDescriptorFactory.fromBitmap(bitmap)
            ).snippet(snippet).draggable(false)

            return mMap.addMarker(markerOpt)
        }
        return null
    }

    private fun moveToLocation(lat: Double, lng: Double) {
        val cameraUpdate = CameraUpdateFactory.newLatLng(LatLng(lat, lng))
        mMap.moveCamera(cameraUpdate)
    }

    private fun zoom(zoom: Float) {
        val cameraUpdateZoom = CameraUpdateFactory.zoomTo(zoom)
        mMap.moveCamera(cameraUpdateZoom)
    }

    private fun setLayoutBelowSystemBar() {
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.layoutAppBar) { v, insets ->
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = insets.systemWindowInsetTop
            v.layoutParams = layoutParams
            insets
        }
    }

    companion object {
        private const val DEFAULT_ZOOM = 13f
        private const val HOME_ZOOM = 11f

        @JvmStatic
        fun newInstance() = NearbyFragment()
    }
}
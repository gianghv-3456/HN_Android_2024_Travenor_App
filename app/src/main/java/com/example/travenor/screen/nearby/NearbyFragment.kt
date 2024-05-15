package com.example.travenor.screen.nearby

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import com.example.travenor.R
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.repository.NearbyRepositoryImpl
import com.example.travenor.data.repository.PlaceRepositoryImpl
import com.example.travenor.data.source.local.PlaceExploreLocalSource
import com.example.travenor.data.source.local.PlaceLocalDataSource
import com.example.travenor.data.source.remote.PlaceRemoteDataSource
import com.example.travenor.databinding.FragmentNearbyBinding
import com.example.travenor.screen.detail.DetailActivity
import com.example.travenor.screen.nearby.adapter.CustomInfoWindowAdapter
import com.example.travenor.utils.base.BaseFragment
import com.example.travenor.utils.ext.getBitmap
import com.example.travenor.utils.location.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("TooManyFunctions")
class NearbyFragment :
    BaseFragment<FragmentNearbyBinding>(),
    OnMapReadyCallback,
    OnInfoWindowClickListener,
    OnCameraMoveListener,
    NearbyContract.View {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var mPresenter: NearbyPresenter? = null

    private var mHomeLat = 0.0
    private var mHomeLng = 0.0
    private var mTargetLat = 0.0
    private var mTargetLng = 0.0

    private val customInfoWindowAdapter by lazy { CustomInfoWindowAdapter(getLayoutInflater()) }

    private val mMarkerList = mutableListOf<Marker>()
    private lateinit var mMap: GoogleMap

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentNearbyBinding {
        return FragmentNearbyBinding.inflate(inflater)
    }

    override fun initData() {
        val remoteDataSource = PlaceRemoteDataSource.getInstance()
        val localDataSource = PlaceLocalDataSource.getInstance(requireContext())
        val localExploreDataSource = PlaceExploreLocalSource.getInstance(requireContext())

        val placeRepository = PlaceRepositoryImpl.getInstance(
            remoteDataSource,
            localDataSource,
            localExploreDataSource
        )
        val nearbyRepository = NearbyRepositoryImpl.getInstance(remoteDataSource, localDataSource)

        mPresenter = NearbyPresenter(nearbyRepository, placeRepository)
        mPresenter?.setView(this)
    }

    override fun initView() {
        setLayoutBelowSystemBar()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewBinding.cardSearchAround.setOnClickListener {
            getNearbyPlace(mTargetLat, mTargetLng)
        }
    }

    override fun onGetNearbyAttractionSuccess(placePhotos: List<PlacePhoto>, places: List<Place>) {
        customInfoWindowAdapter.addData(places, placePhotos)
        places.forEach {
            val marker = addMarker(
                it.latitude.toDouble(),
                it.longitude.toDouble(),
                it.name,
                it.locationId,
                R.drawable.ic_location_pin_attraction
            )

            // Check duplicated marker
            if (marker != null) {
                if (mMarkerList.none { m -> m.snippet == marker.snippet }) {
                    mMarkerList.add(marker)
                }
            }

            marker?.tag = CustomInfoWindowAdapter.TAG_DISPLAY
        }
    }

    override fun onGetNearbyHotelSuccess(placePhotos: List<PlacePhoto>, places: List<Place>) {
        customInfoWindowAdapter.addData(places, placePhotos)
        places.forEach {
            val marker = addMarker(
                it.latitude.toDouble(),
                it.longitude.toDouble(),
                it.name,
                it.locationId,
                R.drawable.ic_location_pin_hotel
            )

            // Check duplicated marker
            if (marker != null) {
                if (mMarkerList.none { m -> m.snippet == marker.snippet }) {
                    mMarkerList.add(marker)
                }
            }

            marker?.tag = CustomInfoWindowAdapter.TAG_DISPLAY
        }
    }

    override fun onGetNearbyRestaurantSuccess(placePhotos: List<PlacePhoto>, places: List<Place>) {
        customInfoWindowAdapter.addData(places, placePhotos)

        places.forEach {
            val marker = addMarker(
                it.latitude.toDouble(),
                it.longitude.toDouble(),
                it.name,
                it.locationId,
                R.drawable.ic_location_pin_restaurant
            )

            // Check duplicated marker
            if (marker != null) {
                if (mMarkerList.none { m -> m.snippet == marker.snippet }) {
                    mMarkerList.add(marker)
                }
            }

            marker?.tag = CustomInfoWindowAdapter.TAG_DISPLAY
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        configureMap(mMap)
        requestLastLocation()

        val cameraUpdateZoom = CameraUpdateFactory.zoomTo(DEFAULT_ZOOM)
        mMap.moveCamera(cameraUpdateZoom)
    }

    override fun onInfoWindowClick(p0: Marker) {
        if (p0.tag.toString() == CustomInfoWindowAdapter.TAG_NO_DISPLAY) return

        mMap.animateCamera(CameraUpdateFactory.newLatLng(p0.position))
        openPlaceDetailScreen(p0.snippet.toString())
    }

    override fun onCameraMove() {
        val latLng = mMap.cameraPosition.target
        val distance =
            LocationUtils.calculateDistance(latLng.latitude, latLng.longitude, mHomeLat, mHomeLng)
        mTargetLat = latLng.latitude
        mTargetLng = latLng.longitude

        if (distance > FIND_AROUND_THRESHOLD_DISTANCE_FROM_HOME) {
            viewBinding.cardSearchAround.visibility = View.VISIBLE
        } else {
            viewBinding.cardSearchAround.visibility = View.GONE
        }
    }

    private fun configureMap(map: GoogleMap) {
        // map settings
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.isBuildingsEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isRotateGesturesEnabled = true
        map.uiSettings.isTiltGesturesEnabled = true

        map.setInfoWindowAdapter(customInfoWindowAdapter)
        map.setOnInfoWindowClickListener(this)
        map.setOnCameraMoveListener(this)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }
    }

    private fun requestLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.setData(uri)
            startActivity(intent)
            return
        }

        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            if (it != null) {
                mHomeLat = it.latitude
                mHomeLng = it.longitude

                moveToLocation(it.latitude, it.longitude)

                // Add marker for home
                addMarker(
                    it.latitude,
                    it.longitude,
                    "You",
                    "",
                    R.drawable.ic_home_location
                )?.tag = CustomInfoWindowAdapter.TAG_NO_DISPLAY

                getNearbyPlace(mHomeLat, mHomeLng)
            }
        }
    }

    private fun getNearbyPlace(lat: Double, lng: Double) {
        mPresenter?.getNearbyAttraction(lat, lng)
        mPresenter?.getNearbyHotel(lat, lng)
        mPresenter?.getNearbyRestaurant(lat, lng)
    }

    private fun addMarker(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String,
        drawableId: Int
    ): Marker? {
        val drawable = AppCompatResources.getDrawable(requireContext(), drawableId)

        if (drawable != null) {
            val bitmap = drawable.getBitmap()
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

    private fun setLayoutBelowSystemBar() {
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.layoutAppBar) { v, insets ->
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = insets.systemWindowInsetTop
            v.layoutParams = layoutParams
            insets
        }
    }

    private fun openPlaceDetailScreen(placeId: String) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.KEY_INTENT_PLACE_ID, placeId)
        startActivity(intent)
    }

    companion object {
        private const val DEFAULT_ZOOM = 12f
        private const val FIND_AROUND_THRESHOLD_DISTANCE_FROM_HOME = 6000

        @JvmStatic
        fun newInstance() = NearbyFragment()
    }
}

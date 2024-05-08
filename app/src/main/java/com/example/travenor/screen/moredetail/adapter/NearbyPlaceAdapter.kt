package com.example.travenor.screen.moredetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.databinding.ItemNearbyPlaceBinding
import com.example.travenor.utils.location.LocationUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class NearbyPlaceAdapter(context: Context) : RecyclerView.Adapter<NearbyPlaceAdapter.ViewHolder>() {
    private var placeData: List<Place> = emptyList()
    private var placePhotos: MutableList<PlacePhoto> = mutableListOf()
    private var targetLat: Double = 0.0
    private var targetLong: Double = 0.0

    fun setTargetLocation(lat: Double, long: Double) {
        targetLat = lat
        targetLong = long
    }

    fun setPlaces(places: List<Place>) {
        placeData = places
        notifyDataSetChanged()
    }

    fun loadImage(photo: List<PlacePhoto>) {
        placePhotos.addAll(photo)
        for (p in photo) {
            var position = 0
            for (place in placeData) {
                if (place.locationId == p.locationId) {
                    position = placeData.indexOf(place)
                    break
                }
            }
            notifyItemChanged(position)
        }
    }

    class ViewHolder(val binding: ItemNearbyPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNearbyPlaceBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return placeData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val place = placeData[position]

        binding.textName.text = place.name
        binding.textAddress.text = place.addressObj.getAddress()
        binding.textRating.text = place.rating.toString()

        // Calculate distance. This distance is in meters. Convert it to kilometers.
        val distance: Double = LocationUtils.calculateDistance(
            targetLat,
            targetLong,
            place.latitude.toDouble(),
            place.longitude.toDouble()
        ) / 1000

        println("Target: $targetLat, $targetLong")
        println("Place: ${place.latitude}, ${place.longitude}")

        val roundedDistance:Int = distance.roundToInt()
        println("DISTANCE")
        println(roundedDistance)
        binding.textDistance.text = "$roundedDistance km"
    }
}
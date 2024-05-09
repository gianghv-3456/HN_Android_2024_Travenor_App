package com.example.travenor.utils.location

import android.location.Location
import com.example.travenor.constant.NEARBY_DISTANCE_IN_METERS
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LocationUtils {

    /**
     * Generate random location for explore place around user
     */
    fun generateRandomLocation(centerLat: Double, centerLong: Double): Pair<Double, Double> {
        return generateRandomLocationAround(centerLat, centerLong, NEARBY_DISTANCE_IN_METERS)
    }

    /**
     * Generates a random location within a given radius from a given center location.
     * @return A pair of latitude and longitude values within the given radius from the center location.
     */
    fun generateRandomLocationAround(
        centerLat: Double,
        centerLong: Double,
        radius: Double
    ): Pair<Double, Double> {
        // Convert center location to cartesian coordinates
        val cartesianCenterX = centerLat * Math.PI / DEGREE_180
        val cartesianCenterY = centerLong * Math.PI / DEGREE_180

        val r = sqrt(Math.random()) * radius
        val theta = Math.random() * 2 * Math.PI

        val dx = r * cos(theta)
        val dy = r * sin(theta)

        val newX = dx / EARTH_RADIUS + cartesianCenterX
        val newY = dy / EARTH_RADIUS + cartesianCenterY

        // Convert back to degrees
        val newLat = newX * DEGREE_180 / Math.PI
        val newLong = newY * DEGREE_180 / Math.PI

        return Pair(newLat, newLong)
    }

    /**
     * Distance between two location in double number
     */
    fun calculateDistance(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val l1 = Location("A")
        l1.latitude = lat1
        l1.longitude = long1
        val l2 = Location("B")
        l2.latitude = lat2
        l2.longitude = long2
        return l1.distanceTo(l2).toDouble()
    }

    // Earth radius in meters
    private const val EARTH_RADIUS = 6371000.0
    private const val DEGREE_180 = 180
}

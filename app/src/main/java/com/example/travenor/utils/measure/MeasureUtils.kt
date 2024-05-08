package com.example.travenor.utils.measure

object MeasureUtils {
    private const val MILE_TO_KM_FACTOR = 1.609344
    fun mileToKm(mile: Double): Double {
        return mile * MILE_TO_KM_FACTOR
    }

}
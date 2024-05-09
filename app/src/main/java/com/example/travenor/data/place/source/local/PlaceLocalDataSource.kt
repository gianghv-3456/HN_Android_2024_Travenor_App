package com.example.travenor.data.place.source.local

import android.content.Context
import android.database.sqlite.SQLiteException
import com.example.travenor.constant.PlaceCategory
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.source.PlaceSource
import com.example.travenor.data.place.sqlite.dao.PlaceDAO
import com.example.travenor.data.place.sqlite.dao.PlacePhotoDAO

class PlaceLocalDataSource(
    private val placeDao: PlaceDAO,
    private val placePhotoDAO: PlacePhotoDAO

) : PlaceSource.Local {

    override fun getPlaceDetail(locationId: String): Place? {
        try {
            return placeDao.getPlaceData(locationId)
        } catch (e: SQLiteException) {
            e.run { printStackTrace() }
            return null
        }
    }

    override fun getPlacePhoto(locationId: String): List<PlacePhoto>? {
        try {
            return placePhotoDAO.getPhotoData(locationId)
        } catch (e: SQLiteException) {
            e.run { printStackTrace() }
            return null
        }
    }

    override fun savePlaceDetail(place: Place) {
        try {
            placeDao.insertPlace(place)
        } catch (e: SQLiteException) {
            e.run { printStackTrace() }
        }
    }

    override fun savePlacePhoto(placePhotos: List<PlacePhoto>) {
        try {
            placePhotos.forEach {
                placePhotoDAO.insertPhoto(it)
            }
        } catch (e: SQLiteException) {
            e.run { printStackTrace() }
        }
    }

    override fun savePlaceAddress(place: Place) {
        try {
            placeDao.insertPlaceAddress(place.addressObj, place.locationId)
        } catch (e: SQLiteException) {
            e.run { printStackTrace() }
        }
    }

    override fun getNearbyPlaceLocal(
        lat: Double,
        long: Double,
        category: PlaceCategory,
        limit: Int,
        radius: Double
    ): List<Place>? {
        return try {
            val result = placeDao.getNearByPlace(lat, long, limit, radius, category)
            result.ifEmpty { null }
        } catch (e: SQLiteException) {
            e.run { printStackTrace() }
            null
        }
    }

    companion object {
        private var instance: PlaceLocalDataSource? = null

        fun getInstance(context: Context) = synchronized(this) {
            val placeDao = PlaceDAO(context)
            val placePhotoDAO = PlacePhotoDAO(context)
            instance ?: PlaceLocalDataSource(
                placeDao,
                placePhotoDAO
            ).also { instance = it }
        }
    }
}

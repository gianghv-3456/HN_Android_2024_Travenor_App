package com.example.travenor.data.source.local.database.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.travenor.data.model.photo.Image
import com.example.travenor.data.model.photo.ImageList
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.source.local.database.PlaceSqliteHelper
import com.example.travenor.data.source.local.database.table.PlacePhotoTable

class PlacePhotoDAO(context: Context) {
    private val helper = PlaceSqliteHelper(context)

    private fun createPhotoValues(photo: PlacePhoto): ContentValues {
        val values = ContentValues().apply {
            put(PlacePhotoTable.COL_ID, photo.id)
            put(PlacePhotoTable.COL_CAPTION, photo.caption)
            put(PlacePhotoTable.COL_LOCATION_ID, photo.locationId)

            if (photo.imageList.original != null) {
                put(PlacePhotoTable.COL_URL, photo.imageList.original.url)
            } else if (photo.imageList.large != null) {
                put(PlacePhotoTable.COL_URL, photo.imageList.large.url)
            } else if (photo.imageList.medium != null) {
                put(PlacePhotoTable.COL_URL, photo.imageList.medium.url)
            } else if (photo.imageList.small != null) {
                put(PlacePhotoTable.COL_URL, photo.imageList.small.url)
            } else {
                put(PlacePhotoTable.COL_URL, photo.imageList.thumbnail?.url)
            }

            put(PlacePhotoTable.COL_ORIGIN_WIDTH, photo.imageList.original?.width)
            put(PlacePhotoTable.COL_ORIGIN_HEIGHT, photo.imageList.original?.height)
        }
        return values
    }

    fun insertPhoto(photo: PlacePhoto): Boolean {
        val values = createPhotoValues(photo)
        val falseCode = -1L

        val db = helper.writableDatabase
        val result = db.insertWithOnConflict(
            PlacePhotoTable.TABLE_PLACE_PHOTO,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )

        return if (result == falseCode) {
            Log.d(TAG, "Fail to insert data")
            false
        } else {
            Log.d(TAG, "Insert success")
            true
        }
    }

    fun updatePhoto(photo: PlacePhoto, locationId: String): Boolean {
        val values = createPhotoValues(photo)

        val db = helper.writableDatabase
        val whereString = "${PlacePhotoTable.COL_LOCATION_ID} =? "

        // True if number of effected row more than 0
        return db.update(
            PlacePhotoTable.TABLE_PLACE_PHOTO,
            values,
            whereString,
            arrayOf(locationId)
        ) > 0
    }

    fun getPhotoData(locationId: String): List<PlacePhoto>? {
        val db = helper.readableDatabase
        val columns = arrayOf(
            PlacePhotoTable.COL_ID,
            PlacePhotoTable.COL_CAPTION,
            PlacePhotoTable.COL_URL,
            PlacePhotoTable.COL_ORIGIN_WIDTH,
            PlacePhotoTable.COL_ORIGIN_HEIGHT
        )
        val selectionString = "${PlacePhotoTable.COL_LOCATION_ID} =? "
        val args = arrayOf(locationId)

        val cursor = db.query(
            PlacePhotoTable.TABLE_PLACE_PHOTO,
            columns,
            selectionString,
            args,
            null,
            null,
            null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val result = mutableListOf<PlacePhoto>()

            do {
                var columnIndex = 0

                val id = cursor.getString(columnIndex++).orEmpty()
                val caption = cursor.getString(columnIndex++).orEmpty()
                val url = cursor.getString(columnIndex++).orEmpty()
                val originWidth = cursor.getInt(columnIndex++)
                val originHeight = cursor.getInt(columnIndex)

                // create ImageList for imageList field of PlacePhoto object
                val thumbnail = Image(originHeight, originWidth, url)
                val small = Image(originHeight, originWidth, url)
                val medium = Image(originHeight, originWidth, url)
                val large = Image(originHeight, originWidth, url)
                val origin = Image(originHeight, originWidth, url)
                val imageList = ImageList(thumbnail, small, medium, large, origin)

                val photo = PlacePhoto(id, locationId, caption, imageList)
                result.add(photo)
            } while (cursor.moveToNext())

            cursor.close()
            return result
        }
        return null
    }

    companion object {
        private const val TAG = "com.example.travenor.dao.place_photo"
    }
}

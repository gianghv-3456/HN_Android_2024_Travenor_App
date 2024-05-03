package com.example.travenor.data.place.sqlite.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.travenor.data.model.place.Address
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.sqlite.PlaceSqliteHelper
import com.example.travenor.data.place.sqlite.tables.AddressTable
import com.example.travenor.data.place.sqlite.tables.PlaceTable

class PlaceDAO(context: Context) {
    private val sqlHelper = PlaceSqliteHelper(context)

    /**
     * Insert place into DB
     */
    fun insertPlace(place: Place): Boolean {
        val values = createPlaceContentValue(place)
        val falseCode = -1L

        val db = sqlHelper.writableDatabase
        val result = db.insertWithOnConflict(
            PlaceTable.TABLE_PLACE,
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

    /**
     * Update a place's data follow locationId key
     * @param locationId location Id
     * @return int value
     */
    fun updatePlaceData(locationId: String, newData: Place): Int {
        val values = createPlaceContentValue(newData)

        val db = sqlHelper.writableDatabase
        val whereString = "${PlaceTable.COL_LOCATION_ID} =? "
        return db.update(PlaceTable.TABLE_PLACE, values, whereString, arrayOf(locationId))
    }

    fun getPlaceData(locationId: String): Place? {
        val db = sqlHelper.readableDatabase
        val columns = arrayOf(
            PlaceTable.COL_NAME,
            PlaceTable.COL_DESCRIPTION,
            PlaceTable.COL_WEB_URL,
            PlaceTable.COL_LATITUDE,
            PlaceTable.COL_LONGITUDE,
            PlaceTable.COL_RATING,
            PlaceTable.COL_RATING_AMOUNT,
            PlaceTable.COL_PHOTO_COUNT,
            PlaceTable.COL_PLACE_CATEGORY,
            PlaceTable.COL_IS_FAVORITE
        )
        val selectionString = "${PlaceTable.COL_LOCATION_ID} =? "
        val args = arrayOf(locationId)

        val cursor = db.query(
            PlaceTable.TABLE_PLACE,
            columns,
            selectionString,
            args,
            null,
            null,
            null
        )

        var columnIndex = 0
        if (cursor != null && cursor.moveToFirst()) {
            // Get data from cursor
            val name = cursor.getString(columnIndex++).orEmpty()
            val desc = cursor.getString(columnIndex++).orEmpty()
            val webUrl = cursor.getString(columnIndex++).orEmpty()
            val latitude = cursor.getFloat(columnIndex++)
            val longitude = cursor.getFloat(columnIndex++)
            val rating = cursor.getFloat(columnIndex++)
            val ratingAmount = cursor.getInt(columnIndex++)
            val photoCount = cursor.getInt(columnIndex++)
            val placeCategory = cursor.getString(columnIndex++).orEmpty()
            val isFavorite = cursor.getInt(columnIndex)

            cursor.close()

            // Address field is an Object, so query from address table before
            val address = getPlaceAddress(locationId)
            if (address != null) {
                return Place(
                    locationId,
                    name,
                    desc,
                    webUrl,
                    address,
                    latitude,
                    longitude,
                    rating,
                    ratingAmount,
                    photoCount,
                    "",
                    placeCategory,
                    isFavorite
                )
            }
        }
        return null
    }

    /**
     * Query Place Address from DB
     *
     */
    private fun getPlaceAddress(locationId: String): Address? {
        val selectionString = "${AddressTable.COL_LOCATION_ID} =? "
        val db = sqlHelper.readableDatabase
        val column = arrayOf(
            AddressTable.COL_ADDRESS_STRING,
            AddressTable.COL_COUNTRY,
            AddressTable.COL_CITY,
            AddressTable.COL_STATE,
            AddressTable.COL_STREET,
            AddressTable.COL_POSTAL_CODE
        )

        val cursor = db.query(
            AddressTable.TABLE_ADDRESS,
            column,
            selectionString,
            arrayOf(locationId),
            null,
            null,
            null
        )

        var columnIndex = 0
        return if (cursor != null && cursor.moveToFirst()) {
            val addressString = cursor.getString(columnIndex++).orEmpty()
            val country = cursor.getString(columnIndex++).orEmpty()
            val city = cursor.getString(columnIndex++).orEmpty()
            val state = cursor.getString(columnIndex++).orEmpty()
            val street = cursor.getString(columnIndex++).orEmpty()
            val postalCode = cursor.getString(columnIndex).orEmpty()

            cursor.close()
            Address(street, city, state, country, postalCode, addressString)
        } else {
            null
        }
    }

    fun insertPlaceAddress(address: Address, locationId: String): Boolean {
        val values = ContentValues().apply {
            put(AddressTable.COL_LOCATION_ID, locationId)
            put(AddressTable.COL_COUNTRY, address.country)
            put(AddressTable.COL_STATE, address.state)
            put(AddressTable.COL_CITY, address.city)
            put(AddressTable.COL_STREET, address.street1)
            put(AddressTable.COL_ADDRESS_STRING, address.addressString)
            put(AddressTable.COL_POSTAL_CODE, address.postalCode)
        }
        val db = sqlHelper.writableDatabase
        val result = db.insertWithOnConflict(
            AddressTable.TABLE_ADDRESS,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        return result != -1L
    }

    /**
     * Create content value object for Place
     */
    private fun createPlaceContentValue(place: Place): ContentValues {
        val values = ContentValues()
        values.put(PlaceTable.COL_LOCATION_ID, place.locationId)
        values.put(PlaceTable.COL_NAME, place.name)
        values.put(PlaceTable.COL_DESCRIPTION, place.description)
        values.put(PlaceTable.COL_WEB_URL, place.webUrl)
        values.put(PlaceTable.COL_ADDRESS_STRING, place.addressObj.addressString)
        values.put(PlaceTable.COL_LATITUDE, place.latitude)
        values.put(PlaceTable.COL_LONGITUDE, place.longitude)
        values.put(PlaceTable.COL_RATING, place.rating)
        values.put(PlaceTable.COL_RATING_AMOUNT, place.ratingAmount)
        values.put(PlaceTable.COL_PHOTO_COUNT, place.photoAmount)
        values.put(PlaceTable.COL_PLACE_CATEGORY, place.locationType)
        return values
    }

    companion object {
        private const val TAG = "com.example.travenor.dao.place"
    }
}

package com.example.travenor.data.place.sqlite.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.travenor.constant.PlaceCategory
import com.example.travenor.data.place.sqlite.PlaceSqliteHelper
import com.example.travenor.data.place.sqlite.tables.ExploreHotelTable
import com.example.travenor.data.place.sqlite.tables.ExplorePlaceTable
import com.example.travenor.data.place.sqlite.tables.ExploreRestaurantTable

class PlaceExploreDAO(context: Context) {
    private val sqlHelper = PlaceSqliteHelper(context)

    fun saveExploreAttraction(locationId: String, timestamp: Long): Boolean {
        val db = sqlHelper.writableDatabase
        val values = ContentValues().apply {
            put(ExplorePlaceTable.COL_LOCATION_ID, locationId)
            put(ExplorePlaceTable.COL_TIMESTAMP, timestamp)
        }

        val result = db.insertWithOnConflict(
            ExplorePlaceTable.EXPLORE_PLACE,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        return result != -1L
    }

    fun saveExploreRestaurant(locationId: String, timestamp: Long): Boolean {
        val db = sqlHelper.writableDatabase
        val values = ContentValues().apply {
            put(ExploreRestaurantTable.COL_LOCATION_ID, locationId)
            put(ExploreRestaurantTable.COL_TIMESTAMP, timestamp)
        }

        val result = db.insertWithOnConflict(
            ExploreRestaurantTable.EXPLORE_RESTAURANT,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )

        return result != -1L
    }

    fun saveExploreHotel(locationId: String, timestamp: Long): Boolean {
        val db = sqlHelper.writableDatabase
        val values = ContentValues().apply {
            put(ExploreHotelTable.COL_LOCATION_ID, locationId)
            put(ExploreHotelTable.COL_TIMESTAMP, timestamp)
        }

        val result = db.insertWithOnConflict(
            ExploreHotelTable.EXPLORE_HOTEL,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        return result != -1L
    }

    /**
     * Get attraction explore that some attraction stored in DB, query random 5 attraction place
     * @return locationIds
     */
    fun getAttractionExplore(limit: Int = 5): List<Pair<String, Long>> {
        return getExplorePlace(limit, PlaceCategory.ATTRACTIONS)
    }

    /**
     * Get restaurant explore that some restaurant stored in DB, query random 5 restaurant place
     * @return locationIds
     */
    fun getRestaurantExplore(limit: Int = 5): List<Pair<String, Long>> {
        return getExplorePlace(limit, PlaceCategory.RESTAURANTS)
    }

    /**
     * Get hotel explore that some hotel stored in DB, query random 5 hotel place
     * @return locationIds
     */
    fun getHotelExplore(limit: Int = 5): List<Pair<String, Long>> {
        return getExplorePlace(limit, PlaceCategory.HOTELS)
    }

    /**
     * Helper method
     * Get explore place that stored in DB.
     */
    private fun getExplorePlace(
        limit: Int = 5,
        locationType: PlaceCategory
    ): List<Pair<String, Long>> {
        val db = sqlHelper.readableDatabase
        val columns = getQueryColumn(locationType)
        val threeWeekAgoTimePointInMillisecond =
            System.currentTimeMillis() - THREE_WEEK_IN_MILLISECONDS
        val args = arrayOf(threeWeekAgoTimePointInMillisecond.toString())

        val whereString = queryConditionsString(locationType)
        val cursor = db.query(
            getTableName(locationType),
            columns,
            whereString,
            args,
            null,
            null,
            null
        )
        val result = mutableListOf<Pair<String, Long>>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get locationId
                val locationId = cursor.getString(0).orEmpty()
                // Get timestamp
                val timestamp = cursor.getLong(1)

                if (locationId.isNotEmpty()) {
                    result.add(Pair(locationId, timestamp))
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        result.shuffle()
        return result.take(limit)
    }

    private fun queryConditionsString(locationType: PlaceCategory): String {
        return when (locationType) {
            PlaceCategory.ATTRACTIONS -> "${ExplorePlaceTable.COL_TIMESTAMP} >?"
            PlaceCategory.RESTAURANTS -> "${ExploreRestaurantTable.COL_TIMESTAMP} >?"
            PlaceCategory.HOTELS -> "${ExploreHotelTable.COL_TIMESTAMP} >?"
        }
    }

    private fun getTableName(locationType: PlaceCategory): String {
        return when (locationType) {
            PlaceCategory.ATTRACTIONS -> ExplorePlaceTable.EXPLORE_PLACE
            PlaceCategory.RESTAURANTS -> ExploreRestaurantTable.EXPLORE_RESTAURANT
            PlaceCategory.HOTELS -> ExploreHotelTable.EXPLORE_HOTEL
        }
    }

    private fun getQueryColumn(locationType: PlaceCategory): Array<String> {
        return when (locationType) {
            PlaceCategory.ATTRACTIONS -> arrayOf(
                ExplorePlaceTable.COL_LOCATION_ID,
                ExplorePlaceTable.COL_TIMESTAMP
            )

            PlaceCategory.RESTAURANTS -> arrayOf(
                ExploreRestaurantTable.COL_LOCATION_ID,
                ExploreRestaurantTable.COL_TIMESTAMP
            )

            PlaceCategory.HOTELS -> arrayOf(
                ExploreHotelTable.COL_LOCATION_ID,
                ExploreHotelTable.COL_TIMESTAMP
            )
        }
    }

    companion object {
        private const val THREE_WEEK_IN_MILLISECONDS = 1000 * 60 * 60 * 24 * 7 * 3
    }
}

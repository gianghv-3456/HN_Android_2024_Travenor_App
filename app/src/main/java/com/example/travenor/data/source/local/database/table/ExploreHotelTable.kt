package com.example.travenor.data.source.local.database.table

object ExploreHotelTable {
    const val EXPLORE_HOTEL = "explore_hotel"
    const val COL_ID = "id"
    const val COL_LOCATION_ID = "location_id"
    const val COL_TIMESTAMP = "timestamp"

    const val QUERY_CREATE_TABLE = "CREATE TABLE $EXPLORE_HOTEL (" +
        "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
        "$COL_LOCATION_ID TEXT UNIQUE," +
        "$COL_TIMESTAMP INTEGER" +
        ");"
}

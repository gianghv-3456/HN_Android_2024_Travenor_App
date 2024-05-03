package com.example.travenor.data.place.sqlite.tables

object PlaceTable {
    const val TABLE_PLACE = "place"
    const val COL_LOCATION_ID = "place_location_id"
    const val COL_NAME = "place_name"
    const val COL_DESCRIPTION = "place_desc"
    const val COL_WEB_URL = "place_web_url"
    const val COL_ADDRESS_STRING = "place_address_string"
    const val COL_LATITUDE = "place_latitude"
    const val COL_LONGITUDE = "place_longitude"
    const val COL_RATING = "place_rating"
    const val COL_RATING_AMOUNT = "place_rating_amount"
    const val COL_PHOTO_COUNT = "place_photo_count"
    const val COL_PLACE_CATEGORY = "place_category"
    const val COL_IS_FAVORITE = "place_is_favorite"
    const val COL_TIMESTAMP = "timestamp"

    const val QUERY_CREATE_PLACE_DB = "CREATE TABLE $TABLE_PLACE (" +
        "$COL_LOCATION_ID TEXT PRIMARY KEY," +
        "$COL_NAME TEXT," +
        "$COL_DESCRIPTION TEXT," +
        "$COL_WEB_URL TEXT," +
        "$COL_ADDRESS_STRING TEXT," +
        "$COL_LATITUDE REAL," +
        "$COL_LONGITUDE REAL," +
        "$COL_RATING REAL," +
        "$COL_RATING_AMOUNT INTEGER," +
        "$COL_PHOTO_COUNT INTEGER," +
        "$COL_PLACE_CATEGORY TEXT," +
        "$COL_IS_FAVORITE INTEGER," +
        "$COL_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");"
}

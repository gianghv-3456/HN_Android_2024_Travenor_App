package com.example.travenor.data.source.local.database.table

object PlacePhotoTable {
    const val TABLE_PLACE_PHOTO = "place_photo"
    const val COL_ID = "photo_id"
    const val COL_LOCATION_ID = "photo_location_id"
    const val COL_CAPTION = "photo_caption"
    const val COL_ORIGIN_HEIGHT = "photo_origin_height"
    const val COL_ORIGIN_WIDTH = "photo_origin_width"
    const val COL_URL = "photo_url"
    const val COL_TIMESTAMP = "timestamp"

    const val QUERY_CREATE_PLACE_PHOTO_DB = "CREATE TABLE $TABLE_PLACE_PHOTO (" +
        "$COL_ID TEXT PRIMARY KEY," +
        "$COL_LOCATION_ID TEXT," +
        "$COL_CAPTION TEXT," +
        "$COL_ORIGIN_HEIGHT INTEGER," +
        "$COL_ORIGIN_WIDTH INTEGER," +
        "$COL_URL TEXT," +
        "$COL_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "FOREIGN KEY ($COL_LOCATION_ID) REFERENCES ${PlaceTable.TABLE_PLACE} (${PlaceTable.COL_LOCATION_ID})" +
        ");"
}

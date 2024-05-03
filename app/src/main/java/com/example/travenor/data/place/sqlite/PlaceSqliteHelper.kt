package com.example.travenor.data.place.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.travenor.data.place.sqlite.tables.AddressTable
import com.example.travenor.data.place.sqlite.tables.ExploreHotelTable
import com.example.travenor.data.place.sqlite.tables.ExplorePlaceTable
import com.example.travenor.data.place.sqlite.tables.ExploreRestaurantTable
import com.example.travenor.data.place.sqlite.tables.PlacePhotoTable
import com.example.travenor.data.place.sqlite.tables.PlaceTable

class PlaceSqliteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        try {
            p0?.execSQL(PlaceTable.QUERY_CREATE_PLACE_DB)
            p0?.execSQL(PlacePhotoTable.QUERY_CREATE_PLACE_PHOTO_DB)
            p0?.execSQL(AddressTable.QUERY_CREATE_ADDRESS)
            p0?.execSQL(ExplorePlaceTable.QUERY_CREATE_TABLE)
            p0?.execSQL(ExploreRestaurantTable.QUERY_CREATE_TABLE)
            p0?.execSQL(ExploreHotelTable.QUERY_CREATE_TABLE)
        } catch (e: SQLiteException) {
            e.run { printStackTrace() }
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS " + PlaceTable.TABLE_PLACE)
        p0?.execSQL("DROP TABLE IF EXISTS " + PlacePhotoTable.TABLE_PLACE_PHOTO)
        p0?.execSQL("DROP TABLE IF EXISTS " + AddressTable.TABLE_ADDRESS)
        p0?.execSQL("DROP TABLE IF EXISTS " + ExplorePlaceTable.EXPLORE_PLACE)
        p0?.execSQL("DROP TABLE IF EXISTS " + ExploreRestaurantTable.EXPLORE_RESTAURANT)
        p0?.execSQL("DROP TABLE IF EXISTS " + ExploreHotelTable.EXPLORE_HOTEL)
        onCreate(p0)
    }

    companion object {
        private const val DATABASE_NAME = "com.example.travenor.db"
        private const val DATABASE_VERSION = 1
    }
}

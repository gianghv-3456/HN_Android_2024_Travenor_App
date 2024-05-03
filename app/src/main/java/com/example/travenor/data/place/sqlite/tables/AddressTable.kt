package com.example.travenor.data.place.sqlite.tables

object AddressTable {
    const val TABLE_ADDRESS = "address"
    const val COL_ID = "address_id"
    const val COL_LOCATION_ID = "location_id"
    const val COL_COUNTRY = "country"
    const val COL_STATE = "state"
    const val COL_CITY = "city"
    const val COL_STREET = "street_1"
    const val COL_ADDRESS_STRING = "address_string"
    const val COL_POSTAL_CODE = "postalcode"
    const val TIME_STAMP = "timestamp"

    const val QUERY_CREATE_ADDRESS = "CREATE TABLE $TABLE_ADDRESS (" +
        "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
        "$COL_LOCATION_ID TEXT UNIQUE," +
        "$COL_COUNTRY TEXT," +
        "$COL_STATE TEXT," +
        "$COL_CITY TEXT," +
        "$COL_STREET TEXT," +
        "$COL_ADDRESS_STRING TEXT," +
        "$COL_POSTAL_CODE TEXT," + "$TIME_STAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "FOREIGN KEY ($COL_LOCATION_ID) REFERENCES ${PlaceTable.TABLE_PLACE} (${PlaceTable.COL_LOCATION_ID})" +
        ");"
}

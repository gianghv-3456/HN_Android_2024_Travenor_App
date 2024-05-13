package com.example.travenor.data.source.local.database.table

object RecentSearchTable {
    const val TABLE_NAME = "recent_search"
    const val COL_ID = "id"
    const val COL_SEARCH_QUERY = "search_query"
    const val QUERY_CREATE_RECENT_SEARCH_DB = "CREATE TABLE $TABLE_NAME (" +
            "$COL_ID PRIMARY KEY AUTOINCREMENT," +
            "$COL_SEARCH_QUERY TEXT)"
}
package com.example.travenor.constant

const val FOURSQUARE_KEY_NAME = "foursquare_key"
const val RAPID_KEY_NAME = "rapid_key"
const val TRIP_ADVISOR_KEY_NAME = "trip_advisor_key"

val foursquareApiKey = System.getProperty(FOURSQUARE_KEY_NAME)
val rapidApiKey = System.getProperty(RAPID_KEY_NAME)
val tripAdvisorApiKey = System.getProperty(TRIP_ADVISOR_KEY_NAME)

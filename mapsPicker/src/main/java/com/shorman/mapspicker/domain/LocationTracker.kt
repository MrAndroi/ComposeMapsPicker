package com.shorman.mapspicker.domain

import android.location.Location

internal interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}
package com.shorman.mapspicker.data.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.shorman.mapspicker.data.DefaultLocationTracker
import com.shorman.mapspicker.domain.LocationTracker

internal object LocationModule {

    fun providesFusedLocationProviderClient(
        context: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        context: Context
    ): LocationTracker = DefaultLocationTracker(
        fusedLocationProviderClient = fusedLocationProviderClient,
        context = context
    )
}
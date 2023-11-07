package com.shorman.mapspicker.presentation.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.animation.flyTo
import com.shorman.mapspicker.presentation.model.LocationInfoLanguage
import com.shorman.mapspicker.presentation.model.UserLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

internal fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    ContextCompat.startActivity(this, intent, null)
}

internal fun CameraPositionState.moveToLocation(latLng: LatLng) {
    this.move(
        CameraUpdateFactory.newCameraPosition(
            CameraPosition(
                LatLng(latLng.latitude, latLng.longitude),
                20f,
                20f,
                20f,
            )
        )
    )
}

internal suspend fun CameraPositionState.animateToLocation(latLng: LatLng) {
    this.animate(
        CameraUpdateFactory.newCameraPosition(
            CameraPosition(
                LatLng(latLng.latitude, latLng.longitude),
                20f,
                20f,
                20f,
            )
        )
    )
}

internal fun MapboxMap.animateToLocation(latLng: LatLng) {
    this.flyTo(
        CameraOptions.Builder()
            .zoom(16.0)
            .center(Point.fromLngLat(latLng.longitude, latLng.latitude))
            .build()
    )
}

internal fun MapboxMap.moveToLocation(latLng: LatLng) {
    this.setCamera(
        CameraOptions.Builder()
            .zoom(16.0)
            .center(Point.fromLngLat(latLng.longitude, latLng.latitude))
            .build()
    )
}

fun Context.showToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

internal suspend fun LatLng.getUserLocation(
    context: Context,
    language: LocationInfoLanguage
): UserLocation = withContext(Dispatchers.IO) {
    try {
        val geocoder = Geocoder(context, Locale.forLanguageTag(language.code))
        val list = geocoder.getFromLocation(
            this@getUserLocation.latitude,
            this@getUserLocation.longitude,
            1
        )

        if (!list.isNullOrEmpty()) {
            val address = list[0]
            val city = address.locality
            val country = address.countryName
            val street = address.thoroughfare
            return@withContext UserLocation(
                country = country,
                city = city,
                street = street,
                lat = this@getUserLocation.latitude,
                lng = this@getUserLocation.longitude,
                language = language
            )
        } else {
            return@withContext fillUserLocationWithoutInfo(
                this@getUserLocation.latitude,
                this@getUserLocation.longitude
            )
        }
    } catch (e: Exception) {
        return@withContext UserLocation(0.0, 0.0)
    }
}

fun String.fromCodeToLocationInfoLanguage(): LocationInfoLanguage {
    return LocationInfoLanguage.valueOf(this.uppercase(Locale.ROOT))
}

fun fillUserLocationWithoutInfo(lat: Double, lng: Double): UserLocation {
    return UserLocation(
        lat = lat,
        lng = lng,
        country = "Google",
        city = "Services Are",
        street = "Not Available to fetch location info"
    )
}

internal fun isGoogleServicesEnabled(context: Context): Boolean {
    return GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
}
package com.shorman.mapspicker.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

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

fun Context.showToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}
package com.shorman.mapspicker.presentation

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.shorman.mapspicker.domain.LocationTracker
import kotlinx.coroutines.launch

internal class MapsPickerViewModel constructor(
    private val locationTracker: LocationTracker,
    private val enableMyLocation: Boolean
) : ViewModel() {

    var currentLocation by mutableStateOf<Pair<Boolean, Location?>>(false to null)
    var selectedLocation by mutableStateOf<LatLng?>(null)

    init {
        if (enableMyLocation) {
            getCurrentLocation()
        }
    }

    fun getCurrentLocation(animate: Boolean = false) = viewModelScope.launch {
        currentLocation = animate to locationTracker.getCurrentLocation()
    }

    fun updateSelectedLocation(newLocation: LatLng) = viewModelScope.launch {
        selectedLocation = newLocation
    }

}
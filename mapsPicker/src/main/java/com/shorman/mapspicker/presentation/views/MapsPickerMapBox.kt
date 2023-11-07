package com.shorman.mapspicker.presentation.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.LocationOn
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.model.LatLng
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.gestures.removeOnMoveListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.shorman.mapspicker.data.di.LocationModule
import com.shorman.mapspicker.presentation.MapsPickerViewModel
import com.shorman.mapspicker.presentation.model.IconAlignment
import com.shorman.mapspicker.presentation.model.LocationInfoLanguage
import com.shorman.mapspicker.presentation.model.UserLocation
import com.shorman.mapspicker.presentation.utils.animateToLocation
import com.shorman.mapspicker.presentation.utils.getUserLocation
import com.shorman.mapspicker.presentation.utils.moveToLocation
import kotlinx.coroutines.async

@Composable
fun MapBoxMap(
    currentLocationIconRes: Int? = null,
    moveToMyLocationIconRes: Int? = null,
    moveToMyLocationIconAlignment: IconAlignment = IconAlignment.TOP_RIGHT,
    enableMyLocation: Boolean = true,
    enableCompass: Boolean = false,
    enableTouch: Boolean = true,
    enableAnimations: Boolean = true,
    myLocationIconTint: Color = MaterialTheme.colorScheme.primary,
    currentLocationIconTint: Color = MaterialTheme.colorScheme.primary,
    getLocationInfo: Boolean = false,
    locationInfoLanguage: LocationInfoLanguage = LocationInfoLanguage.EN,
    onSelectUserLocation: suspend (UserLocation) -> Unit,
) {
    val context = LocalContext.current

    val currentLocationIcon = remember(currentLocationIconRes) {
        currentLocationIconRes?.let { resId ->
            ImageVector.vectorResource(
                theme = context.theme,
                resId = resId,
                res = context.resources
            )
        } ?: run {
            Icons.Sharp.LocationOn
        }
    }

    val moveToMyLocationIcon = remember(moveToMyLocationIconRes) {
        moveToMyLocationIconRes?.let { resId ->
            ImageVector.vectorResource(
                theme = context.theme,
                resId = resId,
                res = context.resources
            )
        } ?: run {
            Icons.Sharp.Home
        }
    }

    val viewModel = remember {
        MapsPickerViewModel(
            locationTracker = LocationModule.providesNoGoogleLocationTracker(
                context = context.applicationContext,
            ),
            enableMyLocation = enableMyLocation
        )
    }

    val currentLocation = viewModel.currentLocation
    val selectedLocation = viewModel.selectedLocation

    var isMovingCameraMoving by remember {
        mutableStateOf(false)
    }

    var mapViewInstance: MapView? by remember {
        mutableStateOf(null)
    }

    val moveListener = remember {
        object : OnMoveListener {
            override fun onMove(detector: MoveGestureDetector) = false

            override fun onMoveBegin(detector: MoveGestureDetector) {
                isMovingCameraMoving = true
            }

            override fun onMoveEnd(detector: MoveGestureDetector) {
                isMovingCameraMoving = false
                val cameraCenter = mapViewInstance?.getMapboxMap()?.cameraState?.center
                val latLng = LatLng(
                    cameraCenter?.latitude() ?: 0.0,
                    cameraCenter?.longitude() ?: 0.0
                )
                viewModel.updateSelectedLocation(latLng)
            }

        }
    }

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let { latLng ->
            if (latLng != LatLng(0.0, 0.0)) {
                val userLocation = if (getLocationInfo) {
                    val location = async { latLng.getUserLocation(context, locationInfoLanguage) }
                    location.await()
                } else {
                    UserLocation(latLng.latitude, latLng.longitude)
                }
                onSelectUserLocation(userLocation)
            }
        }
    }

    val offsetY = animateDpAsState(
        if (isMovingCameraMoving) (-40).dp else (-20).dp,
        animationSpec = tween(durationMillis = 200),
        label = ""
    )

    val shadow = animateDpAsState(
        if (isMovingCameraMoving) (2).dp else 10.dp,
        label = ""
    )

    LaunchedEffect(currentLocation) {
        val location = currentLocation.second
        val animate = currentLocation.first
        if (animate) {
            location?.let {
                mapViewInstance?.getMapboxMap()
                    ?.animateToLocation(LatLng(it.latitude, it.longitude))
            }
        } else {
            location?.let {
                mapViewInstance?.getMapboxMap()?.moveToLocation(LatLng(it.latitude, it.longitude))
            }
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = {
                MapView(it).also { mapView ->
                    mapView.getMapboxMap().loadStyleUri(Style.LIGHT) {
                        mapView.location.updateSettings {
                            enabled = true
                            pulsingEnabled = true
                        }
                    }

                    mapView.compass.enabled = enableCompass
                    mapView.gestures.pitchEnabled = enableTouch
                    mapView.gestures.rotateEnabled = enableTouch
                    mapView.gestures.pinchToZoomEnabled = enableTouch
                    mapView.gestures.scrollEnabled = enableTouch
                    mapView.getMapboxMap().addOnMoveListener(moveListener)
                    mapViewInstance = mapView
                }
            },
            update = { _ ->
            },
            onRelease = { mapView ->
                mapView.getMapboxMap().removeOnMoveListener(moveListener)
            },
        )

        Icon(
            imageVector = currentLocationIcon,
            tint = currentLocationIconTint,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Center)
                .offset(y = if (enableAnimations) offsetY.value else (-20).dp)
                .shadow(if (enableAnimations) shadow.value else 10.dp, CircleShape)

        )

        AnimatedVisibility(
            visible = enableMyLocation,
            modifier = Modifier.align(moveToMyLocationIconAlignment.align)
        ) {
            Icon(
                imageVector = moveToMyLocationIcon,
                tint = myLocationIconTint,
                contentDescription = null,
                modifier = Modifier
                    .padding(20.dp)
                    .size(40.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = Color.Blue, radius = 100.dp),
                        enabled = true,
                        onClickLabel = null,
                        role = Role.Tab,
                    ) {
                        viewModel.resetCurrentLocation()
                        viewModel.getCurrentLocation(true)
                    }
            )
        }

    }

    if (currentLocation.second == null && enableMyLocation) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

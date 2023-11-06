package com.shorman.mapspicker.presentation.views

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.shorman.mapspicker.data.di.LocationModule.providesFusedLocationProviderClient
import com.shorman.mapspicker.data.di.LocationModule.providesLocationTracker
import com.shorman.mapspicker.presentation.MapsPickerViewModel
import com.shorman.mapspicker.presentation.model.IconAlignment
import com.shorman.mapspicker.presentation.model.UserLocation
import com.shorman.mapspicker.presentation.utils.animateToLocation
import com.shorman.mapspicker.presentation.utils.moveToLocation

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
internal fun MapsPicker(
    currentLocationIconRes: Int? = null,
    moveToMyLocationIconRes: Int? = null,
    moveToMyLocationIconAlignment: IconAlignment = IconAlignment.TOP_RIGHT,
    enableMyLocation: Boolean = true,
    enableCompass: Boolean = false,
    enableZoomButtons: Boolean = false,
    enableTouch: Boolean = true,
    enableAnimations: Boolean = true,
    myLocationIconTint: Color = MaterialTheme.colorScheme.primary,
    currentLocationIconTint: Color = MaterialTheme.colorScheme.primary,
    onSelectUserLocation: (UserLocation) -> Unit,
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
            locationTracker = providesLocationTracker(
                context = context.applicationContext,
                fusedLocationProviderClient = providesFusedLocationProviderClient(context.applicationContext)
            ),
            enableMyLocation = enableMyLocation
        )
    }

    val currentLocation = viewModel.currentLocation
    val selectedLocation = viewModel.selectedLocation

    val properties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = enableMyLocation))
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = enableZoomButtons,
                compassEnabled = enableCompass,
                zoomGesturesEnabled = enableTouch,
                tiltGesturesEnabled = enableTouch,
                rotationGesturesEnabled = enableTouch,
                scrollGesturesEnabled = enableTouch,
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState()

    if (!cameraPositionState.isMoving) {
        val latLng = cameraPositionState.position.target
        viewModel.updateSelectedLocation(latLng)
    }

    LaunchedEffect(currentLocation) {
        val location = currentLocation.second
        val animate = currentLocation.first
        if (animate) {
            location?.let {
                cameraPositionState.animateToLocation(LatLng(it.latitude, it.longitude))
            }
        } else {
            location?.let {
                cameraPositionState.moveToLocation(LatLng(it.latitude, it.longitude))
            }
        }
    }

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let { latLng ->
            if (latLng != LatLng(0.0, 0.0)) {
                onSelectUserLocation(UserLocation(latLng.latitude, latLng.longitude))
            }
        }
    }

    val offsetY = animateDpAsState(
        if (cameraPositionState.isMoving) (-40).dp else (-20).dp,
        animationSpec = tween(durationMillis = 200),
        label = ""
    )

    val shadow = animateDpAsState(
        if (cameraPositionState.isMoving) (2).dp else 10.dp,
        label = ""
    )

    Box(
        Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
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
                        viewModel.getCurrentLocation(true)
                    }
            )
        }

    }

}
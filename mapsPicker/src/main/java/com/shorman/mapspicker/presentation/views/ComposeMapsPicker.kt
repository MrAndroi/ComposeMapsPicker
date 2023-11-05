package com.shorman.mapspicker.presentation.views

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.shorman.mapspicker.R
import com.shorman.mapspicker.presentation.model.IconAlignment
import com.shorman.mapspicker.presentation.model.UserLocation
import com.shorman.mapspicker.presentation.utils.openAppSettings

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ComposeMapsPicker(
    modifier: Modifier = Modifier,
    permissionRationalButtonText: Int = R.string.permission_rational_button_text_default,
    permissionButtonText: Int = R.string.permission_button_text_default,
    permissionRationalText: Int = R.string.permission_rational_text_default,
    permissionText: Int = R.string.permission_text_default,
    currentLocationIconRes: Int? = null,
    moveToMyLocationIconRes: Int? = null,
    moveToMyLocationIconAlignment: IconAlignment = IconAlignment.BOTTOM_RIGHT,
    enableMyLocation: Boolean = true,
    enableCompass: Boolean = false,
    enableZoomButtons: Boolean = false,
    enableTouch: Boolean = true,
    onSelectUserLocation: (UserLocation) -> Unit,
) {

    val context = LocalContext.current

    fun getButtonText(shouldShowRationale: Boolean): String {
        return if (shouldShowRationale) {
            context.getString(permissionRationalButtonText)
        } else {
            context.getString(permissionButtonText)
        }
    }

    fun getRationalText(shouldShowRationale: Boolean): String {
        return if (shouldShowRationale) {
            context.getString(permissionRationalText)
        } else {
            context.getString(permissionText)
        }
    }

    Surface(
        modifier = modifier
    ) {

        val locationPermissionsState = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )

        if (locationPermissionsState.allPermissionsGranted) {
            MapsPicker(
                currentLocationIconRes = currentLocationIconRes,
                moveToMyLocationIconRes = moveToMyLocationIconRes,
                moveToMyLocationIconAlignment = moveToMyLocationIconAlignment,
                enableMyLocation = enableMyLocation,
                enableCompass = enableCompass,
                enableZoomButtons = enableZoomButtons,
                enableTouch = enableTouch,
                onSelectUserLocation = onSelectUserLocation
            )
        } else {

            LaunchedEffect(Unit) {
                locationPermissionsState.launchMultiplePermissionRequest()
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = getRationalText(locationPermissionsState.shouldShowRationale),
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (locationPermissionsState.shouldShowRationale) {
                            context.openAppSettings()
                        } else {
                            locationPermissionsState.launchMultiplePermissionRequest()
                        }
                    })
                {
                    Text(getButtonText(locationPermissionsState.shouldShowRationale))
                }
            }
        }

    }
}
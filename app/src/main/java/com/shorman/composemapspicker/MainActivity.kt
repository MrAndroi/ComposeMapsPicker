package com.shorman.composemapspicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shorman.composemapspicker.ui.theme.ComposeMapsPickerTheme
import com.shorman.mapspicker.presentation.model.IconAlignment
import com.shorman.mapspicker.presentation.model.LocationInfoLanguage
import com.shorman.mapspicker.presentation.utils.showToast
import com.shorman.mapspicker.presentation.views.ComposeMapsPicker


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMapsPickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        //Usage of ComposeMapsPicker
                        ComposeMapsPicker(
                            moveToMyLocationIconRes = R.drawable.locate_me_icon,
                            moveToMyLocationIconAlignment = IconAlignment.TOP_RIGHT,
                            enableMyLocation = true,
                            enableCompass = true,
                            enableTouch = true,
                            enableAnimations = true,
                            currentLocationIconTint = Color.Magenta,
                            getLocationInfo = true,
                            //Here you can pass any language you want, or use fromCodeToLocationInfoLanguage()
                            //To select your device language, Please check LocationInfoLanguage to see supported languages
                            locationInfoLanguage = LocationInfoLanguage.DE
                        ) { location ->
                            //location object contains data about selected location
                            //This got called everytime user selects new location
                            this@MainActivity.showToast(location.lat.toString())
                        }

                        //Create any footer you want
                        Box(
                            modifier = Modifier
                                .shadow(10.dp)
                                .fillMaxWidth()
                                .background(Color.White)
                                .align(Alignment.BottomCenter)

                        ) {

                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "Select Location",
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}
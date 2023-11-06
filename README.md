# Compose Maps Picker: Android Location Picker Library

The Maps Picker library is a powerful Android tool that allows users to seamlessly pick their location on a map while providing customization options for map appearance and handling location permissions. This library simplifies the process of obtaining user location and offers numerous features for your Android app.

## Screenshot

![How it looks](screenshots/sample.gif)


## Usage

You can refer to the sample under the app, Then navigate to MainActivity.kt.

```
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
	//Then add these lines to your dependencies, Check Releases for latest version
	dependencies {
	     implementation 'com.github.MrAndroi:ComposeMapsPicker:LATEST'
	}

     <application
        
        <meta-data android:name="com.google.android.geo.API_KEY" android:value="PUT_YOUR_KEY_HERE"/>

    </application>

    //Usage of ComposeMapsPicker
    ComposeMapsPicker(
      moveToMyLocationIconRes = R.drawable.locate_me_icon,
      moveToMyLocationIconAlignment = IconAlignment.TOP_RIGHT,
      enableMyLocation = true,
      enableCompass = true
      ) { location ->
         //location object contains data about selected location
         //This got called everytime user selects new location
         this@MainActivity.showToast("${location.lng}")
      }


```

## Documentation

Now let's walk through the available customizations:

```
    //These are all available customizations that you can pass throughout constructor
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
    enableAnimations: Boolean = true,
    myLocationIconTint: Color = MaterialTheme.colorScheme.primary,
    currentLocationIconTint: Color = MaterialTheme.colorScheme.primary,
    onSelectUserLocation: (UserLocation) -> Unit,

```


### That's it :).

## Sorry for any bad practices in the code, I will be happy for any suggestions to improve the code, Thank you.


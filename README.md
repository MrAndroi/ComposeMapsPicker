# Compose Maps Picker: Android Location Picker Library (Supports devices that don't have google services)

The Maps Picker library is a powerful Android tool that allows users to seamlessly pick their
location on a map while providing customization options for map appearance and handling location
permissions, Getting location info in several languages.

This library simplifies the process of obtaining user location and offers numerous
features for your Android app.

## Screenshot

![How it looks](screenshots/sample.gif)
![How it looks without google services](screenshots/sample2.gif)

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
	1- Add Mappciker dependencies
	dependencies {
	     implementation 'com.github.MrAndroi:ComposeMapsPicker:LATEST'
	}

    2- in your manifest.xml file, Add google API_KEY
     <application
        <meta-data android:name="com.google.android.geo.API_KEY" android:value="PUT_YOUR_KEY_HERE"/>
    </application>
    
    3- in your strings.xml file add mapbox_access_token, Please refer to mapbox.com and create account in order to get this token
    <string name="mapbox_access_token">PUT_MAPBOX_ACCESS_TOKEN</string>
    
    4- add the following line with MAPBOX_DOWNLOADS_TOKEN to gradle.properties file, , Please refer to mapbox.com and create account in order to get this token
    MAPBOX_DOWNLOADS_TOKEN=PUT_MAP_BOX_DOWNLOADS_TOKEN
    
    5- Last step is to add these lines to settings.gradle file in order to download Mapbox Sdk
    maven {
       url 'https://api.mapbox.com/downloads/v2/releases/maven'
       authentication {
           basic(BasicAuthentication)
       }
       credentials {
           username = "mapbox"
           password = MAPBOX_DOWNLOADS_TOKEN
       }
    }


     //Usage of ComposeMapsPicker
     ComposeMapsPicker(
       moveToMyLocationIconRes = R.drawable.locate_me_icon,
       moveToMyLocationIconAlignment = IconAlignment.TOP_RIGHT,
       enableMyLocation = true,
       enableCompass = true,
       enableAnimations = true,
       currentLocationIconTint = Color.Magenta,
       getLocationInfo = true,
       //Here you can pass any language you want, or use fromCodeToLocationInfoLanguage()
       //To select your device language, Please check LocationInfoLanguage to see supported languages
       locationInfoLanguage = LocationInfoLanguage.DE
           ) { location ->
               //location object contains data about selected location
               //This got called everytime user selects new location
               this@MainActivity.showToast(location.getFormattedAddress())
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
    getLocationInfo: Boolean = false,
    locationInfoLanguage: LocationInfoLanguage = LocationInfoLanguage.EN,
    onSelectUserLocation: (UserLocation) -> Unit,
    
    //Suppored languages
    English
    Arabic
    French
    German
    Spanish
    Japanese
    Korean
    Hindi

```

### That's it :).

## Sorry for any bad practices in the code, I will be happy for any suggestions to improve the code, Thank you.


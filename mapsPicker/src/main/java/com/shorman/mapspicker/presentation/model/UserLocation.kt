package com.shorman.mapspicker.presentation.model

data class UserLocation(
    val lat: Double,
    val lng: Double,
    val country: String? = "",
    val city: String? = "",
    val street: String? = "",
    val language: LocationInfoLanguage = LocationInfoLanguage.EN,
) {

    fun getFormattedAddress(): String {
        val unknownCountryTranslated = when (language) {
            LocationInfoLanguage.EN -> "Unknown Country"
            LocationInfoLanguage.AR -> "بلد غير معروف"
            LocationInfoLanguage.FR -> "Pays inconnu"
            LocationInfoLanguage.DE -> "Unbekanntes Land"
            LocationInfoLanguage.ES -> "País desconocido"
            LocationInfoLanguage.JA -> "未知の国"
            LocationInfoLanguage.KA -> "알 수 없는 국가"
            LocationInfoLanguage.HI -> "अज्ञात देश"
        }

        val unknownCityTranslated = when (language) {
            LocationInfoLanguage.EN -> "Unknown City"
            LocationInfoLanguage.AR -> "مدينة غير معروفة"
            LocationInfoLanguage.FR -> "Ville inconnue"
            LocationInfoLanguage.DE -> "Unbekannte Stadt"
            LocationInfoLanguage.ES -> "Ciudad Desconocida"
            LocationInfoLanguage.JA -> "未知の都市"
            LocationInfoLanguage.KA -> "알 수 없는 도시"
            LocationInfoLanguage.HI -> "अनजान शहर"
        }

        val unknownStreetTranslated = when (language) {
            LocationInfoLanguage.EN -> "Unknown Street"
            LocationInfoLanguage.AR -> "شارع غير معروف"
            LocationInfoLanguage.FR -> "Rue inconnue"
            LocationInfoLanguage.DE -> "Unbekannte Straße"
            LocationInfoLanguage.ES -> "Calle desconocida"
            LocationInfoLanguage.JA -> "未知の通り"
            LocationInfoLanguage.KA -> "알 수 없는 거리"
            LocationInfoLanguage.HI -> "अनजान सड़क"
        }

        return "${country ?: unknownCountryTranslated}, ${city ?: unknownCityTranslated}, ${street ?: unknownStreetTranslated}"
    }
}

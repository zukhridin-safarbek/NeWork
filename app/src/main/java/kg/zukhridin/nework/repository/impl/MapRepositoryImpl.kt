package kg.zukhridin.nework.repository.impl

import android.content.Context
import android.location.Geocoder

class MapRepositoryImpl {
    companion object {
        fun getCoordination(context: Context, lat: Double, lng: Double): Map<String, String> {
            return try {
                val geo = Geocoder(context)
                val address = geo.getFromLocation(lat, lng, 1)
                val map: Map<String, String> = mapOf(
                    "country" to "${address?.get(0)?.countryName}",
                    "region" to "${address?.get(0)?.locality}",
                    "thoroughfare" to "${address?.get(0)?.thoroughfare}"
                )
                return map
            } catch (e: Exception) {
                emptyMap<String, String>()
            }
        }
    }
}
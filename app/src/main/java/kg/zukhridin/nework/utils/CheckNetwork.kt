package kg.zukhridin.nework.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class CheckNetwork(private val context: Context) {
    fun networkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
    }
}
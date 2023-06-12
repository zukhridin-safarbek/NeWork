package kg.zukhridin.nework.presentation.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kg.zukhridin.nework.data.util.Constants.PERMISSION_CODE

class Permissions {
    fun isFolderPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun folderPermission(activity: AppCompatActivity) {
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        }
    }

    fun folderPermission(activity: FragmentActivity) {
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        }
    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun setLocationPermission(activity: FragmentActivity): Boolean {
        var isGranted: Boolean = false
        val requestPermission =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted = it
            }
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        return isGranted
    }
    fun setLocationPermission(fragment: Fragment): Boolean {
        var isGranted: Boolean = false
        val requestPermission =
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted = it
            }
        if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        return isGranted
    }
}
package kg.zukhridin.nework.presentation.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kg.zukhridin.nework.data.util.Constants.MY_READ_PERMISSION_CODE

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
                MY_READ_PERMISSION_CODE
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
                MY_READ_PERMISSION_CODE
            )
        }
    }
}
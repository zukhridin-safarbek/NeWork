package kg.zukhridin.nework.data.util

import android.os.Environment

class StoragePaths {
    val ROOT_DIR = Environment.getExternalStorageDirectory().path
    val PICTURES = "$ROOT_DIR/Pictures"
    val CAMERA = "$ROOT_DIR/DCIM/Camera"
}
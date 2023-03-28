package kg.zukhridin.nework.utils

import android.os.Environment

class FilePaths {
    val ROOT_DIR = Environment.getExternalStorageDirectory().path
    val PICTURES = "$ROOT_DIR/Pictures"
    val CAMERA = "$ROOT_DIR/DCIM/Camera"
}
package kg.zukhridin.nework.usecase

import android.content.Context
import android.provider.MediaStore
import kg.zukhridin.nework.dto.CustomMedia
import kg.zukhridin.nework.dto.CustomMediaType

class ImagesFromExternalStorageUseCase {
    companion object{
        fun imagesFromExternalStorage(context: Context): ArrayList<CustomMedia> {
            val medias = ArrayList<CustomMedia>()
            val imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(imagesUri, projection, null, null, null)
            try {
                cursor!!.moveToFirst()
                do {
                    val media = CustomMedia(
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
                        CustomMediaType.IMAGE
                    )
                    medias.add(media)
                } while (cursor.moveToNext())
                cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return medias
        }
    }
}
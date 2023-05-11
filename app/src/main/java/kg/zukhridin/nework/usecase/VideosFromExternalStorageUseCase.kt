package kg.zukhridin.nework.usecase

import android.content.Context
import android.provider.MediaStore
import kg.zukhridin.nework.dto.CustomMedia
import kg.zukhridin.nework.dto.CustomMediaType

class VideosFromExternalStorageUseCase {
    companion object{
        fun videosFromExternalStorage(context: Context): ArrayList<CustomMedia> {
            val medias = ArrayList<CustomMedia>()
            val videosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
            val cursor = context.contentResolver.query(videosUri, projection, null, null, null)
            try {
                cursor!!.moveToNext()
                do {
                    val media = CustomMedia(
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)),
                        CustomMediaType.VIDEO
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
package kg.zukhridin.nework.usecase

import android.content.Context
import android.provider.MediaStore
import kg.zukhridin.nework.dto.CustomMedia
import kg.zukhridin.nework.dto.CustomMediaType

class AudiosFromExternalStorageUseCase {
    companion object {
        fun audiosFromExternalStorageUseCase(context: Context): ArrayList<CustomMedia>{
            val medias = ArrayList<CustomMedia>()
            val audiosUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Audio.AudioColumns.DATA)
            val cursor = context.contentResolver.query(audiosUri, projection, null, null, null)
            try {
                cursor!!.moveToNext()
                do {
                    val media = CustomMedia(
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)),
                        CustomMediaType.AUDIO
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
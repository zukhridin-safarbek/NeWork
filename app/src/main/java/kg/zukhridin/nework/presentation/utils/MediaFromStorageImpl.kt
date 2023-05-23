package kg.zukhridin.nework.presentation.utils

import android.content.Context
import android.provider.MediaStore
import kg.zukhridin.nework.domain.enums.MediaType
import kg.zukhridin.nework.domain.models.MediaModel

     fun Context.getImagesFromGallery(): ArrayList<MediaModel> {
        val medias = ArrayList<MediaModel>()
        val imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this.contentResolver.query(imagesUri, projection, null, null, null)
        try {
            cursor!!.moveToFirst()
            do {
                val media = MediaModel(
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
                    MediaType.IMAGE
                )
                medias.add(media)
            } while (cursor.moveToNext())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return medias
    }

     fun Context.getVideosFromGallery(): ArrayList<MediaModel> {
        val medias = ArrayList<MediaModel>()
        val videosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
        val cursor = this.contentResolver.query(videosUri, projection, null, null, null)
        try {
            cursor!!.moveToNext()
            do {
                val media = MediaModel(
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)),
                    MediaType.VIDEO
                )
                medias.add(media)
            } while (cursor.moveToNext())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return medias
    }

     fun Context.getAudiosFromGallery(): ArrayList<MediaModel> {
        val medias = ArrayList<MediaModel>()
        val audiosUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.AudioColumns.DATA)
        val cursor = this.contentResolver.query(audiosUri, projection, null, null, null)
        try {
            cursor!!.moveToNext()
            do {
                val media = MediaModel(
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)),
                    MediaType.AUDIO
                )
                medias.add(media)
            } while (cursor.moveToNext())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return medias
    }

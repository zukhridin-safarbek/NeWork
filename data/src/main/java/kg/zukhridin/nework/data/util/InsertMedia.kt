package kg.zukhridin.nework.data.util

import kg.zukhridin.nework.data.service.requests.APIService
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class InsertMedia (
    private val mediaService: APIService
) {
    suspend fun insert(file: String): String? {
        return if ("file" !in file) {
            val fileF = File(file)
            val media = MultipartBody.Part.createFormData(
                "file",
                fileF.name,
                requireNotNull(fileF.asRequestBody())
            )
            val mediaResponse = mediaService.insertMedia(media)
            mediaResponse.body()?.url
        } else {
            val fileF = File(file.substring(7, file.length))
            val media = MultipartBody.Part.createFormData(
                "file",
                fileF.name,
                requireNotNull(fileF.asRequestBody())
            )
            val mediaResponse = mediaService.insertMedia(media)
            mediaResponse.body()?.url
        }
    }
}
package kg.zukhridin.nework.usecase

import kg.zukhridin.nework.service.APIService
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SaveMediaToServerUseCase(private val service: APIService) {
    suspend fun saveMediaToServer(file: String): String? {
        if ("file" !in file) {
            val fileF = File(file)
            val media = MultipartBody.Part.createFormData(
                "file",
                fileF.name,
                requireNotNull(fileF.asRequestBody())
            )
            val response = service.insertMedia(media)
            if (!response.isSuccessful) {
                throw Exception()
            }
            return response.body()?.url
        } else {
            val fileF = File(file.substring(7, file.length))
            val media = MultipartBody.Part.createFormData(
                "file",
                fileF.name,
                requireNotNull(fileF.asRequestBody())
            )
            val response = service.insertMedia(media)
            if (!response.isSuccessful) {
                throw Exception()
            }
            return response.body()?.url
        }
    }
}
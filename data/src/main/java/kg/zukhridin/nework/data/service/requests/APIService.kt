package kg.zukhridin.nework.data.service.requests

import kg.zukhridin.nework.domain.models.MediaModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIService {
    @Multipart
    @POST("media/")
    suspend fun insertMedia(@Part file: MultipartBody.Part): Response<MediaModel>
}
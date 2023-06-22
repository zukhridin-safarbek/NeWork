package kg.zukhridin.nework.data.service.requests

import kg.zukhridin.nework.data.entity.JobEntity
import kg.zukhridin.nework.domain.models.Job
import retrofit2.Response
import retrofit2.http.*

interface JobAPIService {

    @GET("{user_id}/jobs/")
    suspend fun getUserJobs(@Path("user_id") userId: Int): Response<List<Job>>

    @POST("my/jobs/")
    suspend fun insertJob(@Body job: Job): Response<Job>

    @DELETE("my/jobs/{job_id}/")
    suspend fun deleteJob(@Path("job_id") jobId: Int): Response<Any>

}
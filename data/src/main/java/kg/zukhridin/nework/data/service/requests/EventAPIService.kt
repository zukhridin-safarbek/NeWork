package kg.zukhridin.nework.data.service.requests

import kg.zukhridin.nework.data.entity.EventEntity
import kg.zukhridin.nework.domain.models.Event
import retrofit2.Response
import retrofit2.http.*

interface EventAPIService {
    @GET("events/latest/")
    suspend fun getLatestEvents(@Query("count") count: Int): Response<List<EventEntity>>

    @GET("events/{event_id}/after/")
    suspend fun getAfterEvents(
        @Query("count") count: Int,
        @Path("event_id") eventId: Int
    ): Response<List<EventEntity>>

    @GET("events/{event_id}/before/")
    suspend fun getBeforeEvents(
        @Query("count") count: Int,
        @Path("event_id") eventId: Int
    ): Response<List<EventEntity>>

    @POST("events/")
    suspend fun insertEvent(@Body event: Event): Response<Event>

    @DELETE("events/{event_id}/")
    suspend fun deleteEvent(@Path("event_id") eventId: Int): Response<Any>

    @POST("events/{event_id}/likes/")
    suspend fun likeById(@Path("event_id") eventId: Int): Response<Event>

    @DELETE("events/{event_id}/likes/")
    suspend fun dislikeById(@Path("event_id") eventId: Int): Response<Event>
}
package kg.zukhridin.nework.domain.service.repositories

import kg.zukhridin.nework.domain.models.ErrorResponseModel
import kg.zukhridin.nework.domain.models.Event

interface EventRepositoryService {
    suspend fun eventLikeById(event: Event): Event?
    suspend fun insertEvent(event: Event): Pair<Boolean, ErrorResponseModel>
    suspend fun deleteEvent(event: Event): Boolean
    suspend fun eventDislikeById(event: Event): Event?

}
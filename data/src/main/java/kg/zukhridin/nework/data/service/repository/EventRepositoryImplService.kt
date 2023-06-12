package kg.zukhridin.nework.data.service.repository

import kg.zukhridin.nework.data.service.requests.APIService
import kg.zukhridin.nework.data.service.requests.EventAPIService
import kg.zukhridin.nework.data.util.InsertMedia
import kg.zukhridin.nework.data.util.getErrorBody
import kg.zukhridin.nework.domain.models.Attachment
import kg.zukhridin.nework.domain.models.ErrorResponseModel
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.domain.service.repositories.EventRepositoryService
import kg.zukhridin.nework.domain.storage.repositories.EventRepositoryStorage
import javax.inject.Inject

class EventRepositoryImplService @Inject constructor(
    private val eventService: EventAPIService,
    private val mediaService: APIService,
    private val repositoryStorage: EventRepositoryStorage
) :
    EventRepositoryService {

    override suspend fun insertEvent(event: Event): Pair<Boolean, ErrorResponseModel> {
        if (event.attachment != null) {
            val media = InsertMedia(mediaService).insert(event.attachment?.url!!)
            val copiedEvent = event.copy(
                attachment = Attachment(
                    media!!,
                    event.attachment!!.type
                )
            )
            val response = eventService.insertEvent(
                copiedEvent
            )
            return if (response.isSuccessful) {
                repositoryStorage.insertEvent(copiedEvent)
                Pair(response.isSuccessful, ErrorResponseModel(null))
            } else {
                Pair(response.isSuccessful, getErrorBody(response.errorBody()))
            }
        } else {
            val response = eventService.insertEvent(event)
            return if (response.isSuccessful) {
                repositoryStorage.insertEvent(event)
                Pair(response.isSuccessful, ErrorResponseModel(null))
            } else {
                Pair(response.isSuccessful, getErrorBody(response.errorBody()))
            }
        }
    }

    override suspend fun deleteEvent(event: Event): Boolean {
        return eventService.deleteEvent(eventId = event.id).isSuccessful
    }

    override suspend fun eventLikeById(event: Event): Event? {
        val response = eventService.likeById(eventId = event.id)
        return response.body()
    }

    override suspend fun eventDislikeById(event: Event): Event? {
        val response = eventService.dislikeById(eventId = event.id)
        return response.body()
    }
}
package kg.zukhridin.nework.data.service.repository

import kg.zukhridin.nework.data.service.requests.APIService
import kg.zukhridin.nework.data.service.requests.EventAPIService
import kg.zukhridin.nework.data.util.InsertMedia
import kg.zukhridin.nework.domain.models.Attachment
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.domain.service.repositories.EventRepositoryService
import javax.inject.Inject

class EventRepositoryImplService @Inject constructor(
    private val eventService: EventAPIService,
    private val mediaService: APIService
) :
    EventRepositoryService {

    override suspend fun insertEvent(event: Event): Pair<Boolean, Event?> {
        return if (event.attachment != null) {
            val media = InsertMedia(mediaService).insert(event.attachment?.url!!)
            val response = eventService.insertEvent(
                event.copy(
                    attachment = Attachment(
                        media!!,
                        event.attachment!!.type
                    )
                )
            )
            Pair(response.isSuccessful, response.body())
        } else {
            val response = eventService.insertEvent(event)
            Pair(response.isSuccessful, response.body())
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
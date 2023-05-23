package kg.zukhridin.nework.data.service.repository

import kg.zukhridin.nework.domain.models.Post
import kg.zukhridin.nework.data.service.requests.APIService
import kg.zukhridin.nework.data.service.requests.PostAPIService
import kg.zukhridin.nework.data.service.requests.WallAPIService
import kg.zukhridin.nework.data.util.InsertMedia
import kg.zukhridin.nework.domain.models.Attachment
import kg.zukhridin.nework.domain.service.repositories.PostRepositoryService
import javax.inject.Inject

class PostRepositoryImplService @Inject constructor(
    private val postService: PostAPIService,
    private val wallService: WallAPIService,
    private val mediaService: APIService
) : PostRepositoryService {
    override suspend fun getWall(userId: Int): Map<Int, List<Post>> {
        val map = mutableMapOf<Int, List<Post>>()
        val wallsResponse = wallService.getWalls(userId)
        map[userId] = wallsResponse.body().orEmpty()
        return map
    }

    override suspend fun insertPost(post: Post): Boolean {
        return if (post.attachment != null) {
            val media = InsertMedia(mediaService).insert(post.attachment?.url!!)
            val response =
                postService.insertPost(
                    post.copy(
                        attachment = Attachment(
                            media!!,
                            post.attachment!!.type
                        )
                    )
                )
            response.isSuccessful
        } else {
            val response =
                postService.insertPost(
                    post
                )
            response.isSuccessful
        }
    }

    override suspend fun deletePostById(id: Int) {
        postService.deletePostById(id)
    }


    override suspend fun updatePostById(post: Post) {
        if (post.id != 0) {
            postService.updatePostById(post)
        }
    }

    override suspend fun likeById(post: Post) {
        postService.likeById(post.id)
    }

    override suspend fun dislikeById(post: Post) {
        postService.dislikeById(post.id)
    }
}
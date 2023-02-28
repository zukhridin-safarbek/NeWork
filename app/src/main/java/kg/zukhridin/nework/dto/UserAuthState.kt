package kg.zukhridin.nework.dto

data class UserAuthState (
    val id: Long,
    val token: String,
    val name: String,
    val avatar: String? = null,
        )
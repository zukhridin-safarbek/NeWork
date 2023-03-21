package kg.zukhridin.nework.dto

data class UserAuthState (
    val id: Int,
    val token: String,
    val name: String,
    val avatar: String? = null,
        )
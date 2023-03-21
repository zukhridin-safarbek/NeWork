package kg.zukhridin.nework.dto

data class User(
    val user_id: Int,
    val login: String,
    val name: String,
    val avatar: String? = null,
)
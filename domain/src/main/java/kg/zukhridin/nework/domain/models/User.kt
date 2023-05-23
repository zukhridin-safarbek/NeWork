package kg.zukhridin.nework.domain.models

data class User(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String? = null,
)
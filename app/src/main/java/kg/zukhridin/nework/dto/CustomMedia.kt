package kg.zukhridin.nework.dto

data class CustomMedia (val url: String, val type: CustomMediaType)
enum class CustomMediaType{
    IMAGE, VIDEO, AUDIO
}
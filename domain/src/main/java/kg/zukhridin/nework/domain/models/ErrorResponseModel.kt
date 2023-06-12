package kg.zukhridin.nework.domain.models

data class ErrorResponseModel(val reason: String? = null, val content: ArrayList<String>? = null)
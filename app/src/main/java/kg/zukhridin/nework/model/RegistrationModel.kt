package kg.zukhridin.nework.model

import kg.zukhridin.nework.dto.Token

data class RegistrationModel(
    val token: Token,
    val errorResponseModel: ErrorResponseModel
)
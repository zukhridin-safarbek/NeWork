package kg.zukhridin.nework.dto

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("users")
    val userPreview: UserPreview
)

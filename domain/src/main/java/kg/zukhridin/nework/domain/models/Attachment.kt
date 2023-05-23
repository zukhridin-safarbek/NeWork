package kg.zukhridin.nework.domain.models

import kg.zukhridin.nework.domain.enums.AttachmentType

data class Attachment(
    val url: String,
    val type: kg.zukhridin.nework.domain.enums.AttachmentType
)
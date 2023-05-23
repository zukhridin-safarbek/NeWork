package kg.zukhridin.nework.presentation.utils

import java.time.OffsetDateTime

class CustomOffsetDateTime {
    companion object{
        fun timeDecode(time: String): String {
            val offsetDateTime = OffsetDateTime.parse(time)
            return "${offsetDateTime.dayOfMonth}/${offsetDateTime.month}/${offsetDateTime.year}"
        }
    }
}
package kg.zukhridin.nework.utils

import java.time.OffsetDateTime

class CustomOffsetDateTime {
    companion object{
        fun timeDecode(time: String): String {
            val offsetDateTime = OffsetDateTime.parse(time)
            return "${offsetDateTime.dayOfMonth}/${offsetDateTime.month}/${offsetDateTime.year}"
        }
    }
}
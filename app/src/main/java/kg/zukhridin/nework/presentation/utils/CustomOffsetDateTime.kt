package kg.zukhridin.nework.presentation.utils

import kg.zukhridin.nework.domain.enums.CustomMonthEnum
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Calendar

class CustomOffsetDateTime {
    companion object {
        fun timeDecode(time: String): String {
            val offsetDateTime = OffsetDateTime.parse(time)
            return "${if (offsetDateTime.dayOfMonth <= 9) "0${offsetDateTime.dayOfMonth}" else offsetDateTime.dayOfMonth}/${
                CustomMonthEnum.fromString(
                    offsetDateTime.month.toString()
                ).value
            }/${offsetDateTime.year}"
        }
        fun timeCode(): String {
            val today = Calendar.getInstance()
            return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(today.time)
        }
    }
}
package kg.zukhridin.nework.domain.enums

import java.util.Locale

enum class CustomMonthEnum(val value: String) {
    JANUARY("01"), FEBRUARY("02"), MARCH("03"), APRIL("04"), MAY("05"), JUNE("06"), JULY("07"),
    AUGUST("08"), SEPTEMBER(
        "09"
    ),
    OCTOBER("10"), NOVEMBER("11"), DECEMBER("12");

    companion object {
        fun fromString(value: String) = valueOf(value.toUpperCase(Locale.ROOT))
    }
}
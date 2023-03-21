package kg.zukhridin.nework.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kg.zukhridin.nework.dto.Coordinates

class CoordinateConverter {
    @TypeConverter
    fun fromCoordinate(coordinates: Coordinates?): String? {
        val gson = Gson()
        return gson.toJson(coordinates)
    }

    @TypeConverter
    fun toCoordinate(value: String?): Coordinates? {
        val coordinates = object : TypeToken<Coordinates>(){}.type
        return Gson().fromJson(value, coordinates)
    }
}
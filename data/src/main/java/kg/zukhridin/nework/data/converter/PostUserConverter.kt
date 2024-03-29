package kg.zukhridin.nework.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kg.zukhridin.nework.domain.models.UserPreview

class PostUserConverter {
    @TypeConverter
    fun fromMap(value: Map<Long, UserPreview>?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromString(value: String?): Map<Long, UserPreview>? {
        val listType = object : TypeToken<Map<Long, UserPreview>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
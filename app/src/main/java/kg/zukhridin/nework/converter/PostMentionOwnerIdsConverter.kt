package kg.zukhridin.nework.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kg.zukhridin.nework.dto.PostLikeOwnerIds
import kg.zukhridin.nework.dto.PostMentionIds

class PostMentionOwnerIdsConverter {
    @TypeConverter
    fun fromList(list: PostMentionIds?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(value: String?):PostMentionIds?{
        val listType = object : TypeToken<PostMentionIds>(){}.type
        return Gson().fromJson(value, listType)
    }
}
package kg.zukhridin.nework.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kg.zukhridin.nework.dto.Job

@Entity
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val position: String,
    val start: String,
    val finish: String? = null,
    val link: String? = null,
    val userId: Int? = null
) {
    fun toDto(): Job = Job(id, name, position, start, finish, link)

    companion object {
        fun fromDto(job: Job): JobEntity =
            JobEntity(job.id, job.name, job.position, job.start, job.finish, job.link, null)
    }
}

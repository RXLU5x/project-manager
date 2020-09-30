package ipl.isel.daw.group8.comments.models

import java.time.LocalDate

data class Comment(val id: Long, val issueId: Long, val projectId: Long, val userId: Long, val creatorId: Long, val text: String?, val createdOn: LocalDate?)

fun Comment.toOutputModel() = CommentOutputModel(this.id, this.issueId, this.projectId, this.userId, this.creatorId, this.text, this.createdOn)
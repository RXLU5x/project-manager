package ipl.isel.daw.group8.comments.services

import ipl.isel.daw.group8.comments.daos.CommentDAO
import ipl.isel.daw.group8.comments.models.Comment
import org.springframework.stereotype.Service

@Service
class CommentServices(val dao: CommentDAO)
{
    fun updateCommentText(userId: Long, projectId: Long, issueId: Long, id: Long, username: String, password: String, newText: String): Comment =
            dao.updateCommentTextWithCredentials(userId, projectId, issueId, id, username, password, newText)

    fun deleteComment(userId: Long, projectId: Long, issueId: Long, id: Long, username: String, password: String)=
            dao.deleteCommentWithCredentials(userId, projectId, issueId, id, username, password)
}

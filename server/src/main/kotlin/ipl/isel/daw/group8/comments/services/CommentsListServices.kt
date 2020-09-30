package ipl.isel.daw.group8.comments.services

import ipl.isel.daw.group8.comments.daos.CommentsListDAO
import org.springframework.stereotype.Service

@Service
class CommentsListServices(val dao: CommentsListDAO)
{
    fun listComments(userId: Long, projectId: Long, issueId: Long, page: Int, username: String, password: String) =
            dao.listAndCountCommentsWithCredentials(userId, projectId, issueId, page, username, password)

    fun registerComment(userId: Long, projectId: Long, issueId: Long, text: String, username: String, password: String) =
            dao.registerCommentWithCredentials(userId, projectId, issueId, text, username, password)
}
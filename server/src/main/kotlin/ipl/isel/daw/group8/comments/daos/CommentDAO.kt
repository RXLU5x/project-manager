package ipl.isel.daw.group8.comments.daos

import ipl.isel.daw.group8.comments.models.Comment
import ipl.isel.daw.group8.common.ForbiddenException
import ipl.isel.daw.group8.issues.daos.IssueDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

class CommentAccessDeniedException(id: Long, issueId: Long): ForbiddenException(
    title = "Comment Access Denied",
    detail = "The issue with id $issueId isn't the owner of the comment with id $id."
)

interface CommentDAO
{
    @CreateSqlObject
    fun IssueDAO(): IssueDAO

    @CreateSqlObject
    fun CommentsListDAO(): CommentsListDAO

    @SqlQuery("SELECT \"creator_id\" FROM \"Comment\" WHERE \"id\" = ?")
    @Transaction
    fun getCommentCreatorId(id: Long): Long

    @SqlQuery("SELECT * FROM \"Comment\" WHERE \"id\" = ?")
    @Transaction
    fun getCommentInformation(id: Long): Comment

    @Transaction
    fun getCommentInformationWithCredentials(userId: Long, projectId: Long, issueId: Long, id: Long, username: String, password: String): Comment {
        validateCommentAction(id, issueId, projectId, userId, username, password)

        return getCommentInformation(id)
    }

    @SqlQuery("UPDATE \"Issue\" SET \"text\" = ? WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun updateCommentText(newText: String, id: Long): Comment

    @Transaction
    fun updateCommentTextWithCredentials(userId: Long, projectId: Long, issueId: Long, id: Long, username: String, password: String, newText: String): Comment
    {
        var comment = getCommentInformationWithCredentials(userId, projectId, issueId, id, username, password)

        if(comment.text != newText)
            comment = updateCommentText(newText, id)

        return comment
    }

    @SqlUpdate("DELETE FROM \"Comment\" WHERE \"id\" = ?")
    @Transaction
    fun deleteComment(id: Long)

    @Transaction
    fun deleteCommentWithCredentials(userId: Long, projectId: Long, issueId: Long, id: Long, username: String, password: String) {
        validateCommentAction(id, issueId, projectId, userId, username, password)

        deleteComment(id)
    }

    @SqlQuery("SELECT \"issue_id\" FROM \"Comment\" WHERE \"id\" = ?")
    @Transaction
    fun getCommentOwnerIssueId(id: Long): Long

    @Transaction
    fun checkOwnership(id: Long, issueId: Long)
    {
        // Get the id of the issue who owns the comment with this id
        val oId = getCommentOwnerIssueId(id)

        // Check if the owner id corresponds to the issue id path variable
        if(issueId != oId)
            throw CommentAccessDeniedException(id, issueId)
    }

    @Transaction
    fun validateCommentAction(id: Long, issueId: Long, projectId: Long, userId: Long, username: String, password: String, vararg allowedAccountsIds: Long)
    {
        // Check if there's a comment with this id
        CommentsListDAO().checkIfCommentExists(id)

        // Get the id of the comment creator
        val cId = getCommentCreatorId(id)

        // Check if the owner of the comment with this id matches the issue id in the url path
        checkOwnership(id, issueId)

        // Check permission to perform this action
        IssueDAO().validateIssueAction(issueId, projectId, userId, username, password, cId, *allowedAccountsIds)
    }
}

@Repository
class CommentDAOImplementation(val jdbi: Jdbi)
{
    @Bean
    fun getCommentDAO(): CommentDAO = jdbi.onDemand()
}
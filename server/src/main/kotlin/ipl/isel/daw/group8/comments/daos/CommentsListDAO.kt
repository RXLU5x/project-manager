package ipl.isel.daw.group8.comments.daos

import ipl.isel.daw.group8.comments.models.Comment
import ipl.isel.daw.group8.common.NotFoundException
import ipl.isel.daw.group8.issues.daos.IssueDAO
import ipl.isel.daw.group8.users.daos.UserDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

const val NUMBER_OF_USER_PROJECT_ISSUE_COMMENTS_PER_PAGE = 25

class CommentNotFoundException(id: String, value: String): NotFoundException(
    title = "Comment Not Found",
    detail = "There's no issue whose $id is $value."
)

interface CommentsListDAO
{
    @CreateSqlObject
    fun UserDAO(): UserDAO

    @CreateSqlObject
    fun IssueDAO(): IssueDAO

    @SqlQuery("SELECT * FROM \"Comment\" WHERE \"issue_id\" = ? LIMIT $NUMBER_OF_USER_PROJECT_ISSUE_COMMENTS_PER_PAGE OFFSET $NUMBER_OF_USER_PROJECT_ISSUE_COMMENTS_PER_PAGE * (? - 1)")
    @Transaction
    fun listComments(issueId: Long, page: Int): List<Comment>

    @SqlQuery("SELECT COUNT(\"id\") FROM \"Comment\" WHERE \"issue_id\" = ?")
    @Transaction
    fun countComments(issueId: Long): Long

    @Transaction
    fun listAndCountCommentsWithCredentials(userId: Long, projectId: Long, issueId: Long, page: Int, username: String, password: String): Pair<List<Comment>, Long>
    {
        IssueDAO().validateIssueAction(issueId, projectId, userId, username, password)

        val comments = listComments(issueId, page)
        val totalNumberOfComments = countComments(issueId)

        return Pair(comments, totalNumberOfComments)
    }

    @SqlQuery("INSERT INTO \"Comment\" (\"user_id\", \"project_id\", \"issue_id\", \"creator_id\", \"text\", \"createdOn\") VALUES(?, ?, ?, ?, ?, NOW()) RETURNING *")
    @Transaction
    fun registerComment(userId: Long, projectId: Long, issueId: Long, creatorId: Long, text: String): Comment

    @Transaction
    fun registerCommentWithCredentials(userId: Long, projectId: Long, issueId: Long, text: String, username: String, password: String): Comment
    {
        val creatorId = UserDAO().getAccountIdFromCredentials(username, password)

        IssueDAO().validateIssueAction(issueId, projectId, userId, username, password, creatorId)

        return registerComment(userId, projectId, issueId, creatorId, text)
    }

    @SqlQuery("SELECT TRUE FROM \"Comment\" WHERE \"id\" = ?")
    @Transaction
    fun findComment(id: Long): Boolean?

    @Transaction
    fun checkIfCommentExists(id: Long) {
        // Check if there's a comment with this id
        findComment(id) ?: throw CommentNotFoundException("id", "$id")
    }
}

@Repository
class CommentsListDAOImplementation(val jdbi: Jdbi)
{
    @Bean
    fun getCommentsListDAO(): CommentsListDAO = jdbi.onDemand()
}
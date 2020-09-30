package ipl.isel.daw.group8.issues.daos

import ipl.isel.daw.group8.common.BadRequestException
import ipl.isel.daw.group8.common.NotFoundException
import ipl.isel.daw.group8.issues.models.Issue
import ipl.isel.daw.group8.projects.daos.ProjectDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

const val NUMBER_OF_USER_PROJECT_ISSUES_PER_PAGE = 25

class IssueNotFoundException(id: String, value: String): NotFoundException(
    title = "Issue Not Found",
    detail = "There's no issue whose $id is $value."
)

class IssueInvalidLabelsException: BadRequestException(
    title = "Issue Invalid Labels",
    detail = "One or more labels are not allowed in the context of this project."
)

interface IssuesListDAO
{
    @CreateSqlObject
    fun ProjectDAO(): ProjectDAO

    @SqlQuery("SELECT \"id\", \"project_id\", \"user_id\", \"title\" FROM \"Issue\" WHERE \"project_id\" = ? LIMIT $NUMBER_OF_USER_PROJECT_ISSUES_PER_PAGE OFFSET $NUMBER_OF_USER_PROJECT_ISSUES_PER_PAGE * (? - 1)")
    @Transaction
    fun listIssues(projectId: Long, page: Int): List<Issue>

    @SqlQuery("SELECT COUNT(\"id\") FROM \"Issue\" WHERE \"project_id\" = ?")
    @Transaction
    fun countIssues(projectId: Long): Long

    @Transaction
    fun listAndCountIssuesWithCredentials(userId: Long, projectId: Long, page: Int, username: String, password: String): Pair<List<Issue>, Long>
    {
        ProjectDAO().validateProjectAction(projectId, userId, username, password)

        val list = listIssues(projectId, page)
        val totalNumberOfIssues = countIssues(projectId)

        return Pair(list, totalNumberOfIssues)
    }

    @SqlQuery("INSERT INTO \"Issue\" (\"project_id\", \"user_id\", \"title\", \"description\", \"createdOn\", \"closedOn\", \"set_labels\", \"state\") VALUES (?, ?, ?, ?, NOW(), NULL, ?, ?) RETURNING *")
    @Transaction
    fun registerIssue(projectId: Long, userId: Long, title: String, description: String, labels: Array<String>, state: String): Issue

    @Transaction
    fun registerIssueWithCredentials(userId: Long, projectId: Long, title: String, description: String, labels: Array<String>, username: String, password: String): Issue
    {
        ProjectDAO().validateProjectAction(projectId, userId, username, password)

        val allowedStates = ProjectDAO().AllowedStatesDAO().getAllowedStates(projectId)
        val allowedLabels = ProjectDAO().AllowedLabelsDAO().getAllowedLabels(projectId)

        val startState = allowedStates.entries.first { it.value.contains("*") }.key

        if(!labels.all { allowedLabels.contains(it) })
            throw IssueInvalidLabelsException()

        return registerIssue(projectId, userId, title, description, labels, startState)
    }

    @SqlQuery("SELECT TRUE FROM \"Issue\" WHERE \"id\" = ?")
    @Transaction
    fun findIssue(id: Long): Boolean?

    @Transaction
    fun checkIfIssueExists(id: Long) {
        // Check if there's an issue with this id
        findIssue(id) ?: throw IssueNotFoundException("id", "$id")
    }
}

@Repository
class IssuesListDAOImplementation(val jdbi: Jdbi)
{
    @Bean
    fun getIssuesListDAO(): IssuesListDAO = jdbi.onDemand()
}
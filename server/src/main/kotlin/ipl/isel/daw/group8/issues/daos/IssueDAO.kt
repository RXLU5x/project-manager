package ipl.isel.daw.group8.issues.daos

import ipl.isel.daw.group8.common.BadRequestException
import ipl.isel.daw.group8.common.ForbiddenException
import ipl.isel.daw.group8.issues.models.Issue
import ipl.isel.daw.group8.projects.daos.ProjectDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

class IssueAccessDeniedException(id: Long, projectId: Long): ForbiddenException(
    title = "Issue Access Denied",
    detail = "The issue with id $id doesn't belong to the project with id $projectId."
)

class InvalidStateTransitionException(oldState: String, newState: String): BadRequestException(
    title = "Invalid State Transition",
    detail = "It's not possible to transit from $oldState to $newState"
)

interface IssueDAO
{
    @CreateSqlObject
    fun ProjectDAO(): ProjectDAO

    @CreateSqlObject
    fun IssuesListDAO(): IssuesListDAO

    @SqlQuery("SELECT * FROM \"Issue\" WHERE \"id\" = ?")
    @Transaction
    fun getIssueInformation(id: Long): Issue

    @Transaction
    fun getIssueInformationWithCredentials(userId: Long, projectId: Long, id: Long, username: String, password: String): Issue
    {
        // Validate issue action
        validateIssueAction(id, projectId, userId, username, password)

        return getIssueInformation(id)
    }

    @SqlQuery("UPDATE \"Issue\" SET \"title\" = ? WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun updateIssueTitle(newTitle: String, id: Long): Issue

    @SqlQuery("UPDATE \"Issue\" SET \"description\" = ? WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun updateIssueDescription(newDescription: String, id: Long): Issue

    @SqlQuery("UPDATE \"Issue\" SET \"set_labels\" = ? WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun updateIssueLabels(newLabels: Array<String>, id: Long): Issue

    @SqlQuery("UPDATE \"Issue\" SET \"state\" = ? WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun updateIssueState(newState: String, id: Long): Issue

    @SqlQuery("UPDATE \"Issue\" SET \"state\" = 'closed', \"closedOn\" = NOW() WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun closeIssue(id: Long): Issue

    @SqlQuery("UPDATE \"Issue\" SET \"state\" = 'archived' WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun archiveIssue(id: Long): Issue

    @Transaction
    fun updateIssueInformationWithCredentials(userId: Long, projectId: Long, id: Long, username: String, password: String, newTitle: String?, newDescription: String?, newState: String?, newLabels: Array<String>): Issue
    {
        var issue = getIssueInformationWithCredentials(userId, projectId, id, username, password)

        if( (newTitle != null) && (newTitle != issue.title) )
            issue = updateIssueTitle(newTitle, id)

        if( (newDescription != null) && (newDescription != issue.description) )
            issue = updateIssueDescription(newDescription, id)

        if( (newState != null) && (newState != issue.state) )
        {
            if(newState == "closed")
                issue = closeIssue(id)
            else if(newState == "archived")
                issue = archiveIssue(id)
            else
            {
                val oldState = issue.state!!
                val allowedStateTransitions = ProjectDAO().AllowedStatesDAO().getAllowedStateTransitions(projectId, oldState) ?: throw InvalidStateTransitionException(oldState, newState)

                if(allowedStateTransitions.contains(newState) || allowedStateTransitions.contains("*"))
                    issue = updateIssueState(newState, id)
                else
                    throw InvalidStateTransitionException(oldState, newState)
            }
        }

        if(newLabels.isNotEmpty()) {
            val allowedLabels = ProjectDAO().AllowedLabelsDAO().getAllowedLabels(projectId)
            val filteredLabels = newLabels.filter { allowedLabels.contains(it) }.distinct().toTypedArray()

            issue = updateIssueLabels(filteredLabels, id)
        }

        return issue
    }

    @SqlUpdate("DELETE FROM \"Issue\" WHERE \"id\" = ?")
    @Transaction
    fun deleteIssue(id: Long)

    @Transaction
    fun deleteIssueWithCredentials(userId: Long, projectId: Long, id: Long, username: String, password: String)
    {
        // Validate issue action
        validateIssueAction(id, projectId, userId, username, password)

        deleteIssue(id)
    }

    @SqlQuery("SELECT \"project_id\" FROM \"Issue\" WHERE \"id\" = ?")
    @Transaction
    fun getIssueOwnerProjectId(id: Long): Long

    @Transaction
    fun checkOwnership(id: Long, projectId: Long)
    {
        // Get the id of the project who owns the issue with this id
        val oId = getIssueOwnerProjectId(id)

        // Check if the owner id corresponds to the project id path variable
        if(projectId != oId)
            throw IssueAccessDeniedException(id, projectId)
    }

    @Transaction
    fun validateIssueAction(id: Long, projectId: Long, userId: Long, username: String, password: String, vararg allowedAccountsIds: Long)
    {
        // Check if there's a issue with this id
        IssuesListDAO().checkIfIssueExists(id)

        // Check if the owner of the issue with this id matches the project id in the url path
        checkOwnership(id, projectId)

        ProjectDAO().validateProjectAction(projectId, userId, username, password, *allowedAccountsIds)
    }
}

@Repository
class IssueDAOImplementation(val jdbi: Jdbi)
{
    @Bean
    fun getIssueDAO(): IssueDAO = jdbi.onDemand()
}
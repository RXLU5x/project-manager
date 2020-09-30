package ipl.isel.daw.group8.projects.daos

import ipl.isel.daw.group8.common.ForbiddenException
import ipl.isel.daw.group8.projects.models.Project
import ipl.isel.daw.group8.users.daos.UserDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

class ProjectAccessDeniedException(id: Long, userId: Long): ForbiddenException(
    title = "Project Access Denied",
    detail = "The account with id $userId isn't the owner of the project with id $id."
)

interface ProjectDAO
{
    @CreateSqlObject
    fun UserDAO(): UserDAO

    @CreateSqlObject
    fun ProjectsListDAO(): ProjectsListDAO

    @CreateSqlObject
    fun AllowedStatesDAO(): AllowedStatesDAO

    @CreateSqlObject
    fun AllowedLabelsDAO(): AllowedLabelsDAO

    @SqlQuery("SELECT * FROM \"Project\" WHERE \"id\" = ?")
    @Transaction
    fun getProjectInformation(id: Long): Project

    @Transaction
    fun getProjectInformationWithCredentials(id: Long, userId: Long, username: String, password: String): Project
    {
        validateProjectAction(id, userId, username, password)

        val project = getProjectInformation(id)
        val projectAllowedStates = AllowedStatesDAO().getAllowedStates(id)

        return Project(id, userId, project.name, project.description, project.allowedLabels, projectAllowedStates)
    }

    @SqlQuery("UPDATE \"Project\" SET \"name\" = ? WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun updateProjectName(newName: String, id: Long): Project

    @SqlQuery("UPDATE \"Project\" SET \"description\" = ? WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun updateProjectDescription(newDescription: String, id: Long): Project

    @Transaction
    fun updateProjectInformationWithCredentials(id: Long, userId: Long, username: String, password: String, newName: String?, newDescription: String?): Project
    {
        var project = getProjectInformationWithCredentials(id, userId, username, password)

        val projectAllowedStates = project.allowedStates

        if( (newName != null) && (newName != project.name) ) {
            ProjectsListDAO().checkProjectNameUniqueness(newName)
            project = updateProjectName(newName, id)
        }

        if( (newDescription != null) && (newDescription != project.description) )
            project = updateProjectDescription(newDescription, id)

        project.allowedStates = projectAllowedStates

        return project
    }

    @SqlUpdate("DELETE FROM \"Project\" WHERE \"id\" = ?")
    @Transaction
    fun deleteProject(id: Long)

    @Transaction
    fun deleteProjectWithCredentials(id: Long, userId: Long, username: String, password: String)
    {
        // Validate project action
        validateProjectAction(id, userId, username, password)

        deleteProject(id)
    }

    @SqlQuery("SELECT \"user_id\" FROM \"Project\" WHERE \"id\" = ?")
    @Transaction
    fun getProjectOwnerAccountId(id: Long): Long

    @Transaction
    fun checkOwnership(id: Long, userId: Long)
    {
        // Get the id of the account who owns the project with this id
        val oId = getProjectOwnerAccountId(id)

        // Check if the owner id corresponds to the user id path variable
        if(userId != oId)
            throw ProjectAccessDeniedException(id, userId)
    }

    @Transaction
    fun validateProjectAction(id: Long, userId: Long, username: String, password: String, vararg allowedAccountsIds: Long)
    {
        // Check if there's a project with this id
        ProjectsListDAO().checkIfProjectExists(id)

        // Check if the owner of the project with this id matches the user id in the url path
        checkOwnership(id, userId)

        // Check if user has permission to perform this action
        UserDAO().validateAccountAction(username, password, userId, *allowedAccountsIds)
    }
}

@Repository
class ProjectDAOImplementation(val jdbi: Jdbi)
{
    @Bean
    fun getProjectDAO(): ProjectDAO = jdbi.onDemand()
}
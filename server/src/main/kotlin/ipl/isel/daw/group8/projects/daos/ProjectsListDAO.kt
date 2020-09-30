package ipl.isel.daw.group8.projects.daos

import ipl.isel.daw.group8.common.ConflictException
import ipl.isel.daw.group8.common.NotFoundException
import ipl.isel.daw.group8.projects.models.Project
import ipl.isel.daw.group8.users.daos.UserDAO
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

const val NUMBER_OF_PROJECTS_PER_PAGE = 25
const val NUMBER_OF_USER_PROJECTS_PER_PAGE = 25

class ProjectNameAlreadyExistsException(name: String): ConflictException(
    title = "Project Name Already Exists",
    detail = "There's already a project whose name is $name."
)

class ProjectNotFoundException(id: String, value: String): NotFoundException(
    title = "Project Not Found",
    detail = "There's no project whose $id is $value."
)

interface ProjectsListDAO
{
    @CreateSqlObject
    fun UserDAO(): UserDAO

    @CreateSqlObject
    fun AllowedStatesDAO(): AllowedStatesDAO

    @SqlQuery("SELECT \"id\", \"user_id\", \"name\" FROM \"Project\" LIMIT $NUMBER_OF_PROJECTS_PER_PAGE OFFSET $NUMBER_OF_PROJECTS_PER_PAGE * (? - 1)")
    @Transaction
    fun listProjects(page: Int): List<Project>

    @SqlQuery("SELECT COUNT(\"id\") FROM \"Project\"")
    @Transaction
    fun countProjects(): Long

    @Transaction
    fun listAndCountProjects(page: Int): Pair<List<Project>, Long>
    {
        val list = listProjects(page)
        val totalNumberOfProjects = countProjects()

        return Pair(list, totalNumberOfProjects)
    }

    @SqlQuery("SELECT \"id\", \"user_id\", \"name\" FROM \"Project\" WHERE \"user_id\" = ? LIMIT $NUMBER_OF_USER_PROJECTS_PER_PAGE OFFSET $NUMBER_OF_USER_PROJECTS_PER_PAGE * (? - 1)")
    @Transaction
    fun listProjects(userId: Long, page: Int): List<Project>

    @SqlQuery("SELECT COUNT(\"id\") FROM \"Project\" WHERE \"user_id\" = ?")
    @Transaction
    fun countProjects(userId: Long): Long

    @Transaction
    fun listAndCountProjectsWithCredentials(userId: Long, page: Int, username: String, password: String): Pair<List<Project>, Long>
    {
        // Action Validation
        UserDAO().validateAccountAction(username, password, userId)

        val list = listProjects(userId, page)
        val totalNumberOfAccounts = countProjects(userId)

        return Pair(list, totalNumberOfAccounts)
    }

    @SqlQuery("INSERT INTO \"Project\" (\"user_id\", \"name\", \"description\", \"allowed_labels\") VALUES (?, ?, ?, ?) RETURNING *")
    @Transaction
    fun registerProject(userId: Long, name: String, description: String, allowedLabels: Array<String>): Project

    @Transaction
    fun registerProjectWithCredentials(userId: Long, username: String, password: String, name: String, description: String, allowedLabels: Array<String>, allowedStates: Map<String, Array<String>>): Project
    {
        // Validate account action
        UserDAO().validateAccountAction(username, password, userId)

        // Check Project Name Uniqueness
        checkProjectNameUniqueness(name)

        // Register Project
        val project = registerProject(userId, name, description, allowedLabels)

        // Register Allowed States
        AllowedStatesDAO().registerAllowedStates(project.id, userId, allowedStates.entries.map { it.key },  allowedStates.entries.map { it.value })

        project.allowedStates = allowedStates

        return project
    }

    @SqlQuery("SELECT TRUE FROM \"Project\" WHERE \"name\" = ?")
    @Transaction
    fun findProject(name: String): Boolean?

    @Transaction
    fun checkProjectNameUniqueness(name: String)
    {
        // Check if there's already a project with this name
        val found = findProject(name) ?: false

        if(found)
            throw ProjectNameAlreadyExistsException(name)
    }

    @SqlQuery("SELECT TRUE FROM \"Project\" WHERE \"id\" = ?")
    @Transaction
    fun findProject(id: Long): Boolean?

    @Transaction
    fun checkIfProjectExists(id: Long) {
        // Check if there's a project with this id
        findProject(id) ?: throw ProjectNotFoundException("id", "$id")
    }
}

@Repository
class ProjectsListDAOImplementation(val jdbi: Jdbi)
{
    @Bean
    fun getProjectsListDAO(): ProjectsListDAO = jdbi.onDemand()
}
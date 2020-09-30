package ipl.isel.daw.group8.projects.services

import ipl.isel.daw.group8.projects.daos.ProjectsListDAO
import org.springframework.stereotype.Service

@Service
class ProjectListServices(val dao: ProjectsListDAO)
{
    fun listProjects(page: Int) =
            dao.listAndCountProjects(page)

    fun listUserProjects(userId: Long, page: Int, username: String, password: String) =
            dao.listAndCountProjectsWithCredentials(userId, page, username, password)

    fun registerProject(userId: Long, username: String, password: String, name: String, description: String, allowedLabels: Array<String>, allowedStates: Map<String, Array<String>>) =
            dao.registerProjectWithCredentials(userId, username, password, name, description, allowedLabels, allowedStates)
}
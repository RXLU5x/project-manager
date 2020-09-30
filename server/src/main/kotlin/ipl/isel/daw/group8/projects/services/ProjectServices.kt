package ipl.isel.daw.group8.projects.services

import ipl.isel.daw.group8.projects.daos.ProjectDAO
import org.springframework.stereotype.Service

@Service
class ProjectServices(val dao: ProjectDAO)
{
    fun getProjectInformation(id: Long, userId: Long, username: String, password: String) =
            dao.getProjectInformationWithCredentials(id, userId, username, password)

    fun updateProjectInformation(id: Long, userId: Long, username: String, password: String, newName: String?, newDescription: String?) =
            dao.updateProjectInformationWithCredentials(id, userId, username, password, newName, newDescription)

    fun deleteProject(id: Long, userId: Long, username: String, password: String) =
            dao.deleteProjectWithCredentials(id, userId, username, password)
}

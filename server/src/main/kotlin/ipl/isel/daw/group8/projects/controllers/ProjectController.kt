package ipl.isel.daw.group8.projects.controllers

import ipl.isel.daw.group8.common.BasicAuthentication
import ipl.isel.daw.group8.common.SIREN_MEDIA_TYPE_VALUE
import ipl.isel.daw.group8.common.SirenEntity
import ipl.isel.daw.group8.common.USER_PROJECT_PATH
import ipl.isel.daw.group8.projects.models.ProjectInputModel.ProjectUpdateInputModel
import ipl.isel.daw.group8.projects.models.ProjectOutputModel
import ipl.isel.daw.group8.projects.models.toOutputModel
import ipl.isel.daw.group8.projects.services.ProjectServices
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = [USER_PROJECT_PATH], produces = [SIREN_MEDIA_TYPE_VALUE])
class ProjectController(val service: ProjectServices)
{
    @GetMapping
    fun getProjectInformation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") id: Long,
        @RequestHeader("Authorization") authorization: String
    ): SirenEntity<ProjectOutputModel>
    {
        val (username, password) = BasicAuthentication(authorization).parseHeader()

        return service.getProjectInformation(id, userId, username, password)
                .toOutputModel()
                .toSirenObject()
    }

    @PostMapping
    fun updateProjectInformation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") id: Long,
        @RequestHeader("Authorization") authorization: String,
        @RequestBody body: ProjectUpdateInputModel
    ): SirenEntity<ProjectOutputModel>
    {
        val (username, password) = BasicAuthentication(authorization).parseHeader()
        val (newName, newDescription) = body.validateUpdate()

        return service.updateProjectInformation(id, userId, username, password, newName, newDescription)
                .toOutputModel()
                .toSirenObject()
    }

    @DeleteMapping
    fun deleteProject(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") id: Long,
        @RequestHeader("Authorization") authorization: String
    )
    {
        val (username, password) = BasicAuthentication(authorization).parseHeader()

        return service.deleteProject(id, userId, username, password)
    }
}
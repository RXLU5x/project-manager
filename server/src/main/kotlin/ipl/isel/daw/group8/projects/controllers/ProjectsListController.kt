package ipl.isel.daw.group8.projects.controllers

import ipl.isel.daw.group8.common.*
import ipl.isel.daw.group8.projects.models.ProjectInputModel.ProjectRegistryInputModel
import ipl.isel.daw.group8.projects.models.ProjectOutputModel
import ipl.isel.daw.group8.projects.models.ProjectsListOutputModel
import ipl.isel.daw.group8.projects.models.UserProjectsListOutputModel
import ipl.isel.daw.group8.projects.models.toOutputModel
import ipl.isel.daw.group8.projects.services.ProjectListServices
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(produces = [SIREN_MEDIA_TYPE_VALUE])
class ProjectsListController(val service: ProjectListServices)
{
    @GetMapping(value = [USERS_PROJECTS_LIST_PATH])
    fun listProjects(
        @RequestParam(name = "page", defaultValue = "1") page: Int
    ): SirenEntity<ProjectsListOutputModel>
    {
        if(page <= 0)
            throw InvalidPageParameterException()

        val (projects, totalNumberOfProjects) =
                service.listProjects(page)

        val projectsAsSirenSubEntities = projects.map {
            it.toOutputModel()
        }.map {
            it.toSirenObject(
                deleteProjectAction = false,
                updateProjectInformationAction = false,
                issuesListEmbeddedLink = false
            )
        }.map { it.toEmbeddedRepresentation(listOf("projects")) }

        return ProjectsListOutputModel(totalNumberOfProjects, page, projects.size)
                .toSirenObject(subEntities = projectsAsSirenSubEntities)
    }

    @GetMapping(value = [USER_PROJECTS_LIST_PATH])
    fun listProjects(
        @PathVariable("user_id") userId: Long,
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestHeader("Authorization") authorization: String
    ): SirenEntity<UserProjectsListOutputModel>
    {
        if(page <= 0)
            throw InvalidPageParameterException()

        val (username, password) = BasicAuthentication(authorization).parseHeader()

        val (projects, totalNumberOfProjects) =
                service.listUserProjects(userId, page, username, password)

        val projectsAsSirenSubEntities = projects.map {
            it.toOutputModel()
        }.map {
            it.toSirenObject(
                deleteProjectAction = false,
                updateProjectInformationAction = false,
                issuesListEmbeddedLink = false
            )
        }.map {
            it.toEmbeddedRepresentation(listOf("project"))
        }

        return UserProjectsListOutputModel(userId, totalNumberOfProjects, page, projects.size)
                .toSirenObject(subEntities = projectsAsSirenSubEntities)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = [USER_PROJECTS_LIST_PATH], consumes = [JSON_MEDIA_TYPE_VALUE])
    fun registerProject(
        @PathVariable("user_id") userId: Long,
        @RequestHeader("Authorization") authorization: String,
        @RequestBody body: ProjectRegistryInputModel
    ): SirenEntity<ProjectOutputModel>
    {
        val (username, password) = BasicAuthentication(authorization).parseHeader()
        val (name, description, allowedLabels, allowedStates) = body.validateRegistry()

        return service.registerProject(userId, username, password, name, description, allowedLabels, allowedStates)
                .toOutputModel()
                .toSirenObject()
    }
}
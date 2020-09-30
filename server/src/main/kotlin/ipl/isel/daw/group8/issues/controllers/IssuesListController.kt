package ipl.isel.daw.group8.issues.controllers

import ipl.isel.daw.group8.common.*
import ipl.isel.daw.group8.issues.models.IssueInputModel.IssueRegistryInputModel
import ipl.isel.daw.group8.issues.models.IssueListOutputModel
import ipl.isel.daw.group8.issues.models.IssueOutputModel
import ipl.isel.daw.group8.issues.models.toOutputModel
import ipl.isel.daw.group8.issues.services.IssuesListServices
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = [USER_PROJECT_ISSUES_LIST_PATH], produces = [SIREN_MEDIA_TYPE_VALUE])
class IssuesListController(val service: IssuesListServices)
{
    @GetMapping
    fun listIssues(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") projectId: Long,
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestHeader("Authorization") authorization: String
    ): SirenEntity<IssueListOutputModel> {
        if(page <= 0)
            throw InvalidPageParameterException()

        val (username, password) = BasicAuthentication(authorization).parseHeader()

        val (issues, totalNumberOfIssues) =
                service.listIssues(userId, projectId, page, username, password)

        val issuesAsSirenEntities = issues.map {
            it.toOutputModel()
        }.map {
            it.toSirenObject(
                deleteIssueAction = false,
                updateIssueInformation = false,
                commentsListEmbeddedLink = false
            )
        }.map {
            it.toEmbeddedRepresentation(listOf("issue"))
        }

        return IssueListOutputModel(userId, projectId, totalNumberOfIssues, page, issues.size)
                .toSirenObject(subEntities = issuesAsSirenEntities)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [JSON_MEDIA_TYPE_VALUE])
    fun registerIssue(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") projectId: Long,
        @RequestHeader("Authorization") authorization: String,
        @RequestBody body: IssueRegistryInputModel
    ): SirenEntity<IssueOutputModel> {
        val (username, password) = BasicAuthentication(authorization).parseHeader()
        val (title, description, labels) = body.validateRegistry()

        return service.registerIssue(userId, projectId, title, description, labels, username, password)
                .toOutputModel()
                .toSirenObject()
    }
}
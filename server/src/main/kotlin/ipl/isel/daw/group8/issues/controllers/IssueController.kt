package ipl.isel.daw.group8.issues.controllers

import ipl.isel.daw.group8.common.BasicAuthentication
import ipl.isel.daw.group8.common.SIREN_MEDIA_TYPE_VALUE
import ipl.isel.daw.group8.common.SirenEntity
import ipl.isel.daw.group8.common.USER_PROJECT_ISSUE_PATH
import ipl.isel.daw.group8.issues.models.IssueInputModel.IssueUpdateInputModel
import ipl.isel.daw.group8.issues.models.IssueOutputModel
import ipl.isel.daw.group8.issues.models.toOutputModel
import ipl.isel.daw.group8.issues.services.IssueServices
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    path = [USER_PROJECT_ISSUE_PATH],
    produces = [SIREN_MEDIA_TYPE_VALUE]
) class IssueController(val service: IssueServices)
{
    @GetMapping
    fun getIssueInformation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") projectId: Long,
        @PathVariable("issue_id") id: Long,
        @RequestHeader("Authorization") authorization: String
    ): SirenEntity<IssueOutputModel> {
        val (username, password) = BasicAuthentication(authorization).parseHeader()

        return service.getIssueInformation(userId, projectId, id, username, password)
                .toOutputModel()
                .toSirenObject()
    }

    @PostMapping
    fun updateIssueInformation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") projectId: Long,
        @PathVariable("issue_id") id: Long,
        @RequestHeader("Authorization") authorization: String,
        @RequestBody body: IssueUpdateInputModel
    ): SirenEntity<IssueOutputModel> {
        val (username, password) = BasicAuthentication(authorization).parseHeader()
        val (newTitle, newDescription, newState, newLabels) = body.validateUpdate()

        return service.updateIssueInformation(userId, projectId, id, username, password, newTitle, newDescription, newState, newLabels)
                .toOutputModel()
                .toSirenObject()
    }

    @DeleteMapping
    fun deleteIssue(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") projectId: Long,
        @PathVariable("issue_id") id: Long,
        @RequestHeader("Authorization") authorization: String
    ) {
        val (username, password) = BasicAuthentication(authorization).parseHeader()

        service.deleteIssue(userId, projectId, id, username, password)
    }
}
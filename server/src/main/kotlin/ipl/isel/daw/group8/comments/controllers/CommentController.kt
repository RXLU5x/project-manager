package ipl.isel.daw.group8.comments.controllers

import ipl.isel.daw.group8.comments.models.CommentInputModel.CommentUpdateInputModel
import ipl.isel.daw.group8.comments.models.CommentOutputModel
import ipl.isel.daw.group8.comments.models.toOutputModel
import ipl.isel.daw.group8.comments.services.CommentServices
import ipl.isel.daw.group8.common.BasicAuthentication
import ipl.isel.daw.group8.common.SIREN_MEDIA_TYPE_VALUE
import ipl.isel.daw.group8.common.SirenEntity
import ipl.isel.daw.group8.common.USER_PROJECT_ISSUE_COMMENT_PATH
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    value = [USER_PROJECT_ISSUE_COMMENT_PATH],
    produces = [SIREN_MEDIA_TYPE_VALUE]
) class CommentController(val service: CommentServices)
{
    @PostMapping
    fun updateCommentText(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") projectId: Long,
        @PathVariable("issue_id") issueId: Long,
        @PathVariable("comment_id") id: Long,
        @RequestHeader("Authorization") authorization: String,
        @RequestBody body: CommentUpdateInputModel
    ): SirenEntity<CommentOutputModel> {
        val (username, password) = BasicAuthentication(authorization).parseHeader()

        val newText = body.validateUpdate()

        return service.updateCommentText(userId, projectId, issueId, id, username, password, newText)
                .toOutputModel()
                .toSirenObject()
    }

    @DeleteMapping
    fun deleteComment(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") projectId: Long,
        @PathVariable("issue_id") issueId: Long,
        @PathVariable("comment_id") id: Long,
        @RequestHeader("Authorization") authorization: String
    ) {
        val (username, password) = BasicAuthentication(authorization).parseHeader()

        return service.deleteComment(userId, projectId, issueId, id, username, password)
    }
}

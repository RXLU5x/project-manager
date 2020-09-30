package ipl.isel.daw.group8.comments.controllers

import ipl.isel.daw.group8.comments.models.CommentInputModel.CommentRegistryInputModel
import ipl.isel.daw.group8.comments.models.CommentOutputModel
import ipl.isel.daw.group8.comments.models.CommentsListOutputModel
import ipl.isel.daw.group8.comments.models.toOutputModel
import ipl.isel.daw.group8.comments.services.CommentsListServices
import ipl.isel.daw.group8.common.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
   value = [USER_PROJECT_ISSUE_COMMENTS_LIST_PATH],
   produces = [SIREN_MEDIA_TYPE_VALUE]
) class CommentsListController(val service: CommentsListServices)
{
     @GetMapping
     fun listComments(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") projectId: Long,
        @PathVariable("issue_id") issueId: Long,
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestHeader("Authorization") authorization: String
     ): SirenEntity<CommentsListOutputModel>
     {
          if(page <= 0)
               throw InvalidPageParameterException()

          val (username, password) =
                  BasicAuthentication(authorization).parseHeader()

          val (comments, totalNumberOfComments) =
                  service.listComments(userId, projectId, issueId, page, username, password)

          val commentsAsSirenSubEntities = comments.map {
               it.toOutputModel()
          }.map {
               it.toSirenObject(
                  deleteCommentAction = false,
                  updateCommentTextAction = false
               )
          }.map {
               it.toEmbeddedRepresentation(listOf("comment"))
          }

          return CommentsListOutputModel(userId, projectId, issueId, totalNumberOfComments, page, comments.size)
                  .toSirenObject(subEntities = commentsAsSirenSubEntities)
     }

     @ResponseStatus(HttpStatus.CREATED)
     @PostMapping(consumes = [JSON_MEDIA_TYPE_VALUE])
     fun registerComment(
        @PathVariable("user_id") userId: Long,
        @PathVariable("project_id") projectId: Long,
        @PathVariable("issue_id") issueId: Long,
        @RequestHeader("Authorization") authorization: String,
        @RequestBody body: CommentRegistryInputModel
     ): SirenEntity<CommentOutputModel>
     {
          val (username, password) = BasicAuthentication(authorization).parseHeader()
          val text = body.validateRegistry()

          return service.registerComment(userId, projectId, issueId, text, username, password)
                  .toOutputModel()
                  .toSirenObject()
     }
}
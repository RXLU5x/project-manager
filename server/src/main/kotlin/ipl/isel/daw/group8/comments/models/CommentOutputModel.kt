package ipl.isel.daw.group8.comments.models

import ipl.isel.daw.group8.common.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI
import java.time.LocalDate

class CommentOutputModel(val id: Long, val issueId: Long, val projectId: Long, val userId: Long, val creatorId: Long, val text: String?, createdOn: LocalDate?)
{
    private fun generateSelfLink() =
            SirenLink(
                rel = listOf("self"),
                href = URI(USER_PROJECT_ISSUE_COMMENT_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$issueId").replace("{comment_id}", "$id"))
            )

    private fun generateUpdateCommentTextAction(): SirenAction
    {
        val updateCommentTextFields = listOf(
            SirenAction.Field(
                name = "newText",
                type = "text"
            )
        )

        return SirenAction(
            name = "update-comment-text",
            title = "Update Comment Text",
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            href = URI(USER_PROJECT_ISSUE_COMMENT_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$issueId").replace("{comment_id}", "$id")),
            fields = updateCommentTextFields
        )
    }

    private fun generateDeleteCommentAction(): SirenAction
    {
        return SirenAction(
            name = "delete-comment",
            title = "Delete Comment",
            method = HttpMethod.DELETE,
            href = URI(USER_PROJECT_ISSUE_COMMENT_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$issueId").replace("{comment_id}", "$id"))
        )
    }

    fun toSirenObject(
        deleteCommentAction: Boolean = true,
        updateCommentTextAction: Boolean = true
    ): SirenEntity<CommentOutputModel> {
        val actions = mutableListOf<SirenAction>()
        val embeddedLinks = mutableListOf<SirenSubEntity.EmbeddedLink>()

        if(deleteCommentAction)
            actions.add(generateDeleteCommentAction())

        if(updateCommentTextAction)
            actions.add(generateUpdateCommentTextAction())

        return SirenEntity(
            clazz = listOf("user", "project", "issue", "comment", "info"),
            properties = this,
            entities = if(embeddedLinks.isEmpty()) null else embeddedLinks,
            links = listOf(generateSelfLink()),
            actions = if(actions.isEmpty()) null else actions
        )
    }
}
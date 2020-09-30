package ipl.isel.daw.group8.comments.models

import ipl.isel.daw.group8.comments.daos.NUMBER_OF_USER_PROJECT_ISSUE_COMMENTS_PER_PAGE
import ipl.isel.daw.group8.common.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

class CommentsListOutputModel(val userId: Long, val projectId: Long, val issueId: Long, val totalNumberOfComments: Long, val pageNumber: Int, val numberOfComments: Int)
{
    private fun generateLinks(): MutableList<SirenLink>
    {
        val links = mutableListOf<SirenLink>()

        if(pageNumber != 1 && numberOfComments != 0)
            links.add(
                SirenLink(
                    rel = listOf("previous"),
                    href = URI("${USER_PROJECT_ISSUE_COMMENTS_LIST_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$issueId")}?page=${pageNumber - 1}")
                )
            )

        links.add(
            SirenLink(
                rel = listOf("self"),
                href = URI("${USER_PROJECT_ISSUE_COMMENTS_LIST_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$issueId")}?page=$pageNumber")
            )
        )

        if(numberOfComments == NUMBER_OF_USER_PROJECT_ISSUE_COMMENTS_PER_PAGE)
            links.add(
                SirenLink(
                    rel = listOf("next"),
                    href = URI("${USER_PROJECT_ISSUE_COMMENTS_LIST_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$issueId")}?page=${pageNumber + 1}")
                )
            )

        return links
    }

    private fun generateRegisterCommentAction(): SirenAction
    {
        val fields = listOf(
            SirenAction.Field(
                name = "text",
                type = "text"
            )
        )

        return SirenAction(
            name = "register-comment",
            title = "Register Comment",
            method = HttpMethod.POST,
            href = URI(USER_PROJECT_ISSUE_COMMENTS_LIST_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$issueId")),
            type = MediaType.APPLICATION_JSON,
            fields = fields
        )
    }

    fun toSirenObject(
        subEntities: List<SirenSubEntity>
    ): SirenEntity<CommentsListOutputModel>
    {
        return SirenEntity(
            clazz = listOf("user", "project", "issue", "comment", "collection"),
            properties = this,
            entities = if(subEntities.isEmpty()) null else subEntities,
            links = generateLinks(),
            actions = listOf(generateRegisterCommentAction())
        )
    }
}
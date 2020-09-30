package ipl.isel.daw.group8.issues.models

import ipl.isel.daw.group8.common.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI
import java.time.LocalDate

class IssueOutputModel(val id: Long, val projectId: Long, val userId: Long, val title: String?, val description: String?, val createdOn: LocalDate?, val closedOn: LocalDate?, val labels: Array<String>?, val state: String?)
{
    private fun generateSelfLink() =
            SirenLink(rel = listOf("self"), href = URI(USER_PROJECT_ISSUE_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$id")))

    private fun generateCommentsListEmbeddedLink() =
            SirenSubEntity.EmbeddedLink(clazz = listOf("comments", "collection"), rel = listOf("user-project-issue-comments"), href = URI(USER_PROJECT_ISSUE_COMMENTS_LIST_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$id")))

    private fun generateUpdateIssueInformationAction(): SirenAction
    {
        val updateIssueInformationFields = listOf(
            SirenAction.Field(
                name = "newTitle",
                type = "text"
            ),

            SirenAction.Field(
                name = "newDescription",
                type = "text"
            ),

            SirenAction.Field(
                name = "newLabels",
                type = "text"
            ),

            SirenAction.Field(
                name = "newState",
                type = "text"
            )
        )

        return SirenAction(
            name = "update-issue-information",
            title = "Update Project Issue Information",
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            href = URI(USER_PROJECT_ISSUE_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$id")),
            fields = updateIssueInformationFields
        )
    }

    private fun generateDeleteProjectAction(): SirenAction
    {
        return SirenAction(
            name = "delete-issue",
            title = "Delete Project Issue",
            method = HttpMethod.DELETE,
            href = URI(USER_PROJECT_ISSUE_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId").replace("{issue_id}", "$id"))
        )
    }

    fun toSirenObject(
        deleteIssueAction: Boolean = true,
        updateIssueInformation: Boolean = true,
        commentsListEmbeddedLink: Boolean = true
    ): SirenEntity<IssueOutputModel> {
        val actions = mutableListOf<SirenAction>()
        val embeddedLinks = mutableListOf<SirenSubEntity.EmbeddedLink>()

        if(deleteIssueAction)
            actions.add(generateDeleteProjectAction())

        if(updateIssueInformation)
            actions.add(generateUpdateIssueInformationAction())

        if(commentsListEmbeddedLink)
            embeddedLinks.add(generateCommentsListEmbeddedLink())

        return SirenEntity(
            clazz = listOf("info", "user", "project", "issue"),
            properties = this,
            entities = if(embeddedLinks.isEmpty()) null else embeddedLinks,
            links = listOf(generateSelfLink()),
            actions = if(actions.isEmpty()) null else actions
        )
    }
}
package ipl.isel.daw.group8.projects.models

import ipl.isel.daw.group8.common.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

class ProjectOutputModel(val id: Long, val userId: Long, val name: String?, val description: String?, val allowedLabels: Array<String>?, val allowedStates: Map<String, Array<String>>?)
{
    private fun generateSelfLink() =
            SirenLink(rel = listOf("self"), href = URI(USER_PROJECT_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$id")))

    private fun generateIssuesListEmbeddedLink() =
            SirenSubEntity.EmbeddedLink(clazz = listOf("issues", "collection"), rel = listOf("user-project-issues"), href = URI(USER_PROJECT_ISSUES_LIST_PATH.replace("{project_id}", "$id").replace("{user_id}", "$userId")))

    private fun generateUpdateProjectInformationAction(): SirenAction
    {
        val updateProjectInformationFields = listOf(
            SirenAction.Field(
                name = "newName",
                type = "text"
            ),

            SirenAction.Field(
                name = "newDescription",
                type = "text"
            )
        )

        return SirenAction(
            name = "update-project-information",
            title = "Update Project Information",
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            href = URI(USER_PROJECT_PATH.replace("{project_id}", "$id").replace("{user_id}", "$userId")),
            fields = updateProjectInformationFields
        )
    }

    private fun generateDeleteProjectAction(): SirenAction
    {
        return SirenAction(
            name = "delete-project",
            title = "Delete Project",
            method = HttpMethod.DELETE,
            href = URI(USER_PROJECT_PATH.replace("{project_id}", "$id").replace("{user_id}", "$userId"))
        )
    }

    fun toSirenObject(
        deleteProjectAction: Boolean = true,
        updateProjectInformationAction: Boolean = true,
        issuesListEmbeddedLink: Boolean = true
    ): SirenEntity<ProjectOutputModel> {
        val actions = mutableListOf<SirenAction>()
        val embeddedLinks = mutableListOf<SirenSubEntity.EmbeddedLink>()

        if(deleteProjectAction)
            actions.add(generateDeleteProjectAction())

        if(updateProjectInformationAction)
            actions.add(generateUpdateProjectInformationAction())

        if(issuesListEmbeddedLink)
            embeddedLinks.add(generateIssuesListEmbeddedLink())

        return SirenEntity(
            clazz = listOf("user", "project", "info"),
            properties = this,
            entities = if(embeddedLinks.isEmpty()) null else embeddedLinks,
            links = listOf(generateSelfLink()),
            actions = if(actions.isEmpty()) null else actions
        )
    }
}
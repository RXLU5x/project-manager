package ipl.isel.daw.group8.users.models

import ipl.isel.daw.group8.common.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

class UserOutputModel(val id: Long, val username: String?, val password: String?)
{
    private fun generateSelfLink() =
        SirenLink(rel = listOf("self"), href = URI(USER_PATH.replace("{user_id}", "$id")))

    private fun generateProjectsListEmbeddedLink() =
        SirenSubEntity.EmbeddedLink(clazz = listOf("projects", "collection"), rel = listOf("user-projects"), href = URI(USER_PROJECTS_LIST_PATH.replace("{user_id}", "$id")))

    private fun generateUpdateAccountInformationAction(): SirenAction
    {
        val updateAccountInformationFields = listOf(
            SirenAction.Field(
                name = "newUsername",
                type = "text"
            ),

            SirenAction.Field(
                name = "newPassword",
                type = "text"
            )
        )

        return SirenAction(
            name = "update-account-information",
            title = "Update Account Information",
            method = HttpMethod.POST,
            type = MediaType.APPLICATION_JSON,
            href = URI(USER_PATH.replace("{user_id}", "$id")),
            fields = updateAccountInformationFields
        )
    }

    private fun generateDeleteAccountAction(): SirenAction
    {
        return SirenAction(
            name = "delete-account",
            title = "Delete Account",
            method = HttpMethod.DELETE,
            href = URI(USER_PATH.replace("{user_id}", "$id"))
        )
    }

    fun toSirenObject(
        deleteAccountAction: Boolean = true,
        updateAccountInformationAction: Boolean = true,
        projectsListEmbeddedLink: Boolean = true
    ): SirenEntity<UserOutputModel> {
        val actions = mutableListOf<SirenAction>()
        val embeddedLinks = mutableListOf<SirenSubEntity.EmbeddedLink>()

        if(deleteAccountAction)
            actions.add(generateDeleteAccountAction())

        if(updateAccountInformationAction)
            actions.add(generateUpdateAccountInformationAction())

        if(projectsListEmbeddedLink)
            embeddedLinks.add(generateProjectsListEmbeddedLink())

        return SirenEntity(
            clazz = listOf("info", "user"),
            properties = this,
            entities = if(embeddedLinks.isEmpty()) null else embeddedLinks,
            links = listOf(generateSelfLink()),
            actions = if(actions.isEmpty()) null else actions
        )
    }
}
package ipl.isel.daw.group8.projects.models

import ipl.isel.daw.group8.common.*
import ipl.isel.daw.group8.projects.daos.NUMBER_OF_PROJECTS_PER_PAGE
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

class UserProjectsListOutputModel(val userId: Long, val totalNumberOfProjects: Long, val pageNumber: Int, val numberOfProjects: Int)
{
    private fun generateLinks(): MutableList<SirenLink>
    {
        val links = mutableListOf<SirenLink>()

        if(pageNumber != 1 && numberOfProjects != 0)
            links.add(
                SirenLink(
                    rel = listOf("previous"),
                    href = URI("${USER_PROJECTS_LIST_PATH.replace("{user_id}", "$userId")}?page=${pageNumber - 1}")
                )
            )

        links.add(
            SirenLink(
                rel = listOf("self"),
                href = URI("${USER_PROJECTS_LIST_PATH.replace("{user_id}", "$userId")}?page=$pageNumber")
            )
        )

        if(numberOfProjects == NUMBER_OF_PROJECTS_PER_PAGE)
            links.add(
                SirenLink(
                    rel = listOf("next"),
                    href = URI("${USER_PROJECTS_LIST_PATH.replace("{user_id}", "$userId")}?page=${pageNumber + 1}")
                )
            )

        return links
    }

    private fun generateRegisterProjectAction(): SirenAction
    {
        val fields = listOf(
            SirenAction.Field(
                name = "name",
                type = "text"
            ),

            SirenAction.Field(
                name = "description",
                type = "text"
            ),

            SirenAction.Field(
                name = "allowedLabels",
                type = "strings-array"
            ),

            SirenAction.Field(
                name = "allowedStates",
                type = "string-to-strings-array-map"
            )
        )

        return SirenAction(
            name = "register-project",
            title = "Register Project",
            method = HttpMethod.POST,
            href = URI(USER_PROJECTS_LIST_PATH.replace("{user_id}", "$userId")),
            type = MediaType.APPLICATION_JSON,
            fields = fields
        )
    }

    fun toSirenObject(
        subEntities: List<SirenSubEntity>
    ): SirenEntity<UserProjectsListOutputModel>
    {
        return SirenEntity(
            clazz = listOf("user", "projects", "collection"),
            properties = this,
            entities = subEntities,
            links = generateLinks(),
            actions = listOf(generateRegisterProjectAction())
        )
    }
}
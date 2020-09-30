package ipl.isel.daw.group8.issues.models

import ipl.isel.daw.group8.common.*
import ipl.isel.daw.group8.issues.daos.NUMBER_OF_USER_PROJECT_ISSUES_PER_PAGE
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

class IssueListOutputModel(val userId: Long, val projectId: Long, val totalNumberOfIssues: Long, val pageNumber: Int, val numberOfIssues: Int)
{
    private fun generateLinks(): MutableList<SirenLink>
    {
        val links = mutableListOf<SirenLink>()

        if(pageNumber != 1 && numberOfIssues != 0)
            links.add(
                SirenLink(
                    rel = listOf("previous"),
                    href = URI("${USER_PROJECT_ISSUES_LIST_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId")}?page=${pageNumber - 1}")
                )
            )

        links.add(
            SirenLink(
                rel = listOf("self"),
                href = URI("${USER_PROJECT_ISSUES_LIST_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId")}?page=$pageNumber")
            )
        )

        if(numberOfIssues == NUMBER_OF_USER_PROJECT_ISSUES_PER_PAGE)
            links.add(
                SirenLink(
                    rel = listOf("next"),
                    href = URI("${USER_PROJECT_ISSUES_LIST_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId")}?page=${pageNumber + 1}")
                )
            )

        return links
    }

    private fun generateRegisterProjectAction(): SirenAction
    {
        val fields = listOf(
            SirenAction.Field(
                name = "title",
                type = "text"
            ),

            SirenAction.Field(
                name = "description",
                type = "text"
            ),

            SirenAction.Field(
                name = "labels",
                type = "array"
            )
        )

        return SirenAction(
            name = "register-issue",
            title = "Register Issue",
            method = HttpMethod.POST,
            href = URI(USER_PROJECT_ISSUES_LIST_PATH.replace("{user_id}", "$userId").replace("{project_id}", "$projectId")),
            type = MediaType.APPLICATION_JSON,
            fields = fields
        )
    }

    fun toSirenObject(
        subEntities: List<SirenSubEntity>
    ): SirenEntity<IssueListOutputModel>
    {
        return SirenEntity(
            clazz = listOf("user", "project", "issues", "collection"),
            properties = this,
            entities = subEntities,
            links = generateLinks(),
            actions = listOf(generateRegisterProjectAction())
        )
    }
}
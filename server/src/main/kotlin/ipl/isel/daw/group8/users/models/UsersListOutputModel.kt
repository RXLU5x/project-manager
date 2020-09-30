package ipl.isel.daw.group8.users.models

import ipl.isel.daw.group8.common.*
import ipl.isel.daw.group8.users.daos.NUMBER_OF_ACCOUNTS_PER_PAGE
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

class UsersListOutputModel(val totalNumberOfAccounts: Long, val pageNumber: Int, val numberOfAccounts: Int)
{
    private fun generateLinks(): MutableList<SirenLink>
    {
        val links = mutableListOf<SirenLink>()

        if(pageNumber != 1 && numberOfAccounts != 0)
            links.add(
                SirenLink(
                    rel = listOf("previous"),
                    href = URI("$USERS_LIST_PATH?page=${pageNumber - 1}")
                )
            )

        links.add(
            SirenLink(
                rel = listOf("self"),
                href = URI("$USERS_LIST_PATH?page=$pageNumber")
            )
        )

        if(numberOfAccounts == NUMBER_OF_ACCOUNTS_PER_PAGE)
            links.add(
                SirenLink(
                    rel = listOf("next"),
                    href = URI("$USERS_LIST_PATH?page=${pageNumber + 1}")
                )
            )

        return links
    }

    private fun generateRegisterAccountAction(): SirenAction
    {
        val fields = listOf(
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
            name = "register-account",
            title = "Register Account",
            method = HttpMethod.POST,
            href = URI(USERS_LIST_PATH),
            type = MediaType.APPLICATION_JSON,
            fields = fields
        )
    }

    fun toSirenObject(
        subEntities: List<SirenSubEntity>
    ): SirenEntity<UsersListOutputModel>
    {
        return SirenEntity(
            clazz = listOf("user", "collection"),
            properties = this,
            entities = if(subEntities.isEmpty()) null else subEntities,
            links = generateLinks(),
            actions = listOf(generateRegisterAccountAction())
        )
    }
}
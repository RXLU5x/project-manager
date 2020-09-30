package ipl.isel.daw.group8.users.models

import ipl.isel.daw.group8.common.SirenEntity
import ipl.isel.daw.group8.common.SirenLink
import ipl.isel.daw.group8.common.SirenSubEntity
import ipl.isel.daw.group8.common.USERS_LIST_PATH
import ipl.isel.daw.group8.users.daos.NUMBER_OF_ACCOUNTS_PER_PAGE
import java.net.URI

class UsersListSearchOutputModel(val searchedUsername: String, val totalNumberOfMatches: Long, val pageNumber: Int, val numberOfMatches: Int)
{
    private fun generateLinks(): List<SirenLink>
    {
        val links = mutableListOf<SirenLink>()

        if(pageNumber != 1 && numberOfMatches != 0)
            links.add(SirenLink(rel = listOf("previous"), href = URI("$USERS_LIST_PATH/search?username=${searchedUsername}&page=${pageNumber - 1}")))

        links.add(SirenLink(rel = listOf("self"), href = URI("$USERS_LIST_PATH/search?username=${searchedUsername}&page=$pageNumber")))

        if(numberOfMatches == NUMBER_OF_ACCOUNTS_PER_PAGE)
            links.add(SirenLink(rel = listOf("next"), href = URI("$USERS_LIST_PATH/search?username=${searchedUsername}&page=${pageNumber + 1}")))

        return links
    }

    fun toSirenObject(
        subEntities: List<SirenSubEntity>
    ): SirenEntity<UsersListSearchOutputModel>
    {
        return SirenEntity(
            clazz = listOf("username", "search", "collection"),
            properties = this,
            entities = if(subEntities.isEmpty()) null else subEntities,
            links = generateLinks(),
            actions = null
        )
    }
}
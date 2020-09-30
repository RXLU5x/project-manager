package ipl.isel.daw.group8.projects.models

import ipl.isel.daw.group8.common.*
import ipl.isel.daw.group8.projects.daos.NUMBER_OF_PROJECTS_PER_PAGE
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

class ProjectsListOutputModel(val totalNumberOfProjects: Long, val pageNumber: Int, val numberOfProjects: Int)
{
    private fun generateLinks(): MutableList<SirenLink>
    {
        val links = mutableListOf<SirenLink>()

        if(pageNumber != 1 && numberOfProjects != 0)
            links.add(SirenLink(rel = listOf("previous"), href = URI("$USERS_PROJECTS_LIST_PATH?page=${pageNumber - 1}")))

        links.add(SirenLink(rel = listOf("self"), href = URI("$USERS_PROJECTS_LIST_PATH?page=$pageNumber")))

        if(numberOfProjects == NUMBER_OF_PROJECTS_PER_PAGE)
            links.add(SirenLink(rel = listOf("next"), href = URI("$USERS_PROJECTS_LIST_PATH?page=${pageNumber + 1}")))

        return links
    }

    fun toSirenObject(
        subEntities: List<SirenSubEntity>
    ): SirenEntity<ProjectsListOutputModel>
    {
        return SirenEntity(
            clazz = listOf("projects", "collection"),
            properties = this,
            entities = if(subEntities.isEmpty()) null else subEntities,
            links = generateLinks(),
            actions = null
        )
    }
}
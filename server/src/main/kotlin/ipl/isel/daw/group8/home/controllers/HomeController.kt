package ipl.isel.daw.group8.home.controllers

import ipl.isel.daw.group8.common.*
import ipl.isel.daw.group8.home.models.ApiInfo
import ipl.isel.daw.group8.home.models.AuthReq
import ipl.isel.daw.group8.home.models.HomeOutputModel
import ipl.isel.daw.group8.home.models.NavigationLink
import ipl.isel.daw.group8.home.services.LoginServices
import ipl.isel.daw.group8.users.models.User
import ipl.isel.daw.group8.users.models.UserOutputModel
import ipl.isel.daw.group8.users.models.toOutputModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping(value = [ROOT_PATH])
class HomeController(val service: LoginServices)
{
    @RequestMapping(value = ["/login"], produces = [SIREN_MEDIA_TYPE_VALUE])
    @GetMapping
    fun getAccountId(
        @RequestHeader("Authorization") authorization: String
    ): SirenEntity<UserOutputModel>
    {
        val (username, password) = BasicAuthentication(authorization).parseHeader()

        return User(service.getAccountId(username, password), username, password)
                .toOutputModel()
                .toSirenObject()
    }

    @GetMapping(produces = [JSON_HOME_MEDIA_TYPE_VALUE])
    fun getNavigation(): HomeOutputModel
    {
        return HomeOutputModel(
            ApiInfo("Group 8 DAW Web API",
                mapOf(
                    "authors" to listOf(URI("mailto:A45218@alunos.isel.pt"), URI("mailto:A45232@alunos.isel.pt"), URI("mailto:A45239@alunos.isel.pt")),
                    "describedBy" to listOf(URI("https://github.com/DABarreiro/DAW-1920v-LI61D-G08/wiki"))
                )
            ),

            mapOf(
                "users-list" to NavigationLink(
                    href = USERS_LIST_PATH,
                    hints = mapOf("allow" to listOf("GET, POST"))
                ),

                "users-projects-list" to NavigationLink(
                    href = USERS_PROJECTS_LIST_PATH,
                    hints = mapOf("allow" to listOf("GET"))
                ),

                "users-list-search" to NavigationLink(
                    href = "$USERS_LIST_PATH/search",
                    hints = mapOf("allow" to listOf("GET"))
                ),

                "user" to NavigationLink(
                    hrefTemplate = USER_PATH,
                    hrefVars = mapOf("user_id" to "user identifier"),
                    hints = mapOf("allow" to listOf("GET", "POST", "DELETE"), "auth-req" to listOf(AuthReq("Basic")))
                ),

                "user-projects-list" to NavigationLink(
                    hrefTemplate = USER_PROJECTS_LIST_PATH,
                    hrefVars = mapOf("user_id" to "user identifier"),
                    hints = mapOf("allow" to listOf("GET", "POST"), "auth-req" to listOf(AuthReq("Basic")))
                ),

                "user-project" to NavigationLink(
                    hrefTemplate = USER_PROJECT_PATH,
                    hrefVars = mapOf("user_id" to "user identifier", "project_id" to "project identifier"),
                    hints = mapOf("allow" to listOf("GET", "POST", "DELETE"), "auth-req" to listOf(AuthReq("Basic")))
                ),

                "user-project-issues-list" to NavigationLink(
                    hrefTemplate = USER_PROJECT_ISSUES_LIST_PATH,
                    hrefVars = mapOf("user_id" to "user identifier", "project_id" to "project identifier"),
                    hints = mapOf("allow" to listOf("GET", "POST"), "auth-req" to listOf(AuthReq("Basic")))
                ),

                "user-project-issue" to NavigationLink(
                    hrefTemplate = USER_PROJECT_ISSUE_PATH,
                    hrefVars = mapOf("user_id" to "user identifier", "project_id" to "project identifier", "issue_id" to "issue identifier"),
                    hints = mapOf("allow" to listOf("GET", "POST", "DELETE"), "auth-req" to listOf(AuthReq("Basic")))
                ),

                "user-project-issue-comments-list" to NavigationLink(
                    hrefTemplate = USER_PROJECT_ISSUE_COMMENTS_LIST_PATH,
                    hrefVars = mapOf("user_id" to "user identifier", "project_id" to "project identifier", "issue_id" to "issue identifier"),
                    hints = mapOf("allow" to listOf("GET", "POST"), "auth-req" to listOf(AuthReq("Basic")))
                ),

                "user-project-issue-comment" to NavigationLink(
                    hrefTemplate = USER_PROJECT_ISSUE_COMMENT_PATH,
                    hrefVars = mapOf("user_id" to "user identifier", "project_id" to "project identifier", "issue_id" to "issue identifier", "comment_id" to "comment identifier"),
                    hints = mapOf("allow" to listOf("GET", "POST", "DELETE"), "auth-req" to listOf(AuthReq("Basic")))
                )
            )
        )
    }
}
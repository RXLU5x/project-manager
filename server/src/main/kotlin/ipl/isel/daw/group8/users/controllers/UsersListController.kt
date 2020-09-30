package ipl.isel.daw.group8.users.controllers

import ipl.isel.daw.group8.common.*
import ipl.isel.daw.group8.users.models.UserInputModel.UserRegistryInputModel
import ipl.isel.daw.group8.users.models.UserOutputModel
import ipl.isel.daw.group8.users.models.UsersListOutputModel
import ipl.isel.daw.group8.users.models.UsersListSearchOutputModel
import ipl.isel.daw.group8.users.models.toOutputModel
import ipl.isel.daw.group8.users.services.UsersListServices
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = [USERS_LIST_PATH], produces = [SIREN_MEDIA_TYPE_VALUE])
class UsersListController(val service: UsersListServices)
{
    class EmptyUsernameParameterException: BadRequestException(
        title = "Empty Username Parameter",
        detail = "The request parameter username must be filled."
    )

    @GetMapping
    fun listAccounts(
        @RequestParam(name = "page", defaultValue = "1") page: Int
    ): SirenEntity<UsersListOutputModel>
    {
        if(page <= 0)
            throw InvalidPageParameterException()

        val (accounts, totalNumberOfAccounts) =
                service.listAccounts(page)

        val accountsAsSirenSubEntities = accounts.map {
            it.toOutputModel()
        }.map {
            it.toSirenObject(
                deleteAccountAction = false,
                updateAccountInformationAction = false,
                projectsListEmbeddedLink = false
            )
        }.map {
            it.toEmbeddedRepresentation(listOf("user"))
        }

        return UsersListOutputModel(totalNumberOfAccounts, page, accounts.size)
                .toSirenObject(subEntities = accountsAsSirenSubEntities)
    }

    @GetMapping("search")
    fun searchAccounts(
        @RequestParam("username") username: String,
        @RequestParam(name = "page", defaultValue = "1") page: Int
    ): SirenEntity<UsersListSearchOutputModel>
    {
        if(page <= 0)
            throw InvalidPageParameterException()

        if(username.isEmpty())
            throw EmptyUsernameParameterException()

        val (matches, totalNumberOfMatches) =
                service.searchAccounts(username, page)

        val matchesAsSirenSubEntities = matches.map {
            it.toOutputModel()
        }.map {
            it.toSirenObject(
                deleteAccountAction = false,
                updateAccountInformationAction = false,
                projectsListEmbeddedLink = false
            )
        }.map {
            it.toEmbeddedRepresentation(listOf("search", "match"))
        }

        return UsersListSearchOutputModel(username, totalNumberOfMatches, page, matches.size)
                .toSirenObject(subEntities = matchesAsSirenSubEntities)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [JSON_MEDIA_TYPE_VALUE])
    fun registerAccount(
        @RequestBody body: UserRegistryInputModel
    ): SirenEntity<UserOutputModel> {
        val (username, password) = body.validateRegistry()

        return service.registerAccount(username, password)
            .toOutputModel()
            .toSirenObject()
    }
}
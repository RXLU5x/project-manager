package ipl.isel.daw.group8.users.controllers

import ipl.isel.daw.group8.common.*
import ipl.isel.daw.group8.users.models.UserInputModel.UserUpdateInputModel
import ipl.isel.daw.group8.users.models.UserOutputModel
import ipl.isel.daw.group8.users.models.toOutputModel
import ipl.isel.daw.group8.users.services.UserServices
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = [USER_PATH], produces = [SIREN_MEDIA_TYPE_VALUE])
class UserController(val service: UserServices)
{
    @GetMapping
    fun getAccountInformation(
        @PathVariable("user_id") id: Long,
        @RequestHeader("Authorization") authorization: String
    ): SirenEntity<UserOutputModel> {
        val (username, password) = BasicAuthentication(authorization).parseHeader()

        return service.getAccountInformation(id, username, password)
                .toOutputModel()
                .toSirenObject()
    }

    @PostMapping(consumes = [JSON_MEDIA_TYPE_VALUE])
    fun updateAccountInformation(
        @PathVariable("user_id") id: Long,
        @RequestHeader("Authorization") authorization: String,
        @RequestBody body: UserUpdateInputModel
    ): SirenEntity<UserOutputModel> {
        val (username, password) = BasicAuthentication(authorization).parseHeader()
        val (newUsername, newPassword) = body.validateUpdate()

        return service.updateAccountInformation(id, username, password, newUsername, newPassword)
                .toOutputModel()
                .toSirenObject()
    }

    @DeleteMapping
    fun deleteAccount(
        @PathVariable("user_id") id: Long,
        @RequestHeader("Authorization") authorization: String
    ) {
        val (username, password) = BasicAuthentication(authorization).parseHeader()

        service.deleteAccount(id, username, password)
    }
}
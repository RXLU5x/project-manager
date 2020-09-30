package ipl.isel.daw.group8.users.services

import ipl.isel.daw.group8.users.daos.UserDAO
import ipl.isel.daw.group8.users.models.User
import org.springframework.stereotype.Service

@Service
class UserServices(val dao: UserDAO)
{
    fun getAccountInformation(id: Long, username: String, password: String) =
            dao.getAccountInformationWithCredentials(id, username, password)

    fun updateAccountInformation(id: Long, username: String, password: String, newUsername: String?, newPassword: String?): User =
            dao.updateAccountInformationWithCredentials(id, username, password, newUsername, newPassword)

    fun deleteAccount(id: Long, username: String, password: String) =
            dao.deleteAccountWithCredentials(id, username, password)
}
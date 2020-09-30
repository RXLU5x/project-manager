package ipl.isel.daw.group8.home.services

import ipl.isel.daw.group8.users.daos.UserDAO
import org.springframework.stereotype.Service

@Service
class LoginServices(val dao: UserDAO)
{
    fun getAccountId(username: String, password: String): Long =
            dao.getAccountIdFromCredentials(username, password)
}
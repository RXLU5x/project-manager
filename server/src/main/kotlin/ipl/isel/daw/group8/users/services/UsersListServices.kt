package ipl.isel.daw.group8.users.services

import ipl.isel.daw.group8.users.daos.UsersListDAO
import org.springframework.stereotype.Service

@Service
class UsersListServices(val dao: UsersListDAO)
{
    fun listAccounts(page: Int) =
            dao.listAndCountAccounts(page)

    fun searchAccounts(username: String, page: Int) =
            dao.searchAndCountAccounts(username, page)

    fun registerAccount(username: String, password: String) =
            dao.registerAccountWithCredentials(username, password)
}
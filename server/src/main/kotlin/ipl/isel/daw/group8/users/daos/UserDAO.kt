package ipl.isel.daw.group8.users.daos

import ipl.isel.daw.group8.common.ForbiddenException
import ipl.isel.daw.group8.users.models.User
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

class AccountActionDenied(id: Long): ForbiddenException(
    title = "Account Action Denied",
    detail = "The account with id $id does not have permission to perform this action."
)

class AccountAccessDenied: ForbiddenException(
    title = "Account Access Denied",
    detail = "The password provided is incorrect."
)

interface UserDAO
{
    @CreateSqlObject
    fun UsersListDAO(): UsersListDAO

    @SqlQuery("SELECT * FROM \"User\" WHERE \"id\" = ?")
    @Transaction
    fun getAccountInformation(id: Long): User

    @SqlQuery("SELECT \"password\" FROM \"User\" WHERE \"id\" = ?")
    @Transaction
    fun getAccountPassword(id: Long): String

    @Transaction
    fun checkPassword(id: Long, password: String) {
        val userPassword = getAccountPassword(id)

        if (password != userPassword)
            throw AccountAccessDenied()
    }

    @Transaction
    fun getAccountInformationWithCredentials(id: Long, username: String, password: String): User
    {
        // Validate account action
        validateAccountAction(username, password, id)

        return getAccountInformation(id)
    }

    @SqlQuery("UPDATE \"User\" SET \"username\" = ? WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun updateAccountUsername(newUsername: String, id: Long): User

    @SqlQuery("UPDATE \"User\" SET \"password\" = ? WHERE \"id\" = ? RETURNING *")
    @Transaction
    fun updateAccountPassword(newPassword: String, id: Long): User

    @Transaction
    fun updateAccountInformationWithCredentials(id: Long, username: String, password: String, newUsername: String?, newPassword: String?): User
    {
        var user: User = getAccountInformationWithCredentials(id, username, password)

        if ( (newUsername != null) && (newUsername != user.username) ) {
            UsersListDAO().checkUsernameUniqueness(newUsername)
            user = updateAccountUsername(newUsername, id)
        }

        if ( (newPassword != null) && (newPassword != user.password) )
            user = updateAccountPassword(newPassword, id)

        return user
    }

    @SqlUpdate("DELETE FROM \"User\" WHERE \"id\" = ?")
    @Transaction
    fun deleteAccount(id: Long)

    @Transaction
    fun deleteAccountWithCredentials(id: Long, username: String, password: String)
    {
        // Validate account action
        validateAccountAction(username, password, id)

        deleteAccount(id)
    }

    @SqlQuery("SELECT \"id\" FROM \"User\" WHERE \"username\" = ?")
    @Transaction
    fun getAccountId(username: String): Long

    @Transaction
    fun getAccountIdFromCredentials(username: String, password: String): Long
    {
        // Check if there's an account with this username
        UsersListDAO().checkIfAccountExists(username)

        // Get the id of the account
        val uId = getAccountId(username)

        // Check if the password is correct
        checkPassword(uId, password)

        return uId
    }

    @Transaction
    fun validateAccountAction(username: String, password: String, id: Long, vararg allowedIds: Long)
    {
        // Check if there's an account with this id
        UsersListDAO().checkIfAccountExists(id)

        // Get the account id from the credentials sent
        val uId = getAccountIdFromCredentials(username, password)

        // Check if user has permission to perform this action
        if(uId == id)
            return
        else if(!allowedIds.contains(uId))
            throw AccountActionDenied(uId)

        // Check if all allowed users accounts exists
        for (allowedId in allowedIds)
            UsersListDAO().checkIfAccountExists(allowedId)
    }
}

@Repository
class UserDAOImplementation(val jdbi: Jdbi)
{
    @Bean
    fun getUserDAO(): UserDAO = jdbi.onDemand()
}
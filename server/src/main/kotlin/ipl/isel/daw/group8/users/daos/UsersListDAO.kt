package ipl.isel.daw.group8.users.daos

import ipl.isel.daw.group8.common.ConflictException
import ipl.isel.daw.group8.common.NotFoundException
import ipl.isel.daw.group8.users.models.User
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

class UsernameAlreadyExistsException(username: String): ConflictException(
    title = "Username Already Exists",
    detail = "There's already an account whose username is $username."
)

class AccountNotFoundException(id: String, value: String): NotFoundException(
    title = "Account Not Found",
    detail = "There's no account whose $id is $value."
)

const val NUMBER_OF_ACCOUNTS_PER_PAGE = 25

@Repository
interface UsersListDAO
{
    @SqlQuery("SELECT \"id\", \"username\" FROM \"User\" LIMIT $NUMBER_OF_ACCOUNTS_PER_PAGE OFFSET $NUMBER_OF_ACCOUNTS_PER_PAGE * (? - 1)")
    @Transaction
    fun listAccounts(page: Int): List<User>

    @SqlQuery("SELECT COUNT(\"id\") FROM \"User\"")
    @Transaction
    fun countAccounts(): Long

    @Transaction
    fun listAndCountAccounts(page: Int): Pair<List<User>, Long>
    {
        val accounts = listAccounts(page)
        val totalNumberOfAccounts = countAccounts()

        return Pair(accounts, totalNumberOfAccounts)
    }

    @SqlQuery("SELECT \"id\", \"username\" FROM \"User\" WHERE \"username\" ILIKE ? LIMIT $NUMBER_OF_ACCOUNTS_PER_PAGE OFFSET $NUMBER_OF_ACCOUNTS_PER_PAGE * (? - 1)")
    @Transaction
    fun searchAccounts(usernamePattern: String, page: Int): List<User>

    @SqlQuery("SELECT COUNT(\"id\") FROM \"User\" WHERE \"username\" ILIKE ?")
    @Transaction
    fun countMatches(usernamePattern: String): Long

    @Transaction
    fun searchAndCountAccounts(username: String, page: Int): Pair<List<User>, Long>
    {
        val matches = searchAccounts("$username%", page)
        val totalNumberOfMatches = countMatches("$username%")

        return Pair(matches, totalNumberOfMatches)
    }

    @SqlQuery("INSERT INTO \"User\" (\"username\", \"password\") VALUES (?, ?) RETURNING \"id\", \"username\"")
    @Transaction
    fun registerAccount(username: String, password: String): User

    @Transaction
    fun registerAccountWithCredentials(username: String, password: String): User
    {
        // Check if the username is unique
        checkUsernameUniqueness(username)

        return registerAccount(username, password)
    }

    @Transaction
    fun checkUsernameUniqueness(username: String)
    {
        // Check if there's already an account with this username
        val found = findAccount(username) ?: false

        if(found)
            throw UsernameAlreadyExistsException(username)
    }

    @SqlQuery("SELECT TRUE FROM \"User\" WHERE \"username\" = ?")
    @Transaction
    fun findAccount(username: String): Boolean?

    @Transaction
    fun checkIfAccountExists(username: String) {
        // Check if there's an account with this username
        findAccount(username) ?: throw AccountNotFoundException("username", username)
    }

    @SqlQuery("SELECT TRUE FROM \"User\" WHERE \"id\" = ?")
    @Transaction
    fun findAccount(id: Long): Boolean?

    @Transaction
    fun checkIfAccountExists(id: Long) {
        // Check if there's an account with this id
        findAccount(id) ?: throw AccountNotFoundException("id", "$id")
    }
}

@Repository
class UsersListDAOImplementation(val jdbi: Jdbi)
{
    @Bean
    fun getUsersListDAO(): UsersListDAO = jdbi.onDemand()
}
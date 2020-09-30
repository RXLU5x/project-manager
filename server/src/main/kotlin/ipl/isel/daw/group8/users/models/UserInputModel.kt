package ipl.isel.daw.group8.users.models

import ipl.isel.daw.group8.common.BlankBodyParametersException
import ipl.isel.daw.group8.common.MissingBodyParametersException

open class UserInputModel
{
    class UserRegistryInputModel(val username: String, val password: String): UserInputModel()
    {
        fun validateRegistry(): Pair<String, String>
        {
            val emptyParams = mutableListOf<String>()

            if(username.isBlank())
                emptyParams.add("username")

            if(password.isBlank())
                emptyParams.add("password")

            if(emptyParams.isNotEmpty())
                throw BlankBodyParametersException(emptyParams)

            return Pair(username, password)
        }
    }

    class UserUpdateInputModel(val newUsername: String?, val newPassword: String?): UserInputModel()
    {
        fun validateUpdate(): Pair<String?, String?>
        {
            val missingParams = mutableListOf<String>()
            val blankParams = mutableListOf<String>()

            if(newUsername == null)
                missingParams.add("newUsername")
            else if(newUsername.isBlank())
                blankParams.add("newUsername")

            if(newPassword == null)
                missingParams.add("newPassword")
            else if(newPassword.isBlank())
                blankParams.add("newPassword")

            if(missingParams.size > 1)
                throw MissingBodyParametersException(missingParams)

            if(blankParams.isNotEmpty())
                throw BlankBodyParametersException(blankParams)

            return Pair(newUsername, newPassword)
        }
    }
}
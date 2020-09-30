package ipl.isel.daw.group8.common

import java.nio.charset.Charset
import java.util.*

class BasicAuthentication(private val header: String)
{
    class UnsupportedAuthenticationTypeException: UnauthorizedException(
        title = "Unsupported Authentication Type",
        detail = "The request uses an unsupported authentication type. For now, the only authentication type available is Basic."
    )

    class MissingCredentialsException(credentials: List<String>): UnauthorizedException(
        title = "Missing " + if(credentials.size == 1) "Credential" else "Credentials",
        detail = if(credentials.size == 1) credentials[0] else credentials.reduceRight { p1: String, p2: String -> "$p1, $p2" }
    )

    fun parseHeader(): Pair<String, String>
    {
        if(!header.matches(Regex("^Basic [\\w+/=]+$")))
            throw UnsupportedAuthenticationTypeException()

        val encodedCredentials = header.substringAfter(" ")
        val decodedCredentials = Base64.getDecoder().decode(encodedCredentials).toString(Charset.forName("UTF-8"))

        val username = decodedCredentials.substringBeforeLast(":")
        val password = decodedCredentials.substringAfterLast(":")

        val missingCredentials = mutableListOf<String>()

        if(username.isBlank())
            missingCredentials.add("username")

        if(password.isBlank())
            missingCredentials.add("password")

        if(missingCredentials.size != 0)
            throw MissingCredentialsException(missingCredentials)

        return Pair(username, password)
    }
}
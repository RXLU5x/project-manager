package ipl.isel.daw.group8.home.models

import java.net.URI

/**
 * Output model used to describe the API's navigation links, as specified in [json-home](https://mnot.github.io/I-D/json-home/).
 */
data class HomeOutputModel(val api: ApiInfo, val resources: Map<String, NavigationLink>)

/**
 * Used in the [HomeOutputModel] and contains the API information.
 */
data class ApiInfo(val title: String, val links: Map<String, List<URI>>?)

/**
 * Used in the [HomeOutputModel] and contains the link to a resource.
 */
data class NavigationLink(val href: String? = null, val hrefTemplate: String? = null, val hrefVars: Map<String, String>? = null, val hints: Map<String, List<Any>>? = null)

data class AuthReq(val scheme: String, val realms: List<String>? = null)
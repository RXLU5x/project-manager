package ipl.isel.daw.group8.users.models

data class User(val id: Long, val username: String?, val password: String?)

fun User.toOutputModel() = UserOutputModel(this.id, this.username, this.password)
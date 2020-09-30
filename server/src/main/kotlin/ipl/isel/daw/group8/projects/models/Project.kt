package ipl.isel.daw.group8.projects.models

data class Project(val id: Long, val userId: Long, val name: String?, val description: String?, val allowedLabels: Array<String>?, var allowedStates: Map<String, Array<String>>?)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Project

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (allowedLabels != null) {
            if (other.allowedLabels == null) return false
            if (!allowedLabels.contentEquals(other.allowedLabels)) return false
        } else if (other.allowedLabels != null) return false
        if (allowedStates != other.allowedStates) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (allowedLabels?.contentHashCode() ?: 0)
        result = 31 * result + (allowedStates?.hashCode() ?: 0)
        return result
    }
}

fun Project.toOutputModel() = ProjectOutputModel(this.id, this.userId, this.name, this.description, this.allowedLabels, this.allowedStates)
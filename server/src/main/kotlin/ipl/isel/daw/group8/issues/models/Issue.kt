package ipl.isel.daw.group8.issues.models

import java.time.LocalDate

class Issue(val id: Long, val projectId: Long, val userId: Long, val title: String?, val description: String?, val createdOn: LocalDate?, val closedOn : LocalDate?, val labels: Array<String>?, val state: String?)

fun Issue.toOutputModel() = IssueOutputModel(this.id, this.projectId, this.userId, this.title, this.description, this.createdOn, this.closedOn, this.labels, this.state)
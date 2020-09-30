package ipl.isel.daw.group8.issues.services

import ipl.isel.daw.group8.issues.daos.IssuesListDAO
import org.springframework.stereotype.Service

@Service
class IssuesListServices(val dao: IssuesListDAO)
{
    fun listIssues(userId: Long, projectId: Long, page: Int, username: String, password: String) =
            dao.listAndCountIssuesWithCredentials(userId, projectId, page, username, password)

    fun registerIssue(userId: Long, projectId: Long, title: String, description: String, labels: Array<String>, username: String, password: String) =
            dao.registerIssueWithCredentials(userId, projectId, title, description, labels, username, password)
}
package ipl.isel.daw.group8.issues.services

import ipl.isel.daw.group8.issues.daos.IssueDAO
import org.springframework.stereotype.Service

@Service
class IssueServices(val dao: IssueDAO)
{
    fun getIssueInformation(userId: Long, projectId: Long, id: Long, username: String, password: String) =
            dao.getIssueInformationWithCredentials(userId, projectId, id, username, password)

    fun updateIssueInformation(userId: Long, projectId: Long, id: Long, username: String, password: String, newTitle: String?, newDescription: String?, newState: String?, newLabels: Array<String>) =
            dao.updateIssueInformationWithCredentials(userId, projectId, id, username, password, newTitle, newDescription, newState, newLabels)

    fun deleteIssue(userId: Long, projectId: Long, id: Long, username: String, password: String) =
            dao.deleteIssueWithCredentials(userId, projectId, id, username, password)
}
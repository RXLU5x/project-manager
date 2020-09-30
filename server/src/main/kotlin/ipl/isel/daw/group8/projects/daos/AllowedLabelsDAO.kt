package ipl.isel.daw.group8.projects.daos

import org.jdbi.v3.sqlobject.SingleValue
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction

interface AllowedLabelsDAO
{
    @SqlQuery("SELECT \"allowed_labels\" FROM \"Project\" WHERE \"id\" = ?")
    @SingleValue
    @Transaction
    fun getAllowedLabels(projectId: Long): Array<String>
}
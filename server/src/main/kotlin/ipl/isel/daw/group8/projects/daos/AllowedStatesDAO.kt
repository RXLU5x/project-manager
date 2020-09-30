package ipl.isel.daw.group8.projects.daos

import org.jdbi.v3.sqlobject.SingleValue
import org.jdbi.v3.sqlobject.config.KeyColumn
import org.jdbi.v3.sqlobject.config.ValueColumn
import org.jdbi.v3.sqlobject.statement.BatchChunkSize
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.transaction.Transaction

interface AllowedStatesDAO
{
    @SqlQuery("SELECT \"name\", \"next\" FROM \"States\" WHERE \"project_id\" = ?")
    @KeyColumn("name")
    @ValueColumn("next")
    @Transaction
    fun getAllowedStates(projectId: Long): Map<String, Array<String>>

    @SqlQuery("SELECT \"next\" FROM \"States\" WHERE \"project_id\" = ? AND \"name\" = ?")
    @SingleValue
    @Transaction
    fun getAllowedStateTransitions(projectId: Long, state: String): Array<String>?

    @SqlBatch("INSERT INTO \"States\" (\"project_id\", \"user_id\", \"name\", \"next\") VALUES (?, ?, ?, ?)")
    @BatchChunkSize(100)
    @Transaction
    fun registerAllowedStates(projectId: Long, userId: Long, allowedStates: List<String>, allowedTransitions: List<Array<String>>)
}
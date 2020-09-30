package ipl.isel.daw.group8.issues.models

import ipl.isel.daw.group8.common.BlankBodyParametersException
import ipl.isel.daw.group8.common.MissingBodyParametersException
import java.io.Serializable

open class IssueInputModel
{
    class IssueRegistryInputModel(val title: String, val description: String, val labels: Array<String> = arrayOf()): IssueInputModel()
    {
        fun validateRegistry(): Triple<String, String, Array<String>>
        {
            val missingParams = mutableListOf<String>()
            val blankParams = mutableListOf<String>()

            if(title.isBlank())
                blankParams.add("name")

            if(description.isBlank())
                blankParams.add("description")

            if(missingParams.isNotEmpty())
                throw MissingBodyParametersException(missingParams)

            if(blankParams.isNotEmpty())
                throw BlankBodyParametersException(blankParams)

            return Triple(title, description, validateAllowedLabels())
        }

        private fun validateAllowedLabels(): Array<String> =
                labels.map{ it.toLowerCase() }.distinct().toTypedArray()
    }

    class IssueUpdateInputModel(val newTitle: String?, val newDescription: String?, val newState: String?, val newLabels: Array<String> = arrayOf()): IssueInputModel()
    {
        data class Quadruple<out A, out B, out C, out D>(val a: A, val b: B, val c: C, val d: D): Serializable

        fun validateUpdate(): Quadruple<String?, String?, String?, Array<String>>
        {
            val missingParams = mutableListOf<String>()
            val blankParams = mutableListOf<String>()

            if(newTitle == null)
                missingParams.add("newName")
            else if(newTitle.isBlank())
                blankParams.add("newName")

            if(newDescription == null)
                missingParams.add("newDescription")
            else if(newDescription.isBlank())
                blankParams.add("newDescription")

            if(newState == null)
                missingParams.add("newState")
            else if(newState.isBlank())
                blankParams.add("newState")

            if(missingParams.size > 2)
                throw MissingBodyParametersException(missingParams)

            if(blankParams.isNotEmpty())
                throw BlankBodyParametersException(blankParams)

            return Quadruple(newTitle, newDescription, newState, newLabels)
        }
    }
}
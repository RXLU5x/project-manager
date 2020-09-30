package ipl.isel.daw.group8.projects.models

import ipl.isel.daw.group8.common.BadRequestException
import ipl.isel.daw.group8.common.BlankBodyParametersException
import ipl.isel.daw.group8.common.MissingBodyParametersException
import java.io.Serializable

class MultipleStartStatesException: BadRequestException(
    title = "Multiple Start States",
    detail = "You're only allowed to define a single start state."
)

class MissingStatesDefinitionException: BadRequestException(
    title = "Missing States Definition",
    detail = "Some transitioning states are missing."
)

class InvalidStartStateException: BadRequestException(
    title = "Invalid Start State",
    detail = "The start state transitions must be specified as \"*\" and must not include more values associated."
)

open class ProjectInputModel
{
    class ProjectRegistryInputModel(val name: String, val description: String, val allowedLabels: Array<String> = arrayOf(), val allowedStates: MutableMap<String, Array<String>> = mutableMapOf()): ProjectInputModel()
    {
        data class Quadruple<out A, out B, out C, out D>(val a: A, val b: B, val c: C, val d: D): Serializable

        fun validateRegistry(): Quadruple<String, String, Array<String>, Map<String, Array<String>>>
        {
            val missingParams = mutableListOf<String>()
            val blankParams = mutableListOf<String>()

            if(name.isBlank())
                blankParams.add("name")

            if(description.isBlank())
                blankParams.add("description")

            if(missingParams.isNotEmpty())
                throw MissingBodyParametersException(missingParams)

            if(blankParams.isNotEmpty())
                throw BlankBodyParametersException(blankParams)

            return Quadruple(name, description, validateAllowedLabels(), validateAllowedStateTransitions())
        }

        private fun validateAllowedStateTransitions(): Map<String, Array<String>>
        {
            val temp: MutableMap<String, Array<String>> = allowedStates
                .filterKeys { key -> !key.isBlank() }
                .mapValues { entry -> entry.value.distinctBy { it.toLowerCase() }.filter { !it.isBlank() }.toTypedArray() }
                .toMutableMap()

            var startKey: String? = null

            for ((k, v) in temp)
            {
                if(v.contains("*") && v.size > 1)
                    throw InvalidStartStateException()

                if(v.contains("*") && v.size == 1) {
                    if(startKey == null)
                        startKey = k
                    else
                        throw MultipleStartStatesException()
                }
            }

            if(startKey == null)
                temp["open"] = arrayOf("*")

            temp.merge("archived", arrayOf()) {a, b -> a + b}
            temp.merge("closed", arrayOf("archived")) {a, b -> a + b}

            val states = temp.values.reduce { acc, strings -> acc + strings }.distinctBy { it.toLowerCase() }.filter { it != "*" }

            if(!states.all{ temp.containsKey(it) })
                throw MissingStatesDefinitionException()

            return temp
        }

        private fun validateAllowedLabels(): Array<String> =
                allowedLabels.filter { !it.isBlank() }.distinctBy { it.toLowerCase() }.toTypedArray()
    }

    class ProjectUpdateInputModel(val newName: String?, val newDescription: String?): ProjectInputModel()
    {
        fun validateUpdate(): Pair<String?, String?>
        {
            val missingParams = mutableListOf<String>()
            val blankParams = mutableListOf<String>()

            if(newName == null)
                missingParams.add("newName")
            else if(newName.isBlank())
                blankParams.add("newName")

            if(newDescription == null)
                missingParams.add("newDescription")
            else if(newDescription.isBlank())
                blankParams.add("newDescription")

            if(missingParams.size > 1)
                throw MissingBodyParametersException(missingParams)

            if(blankParams.isNotEmpty())
                throw BlankBodyParametersException(blankParams)

            return Pair(newName, newDescription)
        }
    }
}
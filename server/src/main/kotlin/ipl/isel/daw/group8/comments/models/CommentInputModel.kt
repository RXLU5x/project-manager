package ipl.isel.daw.group8.comments.models

import ipl.isel.daw.group8.common.BlankBodyParametersException
import ipl.isel.daw.group8.common.MissingBodyParametersException

open class CommentInputModel
{
    class CommentRegistryInputModel(val text: String): CommentInputModel()
    {
        fun validateRegistry(): String
        {
            val blankParams = mutableListOf<String>()

            if(text.isBlank())
                blankParams.add("text")

            if(blankParams.isNotEmpty())
                throw BlankBodyParametersException(blankParams)

            return text
        }
    }

    class CommentUpdateInputModel(val newText: String?): CommentInputModel()
    {
        fun validateUpdate(): String
        {
            if(newText == null)
                throw MissingBodyParametersException(listOf("newText"))
            else if(newText.isBlank())
                throw BlankBodyParametersException(listOf("newText"))

            return newText
        }
    }
}
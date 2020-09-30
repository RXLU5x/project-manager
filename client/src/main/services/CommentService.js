function getIssueCommentsFromSiren(sirenContent){
    let entities = sirenContent.entities
    
    if(entities)
        return entities.map(c => c.properties)
    
    return []
}

function getCommentFromSiren(sirenContent){
    return sirenContent.properties
}

export function getCommentService(baseURL, authToken){
    return {
        getComment: async(user_id, project_id, issue_id, comment_id) => {
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/${issue_id}/${comment_id}`, {
                    method: 'GET',
                    headers: {'Authorization': authToken}
                }
            )

            if(response.status<200 || response.status>=300)
                return null

            const content = await response.json()
            return getCommentFromSiren(content)
        },

        updateComment: async (user_id, project_id, issue_id, comment_id, text) => {
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/${issue_id}/${comment_id}`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json', 'Authorization': authToken},
                    body: JSON.stringify({newText: text})
                }
            )
            
            if(response.status<200 || response.status>=300)
                return null

            const content = await response.json()
            return getCommentFromSiren(content)
        },

        getIssueComments: async(user_id, project_id, issue_id, page) => {
            if(!page) page = 1

            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/${issue_id}/comments?page=${page}`, {
                    method: 'GET',
                    headers: {'Authorization': authToken}
                }
            )

            if(response.status<200 || response.status>=300)
                return null

            const content = await response.json()
            return getIssueCommentsFromSiren(content)
        },

        addComment: async(user_id, project_id, issue_id, text) => {
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/${issue_id}/comments`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json', 
                        'Authorization': authToken
                    },
                    body: JSON.stringify({text: text})
                }
            )

            if(response.status<200 || response.status>=300)
                return null

            const content = await response.json()
            return getCommentFromSiren(content)
        },

        deleteComment: async(user_id, project_id, issue_id, comment_id)=>{
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/${issue_id}/${comment_id}`, {
                    method: 'DELETE',
                    headers: {'Authorization': authToken}
                }
            )            
            
            if(response.status<200 || response.status>=300)
                return null
            
            return null
        }
    }
}
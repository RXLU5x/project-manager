function getUserIssuesFromSiren(sirenContent) {
    let entities = sirenContent.entities
    
    if(entities)
        return entities.map(c => c.properties)
    
    return []
}

function getIssueFromSiren(sirenContent) {
    return sirenContent.properties
}

export function getIssueService(baseURL, authToken) {
    return {
        getIssue: async(user_id, project_id, issue_id) => {
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/${issue_id}`, {
                    method: 'GET',
                    headers: {'Authorization': authToken}
                }
            )
     
            if(response.status<200 || response.status>=300)
                return null

            const content = await response.json()
            return getIssueFromSiren(content)
        },

        updateIssue: async (user_id, project_id, issue_id, titleN, descriptionN, stateN, set_labelsN) => {
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/${issue_id}`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json', 'Authorization': authToken},
                    body: JSON.stringify({newTitle: titleN, newDescription: descriptionN, newState: stateN, newLabels: set_labelsN})
                }
            )
            
            if(response.status<200 || response.status>=300)
                return null
            
            const content = await response.json()
            return getIssueFromSiren(content)
        },

        getProjectIssues: async(user_id, project_id, page, size) => {
            if( !size ) size=10
            if( !page ) page=1
        
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/issues?page=${page}&size=${size}`, {
                method: 'GET',
                headers: { 
                'Authorization': authToken}
            })
            
            if(response.status<200 || response.status>=300)
                return null
            
            const content = await response.json()
            return getUserIssuesFromSiren(content)
        },      

        addIssue: async(user_id, project_id, titleN, descriptionN, set_labelsN, ) => {
            let bdy = JSON.stringify({ title: titleN, description: descriptionN, labels: set_labelsN })

            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/issues`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json', 'Authorization': authToken},
                    body: bdy
                }
            )

            if(response.status<200 || response.status>=300)
                return null

            const content = await response.json()
            return getIssueFromSiren(content)
        },

        deleteIssue: async(user_id, project_id, issue_id)=>{
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}/${issue_id}`, {
                    method: 'DELETE',
                    headers: {'Authorization': authToken}
                }
            )
            if(response.status<200 || response.status>=300)
                return null
            return true
        }
    }
}
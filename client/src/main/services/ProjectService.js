function getUserProjectsFromSiren(sirenContent){
    let entities = sirenContent.entities
    
    if(entities)
        return entities.map(c => c.properties)
    
    return []
}

function getProjectFromSiren(sirenContent){
    return sirenContent.properties
}

export function getProjectService(baseURL, authToken) {
    return {
        getProjectList: async (user_id, page) => {
            if( !page ) page=1
            
            const response = await fetch(`http://localhost:3000/api/${user_id}/projects?page=${page}`, {
                    method: 'GET',
                    headers: {'Authorization': authToken}
                }
            )

            if(response.status<200 || response.status>=300)
                return null
            
            const content = await response.json()
            
            let users = getUserProjectsFromSiren(content)
            return users
        },

        getProject: async(user_id, project_id) => {
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}`, {
                    method: 'GET',
                    headers: {'Authorization': authToken}
                }
            )

            if(response.status<200 || response.status>=300)
                return null
                
            const content = await response.json()
            return getProjectFromSiren(content)
        },
        
        addProject: async(user_id, name, description, allowedLabels, allowedStates) => {
            const response = await fetch(`http://localhost:3000/api/${user_id}/projects`, {
                method: 'POST',
                headers: { 
                    'Authorization': authToken,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    "name": name,
                    "description": description,
                    "allowedLabels": allowedLabels,
                    "allowedStates": allowedStates
                })
            })
            
            const content = await response.json()

            if(response.status < 200 || response.status > 300)
                return {
                    error: true,
                    errorBody: content                      
                }
            
            return getProjectFromSiren(content)
        },

        editProject: async(user_id, project_id, name, description) => {
            console.log("UsersService.editProject")
            const response = await fetch(`http://localhost:3000/api/${user_id}/${project_id}`, {
                method: 'POST',
                headers: { 
                    'Authorization': authToken,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    "newName": name,
                    "newDescription": description
                })
            })

            const content = await response.json()
                        
            if(response.status < 200 || response.status >= 300)
                return {
                    error: true,
                    errorBody: content                       
                }

            return getProjectFromSiren(content)
        },

        getUserProjects: async(user_id, page) => {
            console.log("UsersService.getUserProjects")
            const response = await fetch(`http://localhost:3000/api/${user_id}/projects?page=${page}`, {
                    method: 'GET',
                    headers: {'Authorization': authToken}
                }
            )
            
            if(response.status<200 || response.status>=300)
                return null
            
            const content = await response.json()
            return getUserProjectsFromSiren(content)
        }
    }
}

    export function getMockedProjectService(){
        const projectInfo = {
            "id": "1",
            "userId": "1",
            "name": "nomeP",
            "description": "descP",
            "allowedLabels":[
                "1","2","4"
            ],
            "allowedStates":[
                "a","b","d"
            ]
        }

        return {getProject: async (id, user_id) => {
            return new Promise((resolve, reject)=>{
                setTimeout(()=>resolve(projectInfo), 1500)
            })
        } 
    }
}
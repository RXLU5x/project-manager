function getUsersFromSiren(sirenContent){
    let entities = sirenContent.entities
    
    if(entities)
        return entities.map(c => c.properties)
    
    return []
}

function getUserFromSiren(sirenContent){
    return sirenContent.properties
}

function getUserIDFromSiren(sirenContent){
    return sirenContent.properties.id
}

export function getUserService(baseURL, authToken) {
    return {
        getUserList: async () => {
            const response = await fetch('http://localhost:3000/api/users')
            
            if(response.status<200 || response.status>=300)
                return null
            
            const content = await response.json()
            let users = getUsersFromSiren(content)
            
            return users
        },
        
        getUserID: async () => {
            const response = await fetch(`http://localhost:3000/api/login`, {
                method: 'GET',
                headers: { 'Authorization': authToken}
            })

            if(response.status<200 || response.status>=300)
                return null
            
            const content = await response.json()
            
            return getUserIDFromSiren(content)
        },
        
        getUser: async (user_id) => {
            const response = await fetch(`http://localhost:3000/api/${user_id}`, {
                method: 'GET',
                headers: { 'Authorization': authToken }
            })
            
            if(response.status < 200 || response.status >= 300)
                return null
            
            const content = await response.json()
            
            return getUserFromSiren(content)
        },
        
        addUser: async (name,pass) => {
            const response = await fetch('http://localhost:3000/api/users', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json', 'Authorization': authToken },
              body: JSON.stringify({ username: name, password: pass })
            })

            if(response.status<200 || response.status>=300)
                return null

            const content = await response.json()
            
            return getUserFromSiren(content)
        },
        
        updateUser: async (id, newName, newPass) => {            
            let bdy = {};
            
            if(newName)
                bdy.newUsername = newName

            if(newPass)
                bdy.newPassword = newPass

            const response = await fetch(`http://localhost:3000/api/${id}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'Authorization': authToken },
                body: JSON.stringify(bdy)
            })

            if(response.status<200 || response.status>=300)
                return null
            
            const content = await response.json()
            
            return getUserFromSiren(content)
        },
        
        deleteUser: async (user_id) => {            
            const response = await fetch(`http://localhost:3000/api/${user_id}`, {
                method: 'DELETE',
                headers: {'Authorization': authToken}}
            )

            if(response.status<200 || response.status>=300)
                return null

            return null
        }
    }
}

export function getMockedUserService(){
    const userInfo = {
        id: "1",
        username: "name1"
    }

    return {
        getUser: async (id) => {
            return new Promise(
                (resolve, reject)=> {
                    setTimeout(()=>resolve(userInfo), 2000)
                }
            )
        }
    }
}
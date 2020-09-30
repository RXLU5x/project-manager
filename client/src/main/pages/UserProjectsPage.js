import React from 'react'
import UserProjectsLayout from '../layouts/UserProjectsLayout'
import MenuHome from '../layouts/menus/MenuHome'

import {
    Divider,
    List
  } from 'semantic-ui-react'
import ReactDOM from 'react-dom'

let page = 1
let more = true

class UserProjectsPage extends React.Component {
    generateItem = (id, name, uid, idE) => {
        let hrf = "/projects/"+uid+"/"+id
        return (
                <List.Item a href={hrf} id={idE}>
                    <p>ID: {id}</p> <p>Name: {name}</p> <p>Owner ID: {uid}</p>
                    <Divider/>
                </List.Item>            
        )
    }
    onCreate = async () => {
        let j=1   //Size=25
        let pjs = []
        if(more)    
            pjs =await this.props.projectService.getUserProjects(this.props.uid, page)
        if( !pjs ){
            window.location.href = "/home"
            return
        }
        pjs.forEach( (p)=>{
            ReactDOM.render(this.generateItem(p.id, p.name, p.userId, "p"+j)
            ,document.getElementById("p"+j++))
        }) 
        if(pjs<25)
            page--
        while(j<=25){
            more=false
            ReactDOM.render("", document.getElementById("p"+j++))
        }
    }
    onPreviousPage = () => {
        if(page>1){
            more=true
            page--
            document.getElementById("PrevPagList").innerText = `Page ${page===1 ? 1: page-1}`
            document.getElementById("PageN").innerText = `Page ${page}`
            document.getElementById("NextPagList").innerText = `Page ${page+1}`
            this.onCreate()
        }
    }
    onNextPage = () => {
        if(!more) return 
        page++
        document.getElementById("PrevPagList").innerText = `Page ${page-1}`
        document.getElementById("PageN").innerText = `Page ${page}`
        document.getElementById("NextPagList").innerText = `Page ${page+1}`
        this.onCreate()
    }
    
    //uid ={this.props.uid}
    render(){
        return(
            <div>
                <MenuHome uid={this.props.uid} pid={this.props.pid} userService={this.props.userService} />
                <UserProjectsLayout headerTitle={"Projects of User " + this.props.uid} uid ={this.props.uid}
                    onCreate={this.onCreate} projectService={this.props.projectService}
                    onPreviousPage={this.onPreviousPage} onNextPage={this.onNextPage}/>
            </div>
        )
    }
}

export default UserProjectsPage
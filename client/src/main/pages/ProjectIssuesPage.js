import React from 'react'
import UserProjectsLayout from '../layouts/UserProjectsLayout'
import MenuHome from '../layouts/menus/MenuHome'

import {Divider} from 'semantic-ui-react'
import ReactDOM from 'react-dom'

let page = 1
let more = true

class ProjectIssuesPage extends React.Component {
    generateItem = (pid, title, uid, iid, idE) => {
        let hrf = "/issues/"+uid+"/"+pid+"/"+iid
        return (
            <a href={hrf}>
                <div pid={idE} >
                    <p>ID: {iid}</p> <p>Title: {title}</p><p>Project ID: {pid}</p> <p>Owner ID: {uid}</p>
                    <Divider/>
                </div>            
            </a>
        )
    }
    onCreate = async () => {
        let j=1   //Size=25
        let pjs = []
        if(more)
            pjs = await this.props.issueService.getProjectIssues(this.props.uid, this.props.pid, page, 25)
        if( !pjs ){
            window.location.href = "/home"
            return
        }
        pjs.forEach( (i)=>{
            ReactDOM.render(this.generateItem(i.projectId, i.title, i.userId, i.id ,"p"+j), document.getElementById("p"+j++))
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
    
    render(){
        return(
            <div>
                <MenuHome uid={this.props.uid} userService={this.props.userService} />
                <UserProjectsLayout headerTitle={"Issues of Project " + this.props.uid} uid ={this.props.uid} page={page}
                    onCreate={this.onCreate} projectService={this.props.projectService}
                    onPreviousPage={this.onPreviousPage} onNextPage={this.onNextPage}/>
            </div>
        )
    }
}

export default ProjectIssuesPage
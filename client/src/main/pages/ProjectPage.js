import ProjectLayout from '../layouts/ProjectLayout'
import MenuProject from '../layouts/menus/MenuProject'
import {
  Header,
  Dropdown,
  Divider
} from 'semantic-ui-react'
import ReactDOM from 'react-dom'

import React from 'react'

var page = 1
var more = true

export default class ProjectPage extends React.Component {
  createPage = async() => {
    this.createProject()
    this.createIssues()
  }
  
  createProject = async()=>{
    let project = await this.props.projectService.getProject(this.props.uid, this.props.pid)
    if( !project )  return
    document.getElementById("projectName").innerText = project.name
    document.getElementById("projectId").innerText = "Project ID:\n     " + project.id
    ReactDOM.render(this.generateLink( `/projects/${project.userId}`, "Owner ID:\n" + project.userId), document.getElementById("projectUId"))
    document.getElementById("projectLabels").innerText = "Allowed Labels:\n" + project.allowedLabels
    let states = ""
    
    console.log(project.allowedStates)

    if(project.allowedStates)
      for(let key in project.allowedStates) {
        console.log(key)
        console.log(project[key])
        states += key + " To " + project.allowedStates[key].reduce((accum, val) => accum + ", " + val, " ") + "\n"
      }
    
      document.getElementById("projectStates").innerText = "Allowed States:\n" + states
  }
  
  createIssues = async () => {
    let j=1
    let issues = []
    if(more)
      issues = await this.props.issueService.getProjectIssues(this.props.uid, this.props.pid,page, 10)
    issues.forEach( (i) => {
      ReactDOM.render(this.generateItem(this.props.pid, i.title, this.props.uid, i.id, "dI"+j)
        ,document.getElementById("dI"+j++))
    })

    while(j<=10){
      console.log(j)
      more=false
      ReactDOM.render("", document.getElementById("dI"+j++))
    }  
  }  
  
  onPreviousPage = () => {
    if(page>1){
      more=true
      page--
      document.getElementById("PrevPagListP").innerText = `Page ${page===1 ? 1: page-1}`
      document.getElementById("NextPagListP").innerText = `Page ${page+1}`
      this.createIssues()
    }
  }
  
  onNextPage = () => {
    console.log(more)
    if(!more) return 
    page++
    document.getElementById("PrevPagListP").innerText = `Page ${page-1}`
    document.getElementById("NextPagListP").innerText = `Page ${page+1}`
    this.createIssues()
  }

  generateLink = (hrf, txt) => {
    return(
      <div>
        <a href={hrf}>
            {txt}
        </a>
      </div>
    )
  }

  generateItem = (pid, title, uid, iid, idE) => {
    let hrf = "/issues/"+uid+"/"+pid+"/"+iid
    return (
      <div id={idE}>
            <Dropdown.Item a href={hrf} text={`  ID: ${iid} || Title: ${title}`}/>
            <Divider/>
      </div>
    )
  }

  render(){
    return (
      <div>
        <MenuProject uid={this.props.uid} pid={this.props.pid} />
        <Header textAlign='center' as="h1">Project Info</Header>
        <ProjectLayout projectService={this.props.projectService} createPage={this.createPage} createIssues={this.createIssues}
          onNextPage={this.onNextPage} onPreviousPage={this.onPreviousPage}/>
      </div> 
    )
  }
}
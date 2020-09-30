import React from 'react'
import ReactDOM from 'react-dom'

import MenuIssue from '../layouts/menus/MenuIssue'
import IssueEditLayout from '../layouts/IssueEditLayout'

import {
    Header,
    Dropdown,
    Divider
  } from 'semantic-ui-react'

let set_labels = []

export default class ProfilePage extends React.Component {
    generateItem = (label, idE) => {
        return (
          <div id={idE} onClick={()=>{
            let index = set_labels.find( l => l===label)
            if(!index){
                set_labels.push(label)
                document.getElementById("SelLabelsE").textContent=set_labels
            }
            else{
                console.log(label)
                set_labels = set_labels.filter((l) => l!==label)
                document.getElementById("SelLabelsE").textContent=set_labels
            }
          }}>
                <Divider/>
                <Dropdown.Item  text={`${label}`}/>
          </div>
        )
      }

    createPage = async() => {
        let proj = await this.props.projectService.getProject(this.props.uid, this.props.pid)
        if(!proj) return
        let i=1
        let labels = proj.allowedLabels
            labels.forEach( (l)=>{
                ReactDOM.render(this.generateItem( l, i)
                    , document.getElementById(i++))        
            } )
    }

    handleEdit = async () => {
        let newTit = document.getElementById("newIsTit").value
        if( !newTit || newTit==="" ) newTit=null
        let newDesc = document.getElementById("newDesc").value
        if( !newDesc || newDesc==="" ) newDesc=null
        let newState = document.getElementById("newState").value
        if( !newState || newState==="" ) newState=null
        let res = await this.props.issueService.updateIssue(this.props.uid, this.props.pid, this.props.iid, 
            newTit, newDesc, newState, set_labels )
        if(res) window.location.href= "/issues/"+this.props.uid+"/"+this.props.pid+"/"+this.props.iid
    }

    render(){
        return(
            <div>
                <MenuIssue uid={this.props.uid}  pid={this.props.pid} iid={this.props.iid}/>
                <Header as="h1" textAlign='center'>Edit Issue {this.props.iid}</Header>
                <IssueEditLayout handleEdit={this.handleEdit} createPage={this.createPage}
                    uid={this.props.uid} pid={this.props.pid} iid={this.props.iid}/>
            </div>)
    }
}
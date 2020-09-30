import React from 'react'
import ReactDOM from 'react-dom'
import {
    Header,
    Dropdown,
    Divider
  } from 'semantic-ui-react'
import IssueCreateLayout from '../layouts/IssueCreateLayout'

import MenuHome from '../layouts/menus/MenuHome'

  let set_labels = []

export default class IssueCreatePage extends React.Component {

    generateItem = (label, idE) => {
        return (
          <div id={idE} onClick={()=>{
            let index = set_labels.find( l => l===label)
            if(!index){
                set_labels.push(label)
                document.getElementById("SelLabels").textContent=set_labels
            }
            else{
                console.log(label)
                set_labels = set_labels.filter((l) => l!==label)
                document.getElementById("SelLabels").textContent=set_labels
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
              ,document.getElementById(i++))        
          })
    }

    handleAdd = async () => {
        let title = document.getElementById("IsCreT").value
        let desc = document.getElementById("IsCreD").value
        let res = await this.props.issueService.addIssue(this.props.uid, this.props.pid, title, desc, set_labels )
        if(res) window.location.href= "/issues/"+this.props.uid+"/"+this.props.pid+"/"+res.id
    }

    render(){
        return(
        <div>
            <MenuHome uid={this.props.uid}/>
            <Header as="h1" textAlign='center'>Create Issue for Project {this.props.pid}</Header>
            <IssueCreateLayout handleAdd={this.handleAdd} createPage={this.createPage}/>
        </div>
        )
    }
}

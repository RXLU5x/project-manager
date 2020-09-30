import IssueLayout from '../layouts/IssueLayout'
import MenuIssue from '../layouts/menus/MenuIssue'
import {
  Header,
  Comment,
  Divider,
  Button
} from 'semantic-ui-react'
import ReactDOM from 'react-dom'
import React from 'react'

var page = 1
var more = true

export default class IssuePage extends React.Component {
  createPage = async() => {
    this.createIssue()
    this.createComments()
  }
  createIssue = async()=>{
    let issue = await this.props.issueService.getIssue(this.props.uid, this.props.pid, this.props.iid)
    if( !issue ){
      window.location.href = `/issues/${this.props.uid}/${this.props.pid}`
      return
    } 
    document.getElementById("issueTitle").innerText = issue.title
    document.getElementById("issueId").innerText = "Issue ID:\n" + issue.id
    ReactDOM.render(this.generateLink(`/projects/${issue.userId}`, "Owner ID:\n" + issue.userId), document.getElementById("issueUId"))
    ReactDOM.render(this.generateLink(`/projects/${issue.userId}/${issue.projectId}`, "Project ID:\n" + issue.projectId), document.getElementById("issuePId"))
    document.getElementById("issueState").innerText = "State:\n" + issue.state
    document.getElementById("issueDesc").innerText = "Description:\n" + issue.description
    document.getElementById("issueCrea").innerText = "Created On:\n" + issue.createdOn
    if(issue.closedOn)
      document.getElementById("issueClosed").innerText = "Closed On:\n" + issue.closedOn
    document.getElementById("issueLabels").innerText = "Labels:\n" + issue.set_labels
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
  onAddComm = async () => {
    let txt = document.getElementById("addCommTxt").value
    await this.props.commentService.addComment(this.props.uid, this.props.pid, this.props.iid, txt)
      .then(()=>{
        more=true
        this.createComments()
      })
  }
  createComments = async () => {
    let j=1
    let comments = []
    if ( more )
      comments = await this.props.commentService
        .getIssueComments(this.props.uid, this.props.pid, this.props.iid, page)
    comments.forEach( (c)=>{
      ReactDOM.render(this.generateComment(c, "C"+j)
        ,document.getElementById("C"+j++))
    })
    while(j<=6){
      more=false
      ReactDOM.render("", document.getElementById("C"+j++))
    }  
  }
  onPreviousPage = () => {
    if(page>1){
        more=true
        page--
        document.getElementById("PrevPagListC").innerText = `Page ${page===1 ? 1: page-1}`
        document.getElementById("PageNC").innerText = `Page ${page}`
        document.getElementById("NextPagListC").innerText = `Page ${page+1}`
        this.createComments()
    }
  }
  onNextPage = () => {
      if(!more) return 
      page++
      document.getElementById("PrevPagListC").innerText = `Page ${page-1}`
      document.getElementById("PageNC").innerText = `Page ${page}`
      document.getElementById("NextPagListC").innerText = `Page ${page+1}`
      this.createComments()
  }
  async onDelComm(comment_id){
    await this.props.commentService.deleteComment(this.props.uid, this.props.pid, this.props.iid, comment_id)
      .then(()=>{
        more=true
        this.createComments()
      })
  }
  onDelIssue = async () => {
    let res = await this.props.issueService.deleteIssue(this.props.uid, this.props.pid, this.props.iid)
    if (res) window.location.href = `/issues/${this.props.uid}/${this.props.pid}`
  }
  generateComment = (c, idC) => {
    return(
      <div id={idC}>
        <Divider />
        <Comment>
            <Comment.Author as='a'>
              Author: {c.userId}
            </Comment.Author>
            <Comment.Metadata>
              <div> - {c.createdOn} </div>
            </Comment.Metadata>
            <Comment.Text>
              {c.text}
            </Comment.Text>
            <Comment.Action>
              <Button onClick={() => this.onDelComm(c.id)}>
                Delete
              </Button>
            </Comment.Action>
        </Comment>
      </div>
    )
  }

  //<Button floated='right' onClick={this.onDelIssue}>DELETE ISSUE</Button>
  render(){
    return (
      <div>
        <MenuIssue onDelIssue={this.onDelIssue} uid={this.props.uid} pid={this.props.pid} iid={this.props.iid} />
        <Header textAlign='center' as="h1">Issue Info</Header>
        <IssueLayout projectService={this.props.projectService} 
          createPage={this.createPage} onAddComm={this.onAddComm}
          onPreviousPage={this.onPreviousPage} onNextPage={this.onNextPage}/>
      </div> 
    )
  }
}
import React, { useContext } from 'react'
import './App.css'

import HomePage from './pages/HomePage'
import ProjectPage from './pages/ProjectPage'
import IssuePage from './pages/IssuePage'
import ProfilePage from './pages/ProfilePage'
import LogoutPage from './pages/LogoutPage'
import UserProjectsPage from './pages/UserProjectsPage'
import PickProjectIssuesPage from './pages/PickProjectIssuesPage'
import PickProjIssCreaPage from './pages/PickProjIssCreaPage.js'
import ProjectIssuesPage from './pages/ProjectIssuesPage'
import IssueEditPage from './pages/IssueEditPage.js'
import CreateUserProject from './pages/CreateUserProjectPage.js'
import EditUserProject from './pages/EditUserProjectPage.js'

import {
  Switch,
  Route,
  useParams,
  Redirect
} from "react-router-dom"
import LoginContext from './login/LoginContext'

import{
  getUserService as UserService
} from './services/UserService'
import{
  getProjectService as ProjectService
} from './services/ProjectService'
import{
  getIssueService as IssueService
} from './services/IssueService'
import{
  getCommentService as CommentService
} from './services/CommentService'
import IssueCreatePage from './pages/IssueCreatePage'

var userService = undefined
var projectService = undefined
var issueService = undefined
var commentService = undefined

var id;

export default function App() {
  const context = useContext(LoginContext)
  
  let authTokenN = context.loginService.getToken(); 
  
  userService = UserService(" ", authTokenN)
  projectService = ProjectService(" ", authTokenN)
  issueService = IssueService(" ", authTokenN)
  commentService = CommentService(" ", authTokenN)
  
  id = context.loginService.getID()

  return(
    <div>
      <Switch>
        <Route path='/home' component={Home} />
        <Route path="/projects/:uid/:pid/edit" component={ProjectEdit} />
        <Route path="/projects/:uid/create" component={ProjectCreate} />
        <Route path="/projects/:uid/:pid" component={Project} />
        <Route path='/projects/:uid' component={Projects} />
        <Route path='/logout' component={Logout} />
        <Route path="/profile/:uid" component={Profile} />
        <Route path='/issues/:uid/:pid/create' component={IssueCreate} />
        <Route path='/issues/:uid/:pid/:iid/edit' component={IssueEdit} />
        <Route path='/issues/:uid/:pid/:iid' component={Issue} />
        <Route path='/issues/:uid/create' component={PickIssuesCreate} />
        <Route path='/issues/:uid/:pid' component={Issues} />
        <Route path='/issues/:uid' component={PickIssues} />
        <Route path='*'>
          <Redirect to="/home"/>
        </Route>
      </Switch>
    </div>
  )
}

function Home() {
  return <HomePage uid={id} userService={userService} />
}

function Project() {
  let {uid, pid} = useParams()
  return <ProjectPage uid ={uid} pid={pid} userService={userService} projectService={projectService} 
  issueService={issueService}/>
}

function ProjectCreate() { //pagina para criar project
  let {uid} = useParams()
  return <CreateUserProject uid ={uid} userService={userService} projectService={projectService} />
}

function ProjectEdit() { //pagina para editar project
  let {uid,pid} = useParams()
  return <EditUserProject uid ={uid} pid={pid} userService={userService} projectService={projectService} />
}

function Projects() {
  let {uid} = useParams()
  return <UserProjectsPage uid ={uid} userService={userService} projectService={projectService}/>
}

function Logout() {
  return <LogoutPage/>
}

function Profile() {
  let {uid} = useParams()
  return <ProfilePage uid={uid} userService={userService} projectService={projectService}/>
}

function PickIssues() {
  let {uid} = useParams()
  return <PickProjectIssuesPage uid ={uid} userService={userService} projectService={projectService}/>
}

function PickIssuesCreate() {
  let {uid} = useParams()
  return <PickProjIssCreaPage uid ={uid} userService={userService} projectService={projectService}/>
}

function Issues() {
  let {uid, pid} = useParams()
  return <ProjectIssuesPage uid ={uid} pid={pid}
  userService={userService} projectService={projectService} issueService={issueService}/>
}

function Issue() {
  let {uid, pid, iid} = useParams()
  return <IssuePage uid ={uid} pid={pid} iid={iid} 
    userService={userService} projectService={projectService} 
    issueService={issueService} commentService={commentService}/>
}

function IssueCreate() { //pagina para criar issue
  let {uid, pid} = useParams()
  return <IssueCreatePage uid ={uid} pid={pid}
  userService={userService} projectService={projectService} issueService={issueService}/>
}

function IssueEdit() { //pagina para editar issue
  let {uid, pid, iid} = useParams()
  return <IssueEditPage uid ={uid} pid={pid} iid={iid}
  userService={userService} projectService={projectService} issueService={issueService}/>
}
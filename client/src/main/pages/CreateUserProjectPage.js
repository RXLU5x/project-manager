import CreateProjectLayout from '../layouts/CreateProjectLayout'
import MenuHome from '../layouts/menus/MenuHome'
import {Header} from 'semantic-ui-react'

import React from 'react'

export default class ProjectPage extends React.Component {

  render(){
    return (
      <div>
        <MenuHome uid={this.props.uid} />
        <Header textAlign='center' as="h1">Create a New Project</Header>
        <CreateProjectLayout uid={this.props.uid} projectService={this.props.projectService} />
      </div> 
    )
  }
}
import EditProjectLayout from '../layouts/EditProjectLayout'
import MenuProject from '../layouts/menus/MenuProject'
import {
  Header,
  Loader
} from 'semantic-ui-react'

import React from 'react'
import Message from '../layouts/MessageResponseLayout'

export default class EditUserProject extends React.Component {
  constructor(props) {
    super(props);
 
    this.state = {
      projectState: {},
      isLoading: true
    };
  }
 
  async componentDidMount() {
    await this.setState({ isLoading: true });
 
    await this.props.projectService.getProject(this.props.uid, this.props.pid)
      .then(data => this.setState({ projectState: data, isLoading: false }))
      .catch(error => {
        this.setState({ error: {error: true, errorBody: error}, isLoading: false })
        
      });
  }

  
 render(){
    if( this.state.error && this.state.error.error === true){
      return (
        <div>
        <MenuProject uid={this.props.uid} pid= {this.props.pid} />
        <Message errorResponse={this.state.error} />
      </div> 
      )
    }
    else if(this.state.isLoading){
      return (
        <div>
            <MenuProject uid={this.props.uid} pid= {this.props.pid} />
            <Loader 
              active 
              size="massive"
              content="Fetching Project"
            />
        </div> 
      )
    }
    else
      return (
        <div>
          <MenuProject uid={this.props.uid} pid= {this.props.pid} />
          <Header textAlign='center' as="h1">Edit Project</Header>
          <EditProjectLayout projectState={this.state.projectState} projectService={this.props.projectService} 
            uid={this.props.uid} pid={this.props.pid} />
        </div> 
      )
  }
}
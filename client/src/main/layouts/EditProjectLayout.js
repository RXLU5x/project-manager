import React from 'react'
import {
  Container,
  Divider,
  Grid,
  Header,
  Button,
  Form,
} from 'semantic-ui-react'

import {TextArea} from 'semantic-ui-react'
import Message from './MessageResponseLayout.js'

export default class EditProjectLayout extends React.Component{
    constructor(props) {
        super(props);

        let projectState = this.props.projectState;
        this.state = {
            title:  projectState.name,
            description: projectState.description,
            errorResponse: {}, normalResponse: {}
        }

        this.handleInputChange = this.handleInputChange.bind(this);
        this.edit = this.edit.bind(this);
    }
    
    handleAdditionLabels = (e, { value }) => {
        this.setState((prevState) => ({
            labelsOptions: [{ text: value, value }, ...prevState.labelsOptions],
        }))
    }

    handleInputChange(name){
        return (event) =>{
            const target = event.target;
            const value = target.value;
            const field = name;
            this.setState({
                [field]: value    
            });
        }
    }

    async edit(){
        let response = await this.props.projectService.editProject(this.props.uid, this.props.pid,
            this.state.title,
            this.state.description,
            this.state.allowedLabels,
            this.state.stateMap
        );

        if(response.error){
            this.setState({
                errorResponse: response,
                normalResponse: {}
            })
        }
        else{
            /*this.setState({
                normalResponse: {
                    success: true,
                    body: `Project created with id: ${response['properties']['id']}.`,
                    link: "htttps://localhost:3000/projects" + this.props.uid + "/" + response.properties.id
                },
                errorResponse: {}
            })*/
            window.location.href= "/projects/"+this.props.uid+"/"+this.props.pid

        }
    }

    render(){
        //this.makeStateList();
        let errorResponse=this.state.errorResponse, normalResponse=this.state.normalResponse;

        return (<div>
            <Container text style={{ marginTop: '7em' }}>
                <Header as='h1'id="create"/>
                <p id="userId"></p>
                <Divider/>
                <Grid relaxed>
                    <Grid.Row relaxed>
                        <Grid.Column width="9" floated='left'>
                            <Form id="createForm">
                                <Form.Field>
                                    <label>Title</label>
                                    <input placeholder='Title' state={this.state.name} onChange={this.handleInputChange("title")}/>
                                </Form.Field>
                                <Form.Field>
                                    <label>Description</label>
                                    <TextArea placeholder='Description' state={this.state.description} onChange={this.handleInputChange("description")}/>
                                </Form.Field>                
                            </Form>
                        </Grid.Column>
                    </Grid.Row>
                    <Divider/>
                    <Grid.Row>
                        <Grid.Column floated="left">
                            <Button content="Edit" onClick={this.edit} />  
                        </Grid.Column>
                        <Message floated="left" errorResponse={errorResponse} normalResponse={normalResponse}/>
                    </Grid.Row>
                </Grid>
            </Container>
        </div>)
    }
}
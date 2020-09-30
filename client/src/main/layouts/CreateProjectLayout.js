import React from 'react'
import {
  Container,
  Divider,
  Grid,
  Header,
  Button,
  Form,
} from 'semantic-ui-react'

import { Dropdown, TextArea } from 'semantic-ui-react'
import Message from './MessageResponseLayout.js'

// Format
// { key: 'English', text: 'English', value: 'English' },
const labelsOptions = [
    { key: 'Bug', text: 'Bug', value: 'Bug' },
    { key: 'Feature', text: 'Feature', value: 'Feature' },
    { key: 'Usage-Help', text: 'Usage-Help', value: 'Usage-Help' }
], allowedLabels = [];

const stateMap = {}

export default class CreateProjectLayout extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            labelsOptions, allowedLabels, title:"", 
            stateBuffer:"", nextBuffer:"", stateMap, description:"", stateList:"",
            errorResponse: {}, normalResponse: {}
        }

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleAdditionLabels = this.handleAdditionLabels.bind(this);
        this.handleChangeDropdow = this.handleChangeDropdow.bind(this);
        this.stateAdd = this.stateAdd.bind(this);
        this.stateDelete = this.stateDelete.bind(this);
        this.makeStateList = this.makeStateList.bind(this);
        this.clearMessageBacklog = this.clearMessageBacklog.bind(this);
        this.create = this.create.bind(this);
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

    handleChangeDropdow(e, { value }) {
        this.setState({ allowedLabels: value })
    } 

    stateAdd(){
        let parsed = this.state.nextBuffer.replace(/\s/g,'').split(',');
        let newMap = this.state.stateMap;

        parsed = parsed.map(val => {
            if(val.length === 0)
                return []
            else
                return val 
        })

        newMap[this.state.stateBuffer] = parsed;

        this.setState({
            stateMap: newMap
        })
    }

    stateDelete(){
        let newMap = this.state.stateMap;
        delete newMap[this.state.stateBuffer];

        this.setState({
            stateMap: newMap
        })
    }

    makeStateList(){
        let list = [], i=0;
        for (let [key, value] of Object.entries(this.state.stateMap)) { 
            list[i++] = key + "   --> " + value;
        }
        this.list = list;
    }

    clearMessageBacklog(){
        this.setState({
            errorResponse: {},
            normalResponse: {}
        })
    }

    async create(){
        let response = await this.props.projectService.addProject(this.props.uid, 
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
            
            window.location.href= "/issues/"+this.props.uid+"/"+response.id
        }
    }

    render(){
        this.makeStateList();

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

                                <Form.Field>
                                    <label>Labels</label>
                                    <Dropdown 
                                        placeholder='Choose Allowed Labels'
                                        search
                                        selection
                                        fluid
                                        multiple
                                        allowAdditions
                                        value={this.state.allowedLabels}
                                        options={this.state.labelsOptions}
                                        onAddItem={this.handleAdditionLabels}
                                        onChange={this.handleChangeDropdow}
                                    />
                                </Form.Field>

                                <Divider/>
                                <h4>States</h4>
                                <pre>{JSON.stringify(this.list, null, 2)}</pre>
                                <Form.Field>
                                    <label>Current</label>
                                    <input placeholder='State' state={this.state.stateBuffer} onChange={this.handleInputChange("stateBuffer")}/>
                                    <label>Next</label>
                                    <input placeholder='Next' state={this.state.nextBuffer} onChange={this.handleInputChange("nextBuffer")}/>
                                    <Button floated='left' onClick={this.stateAdd} content="Add" />
                                    <Button floated='right' onClick={this.stateDelete} content="Delete By Name" />
                                </Form.Field>                    
                            </Form>
                        </Grid.Column>
                    </Grid.Row>
                    <Divider/>
                    <Grid.Row>
                        <Grid.Column floated="left">
                            <Button content="Create" onClick={this.create} />  
                        </Grid.Column>
                        <Message floated="left"
                         errorResponse={this.state.errorResponse} normalResponse={this.state.normalResponse}/>
                    </Grid.Row>
                </Grid>
            </Container>
        </div>)
    }
}
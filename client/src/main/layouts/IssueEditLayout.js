import React from 'react'
import {
  Container,
  Divider,
  Grid,
  Header,
  Button,
  Form,
  Dropdown
} from 'semantic-ui-react'

export default class IssueEditLayout extends React.Component{
    componentDidMount(){
        this.props.createPage()
    }
    
    render(){
        return (<div>
            <Container text style={{ marginTop: '7em' }}>
                <Header as='h1'id="userName"/>
                <p id="userId"></p>
                <Divider/>
                <Grid relaxed divided>
                    <Grid.Row>
                            <Grid.Column width='7'>
                                <Form id="editUser" onSubmit={this.props.handleEdit}>

                                    <Form.Field>
                                        <label>New Title</label>
                                        <input id="newIsTit" placeholder='New Title' />
                                    </Form.Field>
                                    <Form.Field>
                                        <label>New Description</label>
                                        <input id="newDesc" placeholder='New Description' />
                                    </Form.Field>
                                    <Form.Field>
                                        <label>New State</label>
                                        <input id="newState" placeholder='New State' />
                                    </Form.Field>                            
                                    <Form.Field>
                                        <Container>
                                            <p> Selected Labels: </p>
                                            <p id="SelLabelsE"></p>
                                        </Container>
                                    </Form.Field>
                                    <Button size='huge' floated='left' type='submit' content="Edit" />
                                </Form>
                            </Grid.Column>
                            <Grid.Column width='7'>
                                <Dropdown multiple selection basic simple text='Labels'>
                                    <Dropdown.Menu id="dI"> 
                                        <div id="1"/>
                                        <div id="2"/>
                                        <div id="3"/>
                                        <div id="4"/>
                                        <div id="5"/>
                                        <div id="6"/>
                                        <div id="7"/>
                                        <div id="8"/>
                                        <div id="9"/>
                                        <div id="10"/>
                                        <div id="11"/>
                                        <div id="12"/>
                                        <div id="13"/>
                                        <div id="14"/>
                                        <div id="15"/>
                                        <div id="16"/>
                                        <div id="17"/>
                                        <div id="18"/>
                                        <div id="19"/>
                                        <div id="20"/>
                                        <div id="21"/>
                                        <div id="22"/>
                                        <div id="23"/>
                                        <div id="24"/>
                                        <div id="25"/>
                                    </Dropdown.Menu>
                                </Dropdown>
                            </Grid.Column>
                    </Grid.Row>
                    <Divider/>
                </Grid>
            </Container>
        </div>)
    }
}
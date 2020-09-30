import React from 'react'
import {
  Container,
  Grid,
  Button,
  Form,
  Dropdown
} from 'semantic-ui-react'

export default class ProfileLayout extends React.Component{
    componentDidMount(){
        this.props.createPage()
    }
    render(){
        return (<div>
            <Container text style={{ marginTop: '7em' }}>
                <Grid relaxed divided centered>
                    <Grid.Column width='5'>
                        <Form onSubmit={this.props.handleAdd}>
                            <Form.Field>
                                <label>Title</label>
                                <input id="IsCreT" placeholder='Title' />
                            </Form.Field>
                            <Form.TextArea label="Description" id="IsCreD" placeholder='Description' />
                            
                            <Form.Field>
                                <Container>
                                    <p> Selected Labels: </p>
                                    <p id="SelLabels"></p>
                                </Container>
                            </Form.Field>
                            <Button centered size='big' type='submit'>Add</Button>
                        </Form>
                    </Grid.Column>
                    <Grid.Column width='7'>
                        <Dropdown id="set_labels" multiple selection basic simple text='Labels'>
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
                </Grid>
            </Container>
        </div>)
    }
}
import React from 'react'
import {
  Container,
  Divider,
  Grid,
  Header,
  Button,
  Form
} from 'semantic-ui-react'

export default class ProfileLayout extends React.Component {
    componentDidMount() {
        this.props.createPage()
    }

    render() {
        return (
            <div>
                <Container text style={{ marginTop: '7em' }}>
                    <Header as='h1'id="userName"/>
                    
                    <p id="userId"></p>
                    
                    <Divider/>
                    
                    <Grid relaxed>
                        <Grid.Row relaxed>
                            <Grid.Column width='5' floated='left'>
                                <Form onSubmit={this.props.handleDelete}>
                                    <Form.Field>
                                        <label>Password</label>
                                        <input id="pass1" placeholder='Password' />
                                    </Form.Field>
                                    
                                    <Form.Field>
                                        <label>Confirm Password</label>
                                        <input id="pass2" placeholder='Confirm Password' />
                                    </Form.Field>
                                    
                                    <Button floated='left' type='submit'>Delete Account</Button>
                                </Form>
                            </Grid.Column>
                            
                            <Grid.Column width='5' floated='right'>
                                <Form id="editUser" onSubmit={this.props.handleEdit}>
                                    <Form.Field>
                                        <label>New Username</label>
                                        <input id="newUsName" placeholder='New Username' />
                                    </Form.Field>
                                    
                                    <Form.Field>
                                        <label>New Password</label>
                                        <input id="newPass" placeholder='NewPass' />
                                    </Form.Field>
                                    
                                    <Button floated='left' type='submit' content="Edit" />
                                </Form>
                            </Grid.Column>
                        </Grid.Row>
                        
                        <Divider/>
                        
                        <Grid.Row>
                            <Button a href = "/logout">
                                Logout 
                            </Button>
                        </Grid.Row>
                    </Grid>
                </Container>
            </div>
        )
    }
}
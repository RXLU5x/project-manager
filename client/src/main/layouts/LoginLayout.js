import React from 'react'
import { Button, Form, Grid, Header, Image, Message, Segment } from 'semantic-ui-react'

const LoginLayout = (handleInputChange,handleSubmit) => (
  <div>
    <Grid textAlign='center' style={{ height: '90vh' }} verticalAlign='middle'>
      <Grid.Column style={{ maxWidth: 450 }}>
        <Header as='h2' color='teal' textAlign='center'>
         Log-in to your account
        </Header>
        <Form size='large' onSubmit={handleSubmit}>
          <Segment stacked>
            <Form.Input fluid icon='user' iconPosition='left' placeholder='Username' onChange={handleInputChange}/>
            <Form.Input
              fluid
              icon='lock'
              iconPosition='left'
              placeholder='Password'
              type='password'
              onChange={handleInputChange}
            />

            <Button color='teal' fluid size='large' onChange={handleInputChange}>
              Login
            </Button>
          </Segment>
        </Form>
        <Message>
          New to us? <a href='/register'>Sign Up</a>
        </Message>
      </Grid.Column>
    </Grid>
  </div>
)

export default LoginLayout
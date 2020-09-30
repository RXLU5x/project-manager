import React from 'react'
import { Button, Form, Grid, Header, Message, Segment } from 'semantic-ui-react'

const RegisterLayout = (handleInputChange,handleSubmit) => (
  <div>
    <Grid textAlign='center' style={{ height: '90vh' }} verticalAlign='middle'>
      <Grid.Column style={{ maxWidth: 450 }}>
        <Header as='h2' color='teal' textAlign='center'>
         Sign up to get your own account
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
              Register
            </Button>
          </Segment>
        </Form>
        <Message>
          Already have one? <a href='/login'>Log-in</a>
        </Message>
      </Grid.Column>
    </Grid>
  </div>
)

export default RegisterLayout
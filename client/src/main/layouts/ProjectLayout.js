import React from 'react'
import {
  Container,
  Divider,
  Dropdown,
  Grid,
  Header,
  Image,
  List,
  Segment,
  Button,
} from 'semantic-ui-react'

export default class FixedMenuLayout extends React.Component{
    onCreateI = ()=> {
        document.getElementById("dI1").innerText = "heheh"
    }
    componentDidMount(){
        this.props.createPage()
        this.props.createIssues()
    }

    render(){
        return (
            <div>
                <Container text style={{ marginTop: '7em' }}>
                <Header textAlign='center' as='h1'id="projectName"></Header>
                <Grid centered>
                    <Grid.Column width={5}>
                        <div id="projectId" />
                        <div id="projectUId"/>
                        <div id="projectDesc" />
                        <div id="projectLabels"/>
                        <div id="projectStates"/>
                    </Grid.Column>
                    <Grid.Column width={5}>
                    <Dropdown basic item simple text='Issues'>
                    <Dropdown.Menu id="dI"> 
                        <div id="dI1" />
                        <div id="dI2" />
                        <div id="dI3" />
                        <div id="dI4" />
                        <div id="dI5" />
                        <div id="dI6" />
                        <div id="dI7" />
                        <div id="dI8" />
                        <div id="dI9" />
                        <div id="dI10"/>
                        <Dropdown.Item id="dI11">
                            <Button id="PrevPagListP" onClick={this.props.onPreviousPage}>Page 1</Button>
                            <Button id="NextPagListP" onClick={this.props.onNextPage}>Page 2</Button>
                        </Dropdown.Item> 
                    </Dropdown.Menu>
                    </Dropdown>
                    </Grid.Column>
                </Grid>
                </Container>

                <Segment inverted vertical style={{ margin: '5em 0em 0em', padding: '5em 0em' }}>
                <Container textAlign='center'>
                    <Grid divided inverted stackable>
                    <Grid.Column width={3}>
                        <Header inverted as='h4' content='Project Elements' />
                        <List link inverted>
                        <List.Item as='a'>Element One</List.Item>
                        <List.Item as='a'>Element Two</List.Item>
                        <List.Item as='a'>Element Three</List.Item>
                        <List.Item as='a'>Element Four</List.Item>
                        </List>
                    </Grid.Column>
                
                    <Grid.Column width={7}>
                        <Header inverted as='h4' content='Project Description' />
                        <p>
                        PODE SER USADO PARA METER DESCRICAO DO PROJETO
                        Extra space for a call to action inside the footer that could help re-engage users.
                        </p>
                    </Grid.Column>
                    </Grid>

                    <Divider inverted section />
                    <Image centered size='mini' src='/logo.png' />
                    <List horizontal inverted divided link size='small'>
                    <List.Item as='a' href='#'>
                        Site Map
                    </List.Item>
                    <List.Item as='a' href='#'>
                        Contact Us
                    </List.Item>
                    <List.Item as='a' href='#'>
                        Terms and Conditions
                    </List.Item>
                    <List.Item as='a' href='#'>
                        Privacy Policy
                    </List.Item>
                    </List>
                </Container>
                </Segment>
            </div>
            )
    }
}
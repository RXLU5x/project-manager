import React from 'react'
import {
  Container,
  Form,
  Comment,
  Grid,
  Header,
  Button,
} from 'semantic-ui-react'

export default class FixedMenuLayout extends React.Component{
    onCreateI = ()=> {
        document.getElementById("dI1").innerText = "heheh"
    }
    componentDidMount(){
        this.props.createPage()
    }

    render(){
        return (
            <div>
                <Container text style={{ marginTop: '7em' }}>
                    
                <Grid divided relaxed centered>

                    <Grid.Row>    
                        <Grid.Column width={5}>
                            <p id="issueTitle"></p>
                            <p id="issueId"></p>
                            <p id="issueUId"></p>
                            <p id="issuePId"></p>
                            <p id="issueState"></p>
                            <p id="issueDesc"></p>
                            <p id="issueCrea"></p>
                            <p id="issueClosed"></p>
                            <p id="issueLabels"></p>
                        </Grid.Column>
                        <Grid.Column width='10'>
                            <Comment.Group textAlign='left' size='big'>
                                <Grid.Row>
                                    <Grid.Column width='2'>
                                        <Header as='h3'>
                                            <p>Comments:</p>
                                            <p id="PageNC"></p>
                                        </Header>
                                    </Grid.Column>
                                    <Grid.Column width='1'>
                                        <Button id="PrevPagListC" onClick={this.props.onPreviousPage} >Page 1</Button>
                                    </Grid.Column>
                                    <Grid.Column width='1'>
                                        <Button id="NextPagListC" onClick={this.props.onNextPage} >Page 2</Button>
                                    </Grid.Column>
                                </Grid.Row>
                                <div id="C1" />
                                <div id="C2" />
                                <div id="C3" />
                                <div id="C4" />
                                <div id="C5" />
                                <div id="C6" />
                                <div id="C7" />
                                <div id="C8" />
                                <div id="C9" />
                                <div id="C10" />
                                <div id="C11" />
                                <div id="C12" />
                                <div id="C13" />
                                <div id="C14" />
                                <div id="C15" />
                                <div id="C16" />
                                <div id="C17" />
                                <div id="C18" />
                                <div id="C19" />
                                <div id="C20" />
                                <div id="C21" />
                                <div id="C22" />
                                <div id="C23" />
                                <div id="C24" />
                                <div id="C25" />
                        </Comment.Group>
                        <Form onSubmit={this.props.onAddComm}>
                            <Form.TextArea id='addCommTxt' />
                            <Button content='Add Comment' labelPosition='left' icon='edit' primary />
                        </Form>
                        </Grid.Column>
                    </Grid.Row>
                </Grid>
                </Container>
            </div>
            )
    }
}
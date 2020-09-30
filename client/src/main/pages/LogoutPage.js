
import React from 'react'
import LoginContext from '../login/LoginContext'

import { Header, Grid, Button } from 'semantic-ui-react'
import MenuProject from '../layouts/menus/MenuProject'



export default class LogoutPage extends React.Component {

    render(){
        return(
            <LoginContext.Consumer>
                {value => {
                    return (
                        <div>
                        <MenuProject/>            
                            <Header textAlign='center'> Are you SURE you want to leave ? </Header>
                        <Grid centered columns={10} relaxed>
                            <Grid.Row>
                                <Grid.Column relaxed>
                                    <Button.Group>
                                        <Button negative a href="/login" onClick={value.loginService.logout}>Yes</Button>
                                        <Button.Or />
                                        <Button positive a href="/home">No</Button>
                                    </Button.Group>
                                </Grid.Column>
                            </Grid.Row>
                        </Grid>
                        </div>
                    )}}   
            </LoginContext.Consumer>
        )
    }
}

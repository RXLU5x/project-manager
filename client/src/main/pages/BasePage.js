import React from 'react'
import MenuHome from '../layouts/menus/MenuHome'
import { Header } from 'semantic-ui-react'
import LoginContext from '../login/LoginContext'

export default class BasePage extends React.Component {

    render(){
        return(
            <LoginContext.Consumer>
                {value => {
                    return (
                    <div>
                    <MenuHome uid={value.loginService.getID()}/>            
                        <Header location='centered'>WELCOME TO PROJECTS MANAGEMENT UI</Header>
                        <p>Please Login or Sign up to start using our amazing API !!</p>
                    </div>)}
                }
            </LoginContext.Consumer>
        )
    }
}

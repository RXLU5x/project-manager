import React from 'react'
import { Header } from 'semantic-ui-react'
import ProfileLayout from '../layouts/ProfileLayout'

import MenuHome from '../layouts/menus/MenuHome'
import LoginContext from '../login/LoginContext'

let context
let user

class ProfilePage extends React.Component {    
    createPage = async () => {
        user = await this.props.userService.getUser(this.props.uid)
        document.getElementById("userName").innerText = "Username: " + user.username
        document.getElementById("userId").innerText = "User ID: " + user.id
    }

    render() {
        return (
            <LoginContext.Consumer>{ 
                value => { 
                    context = value
                    
                    return (
                        <div>
                            <MenuHome uid={this.props.uid}/>
                            <Header as="h1" textAlign='center'>Profile</Header>
                            <ProfileLayout getUserInfo = {this.getUserInfo} createPage = {this.createPage} uid = {this.props.uid}
                                handleEdit = { 
                                    async () => {
                                        let newUN = document.getElementById("newUsName").value
                                        
                                        if(!newUN)
                                            newUN = user.username
                                        
                                        let newPass = document.getElementById("newPass").value
                                        
                                        if(!newPass)
                                            newPass = user.password
                                        
                                        let res = await this.props.userService.updateUser(this.props.uid, newUN, newPass)
                                        
                                        if(res)
                                            context.loginService.login(newUN, newPass)

                                        window.location.reload()
                                    }
                                }

                                handleDelete = {
                                    () => {
                                        let pass1 = document.getElementById("pass1").value
                                        let pass2 = document.getElementById("pass2").value
                                        
                                        if(pass1 === pass2) {
                                            if(pass1 === user.password) {
                                                this.props.userService.deleteUser(user.id)
                                                context.loginService.logout()
                                                window.location.href = ""
                                            }
                                        }
                                    }
                                }
                            />
                        </div>
                    )
                }
            }</LoginContext.Consumer>
        )
    }
}

export default ProfilePage
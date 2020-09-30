import React, { Component } from "react";
import { Menu } from 'semantic-ui-react'

export default class MenuBase extends Component {
    state = {}

    handleItemClick = (e, { name }) => this.setState({ activeItem: name })

    render(){
        const { activeItem } = this.state
        let hrfP = "/" //+ this.props.pid
        let hrfI = "/"//`/project/${this.props.uid}/${this.props.pid}/issues`

        return (
        <Menu id>
            <Menu.Item
            name='home'
            active={activeItem === 'home'}
            a href="/home"
            onClick={this.handleItemClick}
            >
            Home
            </Menu.Item>

            <Menu.Item
            name='projects'
            active={activeItem === 'projects'}
            a href={hrfP}
            onClick={this.handleItemClick}
            >
            Projects
            </Menu.Item>
            <Menu.Item
            name='issues'
            active={activeItem === 'issues'}
            a href={hrfI}
            onClick={this.handleItemClick}
            >
            Issues
            </Menu.Item>

            <Menu.Menu position='right'>
                <Menu.Item
                    name='login'
                    active={activeItem === 'login'}
                    a href="./login"
                    onClick={this.handleItemClick}
                >
                    Login
                </Menu.Item>

                <Menu.Item
                    name='signup'
                    active={activeItem === 'signup'}
                    onClick={this.handleItemClick}
                >
                    Sign Up
                </Menu.Item>

                <Menu.Item
                    name='help'
                    active={activeItem === 'help'}
                    onClick={this.handleItemClick}
                >
                    Help
            </Menu.Item>
        </Menu.Menu>
        </Menu>
        )
    }


}
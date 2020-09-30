import React, { Component } from 'react'
import { Menu, Dropdown } from 'semantic-ui-react'

export default class MenuMain extends Component {
  state = {}

  handleItemClick = (e, { name }) => this.setState({ activeItem: name })

  render() {
    const { activeItem } = this.state

    let hrfPro = `/profile/${this.props.uid}`
    let hrfP = `/projects/${this.props.uid}`
    let hrfCP= `/projects/${this.props.uid}/create`  //PAGINA PARA CRIAR NOVO PROJECT
    let hrfI = `/issues/${this.props.uid}`
    let hrfCI = `/issues/${this.props.uid}/create`   //PAGINA PARA CRIAR NOVO ISSUE

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

        <Dropdown pointing className='link item' text="Projects" >
              <Dropdown.Menu>
                <Dropdown.Item
                    name='viewAllProj'
                    active={activeItem === 'viewAllProj'}
                    a href={hrfP}
                    onClick={this.handleItemClick}>
                  View All
                </Dropdown.Item>
                <Dropdown.Item
                    name='createProj'
                    active={activeItem === 'createProj'}
                    a href={hrfCP}
                    onClick={this.handleItemClick}>
                  Create
                </Dropdown.Item>
              </Dropdown.Menu>
        </Dropdown>
        <Dropdown pointing className='link item' text="Issues" >
              <Dropdown.Menu>
                <Dropdown.Item
                    name='viewIsss'
                    active={activeItem === 'viewIsss'}
                    a href={hrfI}
                    onClick={this.handleItemClick}>
                  View All
                </Dropdown.Item>
                <Dropdown.Item
                    name='createIss'
                    active={activeItem === 'createIss'}
                    a href={hrfCI}
                    onClick={this.handleItemClick}>
                  Create
                </Dropdown.Item>
              </Dropdown.Menu>
        </Dropdown>


        <Menu.Menu position='right'>
        <Menu.Item
            name='profile'
            active={activeItem === 'profile'}
            a href={hrfPro}
            onClick={this.handleItemClick}
          >
            Profile
          </Menu.Item>
          <Menu.Item
            name='logout'
            active={activeItem === 'logout'}
            a href="/logout"
            onClick={this.handleItemClick}
          >
            Logout
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
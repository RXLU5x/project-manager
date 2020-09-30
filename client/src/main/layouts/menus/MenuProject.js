import React, { Component } from 'react'
import { Menu,Dropdown } from 'semantic-ui-react'

export default class MenuProject extends Component {
  state = {}

  handleItemClick = (e, { name }) => this.setState({ activeItem: name })
  /*<Menu.Item
      name='projects'
      active={activeItem === 'projects'}
      a href="/projects/1"
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
    </Menu.Item>*/
  render() {
    const { activeItem } = this.state
    let hrfPs = "/projects/" + this.props.uid
    let hrfCP= `/projects/${this.props.uid}/create`                    //PAGINA PARA CRIAR NOVO PROJECT
    let hrfP= `/projects/${this.props.uid}/${this.props.pid}`                    //PAGINA PARA VER PROJECT ATUAL
    let hrfEP= `/projects/${this.props.uid}/${this.props.pid}/edit`                    //PAGINA PARA CRIAR NOVO PROJECT
    let hrfI = `/issues/${this.props.uid}/${this.props.pid}`
    let hrfCI = `/issues/${this.props.uid}/${this.props.pid}/create`   //PAGINA PARA CRIAR NOVO ISSUE


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
                    a href={hrfPs}
                    onClick={this.handleItemClick}>
                  View All
                </Dropdown.Item>
                <Dropdown.Item
                    name='viewProj'
                    active={activeItem === 'viewProj'}
                    a href={hrfP}
                    onClick={this.handleItemClick}>
                  View Project
                </Dropdown.Item>
                <Dropdown.Item
                    name='editProj'
                    active={activeItem === 'editProj'}
                    a href={hrfEP}
                    onClick={this.handleItemClick}>
                  Edit Current
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
                    name='viewIss'
                    active={activeItem === 'viewIss'}
                    a href={hrfI}
                    onClick={this.handleItemClick}>
                  View project's Issues
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
            a href="/profile/1"
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
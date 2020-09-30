import React from 'react'
import MenuHome from '../layouts/menus/MenuHome'
import { Message, Container } from 'semantic-ui-react'

export default class HomePage extends React.Component{
    testOnChange = async (idE) => {  
    }
    render(){
        console.log(this.props.uid)
        return (
            <div>
                <MenuHome uid={this.props.uid}/>
                <Container>
                    <Message
                        size="massive"
                        content="Welcome to our Website!!!"
                    />
                    <Message
                        size="large"
                        content="Please use the above menu to get around."
                    />
                </Container>
            </div>
        )
    }
}
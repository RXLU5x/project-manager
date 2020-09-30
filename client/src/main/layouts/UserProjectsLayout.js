import React from 'react'

import {
  Button,
  Header,
  List,
  Grid
} from 'semantic-ui-react'

export default class FixedMenuLayout extends React.Component{
    
    componentDidMount(){ this.props.onCreate() }

    render(){
        return (
            <div>
                <List size='large' relaxed id="projectList"  >
                    <Grid centered relaxed divided>
                        <Grid.Row>
                            <Grid.Column textAlign='center'>
                                <Button id="PrevPagList" onClick={this.props.onPreviousPage} >Page 1</Button>
                            </Grid.Column>
                            <Grid.Column textAlign='center' widescreen='5'>
                             <Header as="h1" textAlign='center'><p>{this.props.headerTitle}</p><p id="PageN">Page 1</p> </Header>    
                            </Grid.Column>
                            <Grid.Column textAlign='center'>
                                <Button id="NextPagList" onClick={this.props.onNextPage} >Page 2</Button>
                            </Grid.Column>

                        </Grid.Row>
                        <Grid.Column width={2}>
                            <div id="p1"></div>
                            <div id="p2"></div>
                            <div id="p3"></div>
                            <div id="p4"></div>
                            <div id="p5"></div>
                        </Grid.Column>

                        <Grid.Column width={2}>
                            <div id="p6"></div>
                            <div id="p7"></div>
                            <div id="p8"></div>
                            <div id="p9"></div>
                            <div id="p10"></div>
                        </Grid.Column>
                        
                        <Grid.Column width={2}>
                            <div id="p11"></div>
                            <div id="p12"></div>
                            <div id="p13"></div>
                            <div id="p14"></div>
                            <div id="p15"></div>
                        </Grid.Column>

                        <Grid.Column width={2}>
                            <div id="p16"></div>
                            <div id="p17"></div>
                            <div id="p18"></div>
                            <div id="p19"></div>
                            <div id="p20"></div>
                        </Grid.Column>

                        <Grid.Column width={2}>                                    
                            <div id="p21"></div>
                            <div id="p22"></div>
                            <div id="p23"></div>
                            <div id="p24"></div>
                            <div id="p25"></div>
                        </Grid.Column>
                    </Grid>             
                </List>
            </div>
        )
    }
}
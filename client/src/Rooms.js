import React, { Component } from 'react'

import axios from 'axios'
import Bord from './Bord';
import { Button, Col, Card } from 'react-bootstrap';

const url = "http://localhost:8080"

class Rooms extends Component {
    constructor() {
        super()
        this.state = {
            id: '',
            number: '',
            contexts: []
        }

        this.handleClick = this.handleClick.bind(this)
        this.addToRoom = this.addToRoom.bind(this)
    }

    handleClick() {
        axios.post(url + "/context");
        axios.get(url + "/context")
            .then(response => this.setState({ contexts: response.data }))
    }

    addToRoom(contextId, number) {
        var path = url + "/context" + "/" + contextId + "/" + this.props.userid
        axios.get(path)
        this.setState({ id: contextId, number })
    }

    handleChange(e) {
        this.props.addUser(e.target.value);
    }

    componentDidMount() {
        axios.get(url + "/context")
            .then(response => this.setState({ contexts: response.data }));
    }

    render() {
        var roomNumber = 0
        var children = 0
        if (this.state.id !== '') {
            return <Card userid={this.props.userid} className="text-center mt-5">
                <div className="card-header">
                    Room №{this.state.number}
                </div>
                <Bord userid={this.props.userid} roomid={this.state.id} />
            </Card>
        }
        return <Col userid={this.props.userid} className="mt-5">
            <Button className='btn btn-success mt-3 ml-2' onClick={this.handleClick}>Add room</Button>
            <div className="d-flex flex-wrap">
                {
                    this.state.contexts.map(context => (
                        <div className="card mt-2 ml-2 mr-2 row" key={context.id + children++}>
                            <div className="card-body" key={context.id + children++}>
                                <h5 className="card-title" key={context.id + children++}>Room №{++roomNumber}</h5>
                                <Button className='col' onClick={(e) => this.addToRoom(context.id, roomNumber)} key={context.id + children++}>Come in Room {roomNumber}</Button>
                            </div>
                        </div>))
                }
            </div>
        </Col>
    }
}
export default Rooms
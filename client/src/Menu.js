import React, { Component } from 'react'

import axios from 'axios'

import UserName from './UserName.js'
import Rooms from './Rooms'
import './Common.scss';
import { Button } from 'react-bootstrap';
import './Menu.scss'


const url = "http://localhost:8080"

class Menu extends Component {
    constructor() {
        super()
        this.state = {
            id: '',
            username: ''
        }

        this.onAddUser = this.onAddUser.bind(this);

        this.handleClick = this.handleClick.bind(this)
        this.handleChange = this.handleChange.bind(this)
    }

    onAddUser(userId) {
        this.setState({ userId: userId })
    }

    handleClick(event) {
        axios.get(url + "/user?name=" + this.state.username)
            .then(response => this.setState({ id: response.data.id }))
        event.preventDefault();
    }

    handleChange(event) {
        this.setState({ username: event.target.value });
    }

    render() {
        if (this.state.id === '') {
            return (
                <div>
                    <UserName id={this.state.id} name={this.state.username} />

                    <div className="d-flex justify-content-center text-center mt-5">
                        <form className="form-signin" onSubmit={this.handleClick}>
                            <div className="form-group">
                                <label>User Name:</label>
                                <input className="form-control" type="text" value={this.state.username} onChange={this.handleChange} />
                            </div>
                            <Button variant="primary justify-content-center" type="submit">Strat game</Button>
                        </form>
                    </div>

                </div>
            )
        }

        return <div>
            <UserName id={this.state.id} name={this.state.username} />
            <Rooms userid={this.state.id} />
        </div>
    }


}
export default Menu
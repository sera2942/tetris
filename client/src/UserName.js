import React, { Component } from 'react'
import { Col, Navbar } from 'react-bootstrap';


class UserName extends Component {

  render() {
    return (
      <Navbar className="navbar navbar-expand-lg navbar-light bg-light">
        <h1 class="collapse navbar-collapse">Tetris</h1>
        <span class="navbar-text">
          <p>User name: {this.props.name}</p>
          <a href="#" className="badge badge-info mb-2 navbar-text">User id: {this.props.id}</a>
        </span>        
      </Navbar>
    )
  }
}
export default UserName
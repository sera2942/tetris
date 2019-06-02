import React, { Component } from 'react'
import logo from './tetris_logo.svg'

class UserName extends Component {

  render() {
    if (this.props.id !== '') {
      return (
        <nav class="navbar navbar-dark bg-dark shadow-sm">
          <a href="#" class="navbar-brand d-flex align-items-center">
            <img src={logo} alt="Logo" />
            <h1 class="navbar-brand d-flex align-items-center">Tetris</h1>
          </a>
          <span class="navbar-toggler">
            <p>User name: {this.props.name}</p>
            <a href="#" className="badge badge-info mb-2 navbar-text">User id: {this.props.id}</a>
          </span>
        </nav>

      )
    } else {
      return (
        <nav class="navbar navbar-dark bg-dark shadow-sm">
          <a href="#" class="navbar-brand d-flex align-items-center">
            <img src={logo} alt="Logo" />
            <h1 class="navbar-brand d-flex align-items-center">Tetris</h1>
          </a>
        </nav>

      )
    }

  }
}
export default UserName
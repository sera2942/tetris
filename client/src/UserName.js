import React, { Component } from 'react'
import { Col, Card } from 'react-bootstrap';


class UserName extends Component {

  render() {
    return (
      <Col className="col-4 mt-5">
        <Card className = "jumbotrona">
          <div className="card-body">
            <h5 className="card-title">User name: {this.props.name}</h5>
            <a href="#" className="badge badge-info mb-2">User id: {this.props.id}</a>
          </div>
        </Card>
      </Col>
    )
  }
}
export default UserName
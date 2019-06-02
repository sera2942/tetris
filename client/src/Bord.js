import React, { Component } from 'react';
import './Bord.scss';
import './Common.scss';
import Popup from 'reactjs-popup'

const {
    RSocketClient,
    Utf8Encoders,
    JsonSerializers,
} = require('rsocket-core');
const RSocketWebSocketClient = require('rsocket-websocket-client').default;
const url = 'ws://localhost:9988';

class Bord extends Component {

    constructor(props) {
        super()
        this.state = {
            list: [],
            gameIsOver: false
        }

        this.onAddUser = this.onAddUser.bind(this);

    }

    onAddUser(userId) {
        this.setState({ userId: userId })
    }

    componentWillMount() {
        document.addEventListener("keydown", this.onKeyPressed.bind(this));
    }

    componentWillUnmount() {
        document.removeEventListener("keydown", this.onKeyPressed.bind(this));
    }

    onKeyPressed(e) {
        var action = null
        if (e.keyCode == '37') {
            action = "EAST"
        }
        if (e.keyCode == '39') {
            action = "WEST"
        }
        if (e.keyCode == '40') {
            action = "SOUTH"
        }
        if (e.keyCode == '32') {
            action = "TURN"
        }
        if (action !== null) {
            const client = new RSocketClient({
                serializers: JsonSerializers,
                setup: {
                    keepAlive: 60000,
                    lifetime: 180000,
                    dataMimeType: 'application/json',
                    metadataMimeType: 'application/json',
                },
                transport: new RSocketWebSocketClient({ url: url }),
            });

            client.connect().subscribe({
                onComplete: socket => {
                    socket.fireAndForget({
                        data: { contextId: this.props.roomid, userId: this.props.userid, actionType: action },
                        metadata: { another: { json: { value: true } } },
                    });
                },
                onError: error => console.error(error),
                onSubscribe: cancel => {/* call cancel() to abort */ }
            });
        }
    }

    render() {

        const PopupExample = () => (
            <Popup className="text-danger text-center border border-warning rounded test"
                open={this.state.gameIsOver}
                closeOnDocumentClick>
                {close => (
                    <h1>Game OVER. You are loser &#128514;</h1>
                )}
            </Popup>
        )

        return <div className="overflow-auto p-1" onKeyDown={this.onKeyPressed}>
            {
                this.state.list.map((row) =>
                    <div className="row pl-3 pr-3" style={{ width: (40 * row.length) + "px" }} >
                        {
                            row.map(
                                (cell) => <div className={getColor(cell, this.props.userid)}></div>
                            )
                        }
                    </div>)
            }
            <PopupExample />

        </div>
    }

    componentDidMount() {
        const client = new RSocketClient({
            setup: {
                keepAlive: 60000,
                lifetime: 180000,
                dataMimeType: 'binary',
                metadataMimeType: 'binary',
            },
            transport: new RSocketWebSocketClient({
                url,
                debug: true,
            }, Utf8Encoders),
        });

        // Open the connection
        client.connect().subscribe({
            onComplete: socket => {
                socket.requestStream({
                    data: 'peace',
                    metadata: null,
                }).subscribe({
                    onComplete: () => console.log('complete'),
                    onError: error => console.error(error),
                    onNext: payload => {
                        var state = JSON.parse(payload.data)
                        console.log(state)

                        if (state.contextId === this.props.roomid) {
                            this.setState({ list: state.board })
                            if ("GAME_OVER" === state.typeState) {
                                this.setState({ gameIsOver: true })
                            }

                        }
                    },
                    onSubscribe: subscription => {
                        subscription.request(1000);
                    },
                });
            },
            onError: error => console.error(error),
            onSubscribe: cancel => {/* call cancel() to abort */ }
        });
    }

    componentWillUnmount() {

    }
}

function getColor(cell, userId) {
    // console.log(number)
    var result = "column col d-inline ml-0 border"
    if (cell.color === "RED") {
        if (cell.userId == userId) {
            result = result + " cellOwnRed rounded"
        } else {
            result = result + " cellRed"
        }
    } else if (cell.color === "BLUE") {
        if (cell.userId == userId) {
            result = result + " cellOwnBlue rounded"
        } else {
            result = result + " cellBlue"
        }
    } else if (cell.color === "YELLOW") {
        if (cell.userId == userId) {
            result = result + " cellOwnYellow rounded"
        } else {
            result = result + " cellYellow"
        }
    } else {
        result = result + " border border-secondary"
    }

    switch(cell.color) {
        case "RED" :

    }
    if (cell.base) {
        result = result + " border-success"
    }

    return result
};

export default Bord
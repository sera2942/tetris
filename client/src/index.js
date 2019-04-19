import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';
import RSocket from './RSocket';

const list = [[0, 1, 0, 1], [1, 1, 0, 1], [1, 0, 0, 1]]
const option = {
    host: "localhost",
    port: "8080",
}
function Bord(prop) {
    return prop.list.map((list) => <div className="line">
        {
            list.map(
                (number) => <div className={getColor(number)}>{number}</div>
            )
        }
    </div>)
};

function getColor(number) {
    if (number === 1) {
        RSocket.getClientTransport('tcp', option)
        return "column cell"
    }
    return "column"
}

ReactDOM.render(<Bord list={list} />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();

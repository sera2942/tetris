import Deferred from 'fbjs/lib/Deferred';
import { RSocketClient, Utf8Encoders } from 'rsocket-core';
// import RSocketTcpClient from 'rsocket-tcp-client';
import RSocketWebSocketClient from 'rsocket-websocket-client';

/**
 * Example client that sends requestStream, requests 5 payloads, and cancels
 * when they are received. Designed for use with the
 * conditional-request-handling example at
 *
 * github.com/ReactiveSocket/reactivesocket-cpp/tree/master/examples/conditional-request-handling
 */
export async function run(options) {
  console.log('Start client')
  const socket = await connect(options);
  let pending = 5;
  let subscription;
  const deferred = new Deferred();
  socket.requestStream({
    data: 'Joe',
    metadata: null,
  }).subscribe({
    onComplete() {
      deferred.resolve();
      console.log('onComplete()');
    },
    onError(error) {
      console.log('onError(%s)', error.message);
      deferred.reject(error);
    },
    onNext(payload) {
      console.log('onNext(%s)', payload.data);
      if (--pending === 0) {
        console.log('cancel()');
        subscription.cancel();
        deferred.resolve();
      }
    },
    onSubscribe(_subscription) {
      console.log('requestStream(%s)', pending);
      subscription = _subscription;
      subscription.request(pending);
    },
  });
  return deferred.getPromise();
}

export async function connect(options) {
  const url = "ws://127.0.0.1:9988"
  console.log("OPTION " + url + " port: " + options.port)
  const client = new RSocketClient({
    setup: {
      dataMimeType: 'binary',
      keepAlive: 1000000, // avoid sending during test
      lifetime: 100000,
      metadataMimeType: 'binary',
    },
    transport: new RSocketWebSocketClient({
      url,
      debug: true,
    }, Utf8Encoders),
  });
  return await client.connect();
}

export default RSocketClient
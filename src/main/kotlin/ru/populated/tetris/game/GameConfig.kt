package ru.populated.tetris.game

import io.reactivex.Flowable
import io.reactivex.Single
import io.rsocket.kotlin.*
import io.rsocket.kotlin.transport.netty.server.NettyContextCloseable
import io.rsocket.kotlin.transport.netty.server.WebsocketServerTransport
import io.rsocket.kotlin.util.AbstractRSocket
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean


@Configurable
class GameConfig {

    private val port = 8787

//    fun handler(setup: Setup, rSocket: RSocket): Single<RSocket> {
//        return Single.just(object : AbstractRSocket() {
//            override fun requestStream(payload: Payload): Flowable<Payload> {
//                return Flowable.just(DefaultPayload.text("server handler response"))
//            }
//        })
//    }
//
//    /**
//     *  Initialize the RSocket listener with WebSocket as the Server transport and listen to port. Sets handler()
//     */
//    @Bean
//    fun rSocket(): Single<NettyContextCloseable> = RSocketFactory
//            .receive()
//            .acceptor { { setup, rSocket -> handler(setup, rSocket) } } // server handler RSocket
//            .transport(WebsocketServerTransport.create(port))  // Netty websocket transport
//            .start()
}
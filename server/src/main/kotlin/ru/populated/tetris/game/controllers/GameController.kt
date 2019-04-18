package ru.populated.tetris.game.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.reactivex.Completable
import io.reactivex.Single
import io.rsocket.kotlin.Payload
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.RSocketFactory
import io.rsocket.kotlin.transport.netty.server.NettyContextCloseable
import io.rsocket.kotlin.transport.netty.server.WebsocketServerTransport
import io.rsocket.kotlin.util.AbstractRSocket
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller

@Controller
class GameController {

    private val LOG = LoggerFactory.getLogger(this.javaClass.name)
    private val port = 9988
    private val mapper = ObjectMapper().registerKotlinModule()
    private val closeable: Single<NettyContextCloseable> = initializeRSocket()

    init {
        LOG.info("GameController")
        closeable
                .subscribe({
                    LOG.info("subscribed = $it")
                }, {
                    LOG.error("it = $it")
                })
    }


    /**
     * Handler for the socket. Connects the sightings to the RSocket
     * and maps the items into JSON. If we wanted different types of handlers or a handle to the client side
     * RSocket, we could pass the acceptor lambda parameters here.
     */
    private fun handler(): Single<RSocket> {
        return Single.just(object : AbstractRSocket() {
            // Here we could implement more of the API from AbstractSocket and provide e.g. single request/response
            // data. We want just a stream and a single fire and forget without paying attention to the payload

//            override fun requestStream(payload: Payload): Flowable<Payload> {
//                return sighting.sightings().observeOn(Schedulers.io())
//                        .store {
//                            DefaultPayload.text(mapper.writeValueAsString(it))
//                        }
//            }

            override fun fireAndForget(payload: Payload): Completable {

                return Completable.complete()
            }
        })
    }

    /**
     *  Initialize the RSocket listener with WebSocket as the Server transport and listen to port. Sets handler()
     */
    private fun initializeRSocket(): Single<NettyContextCloseable> = RSocketFactory
            .receive()
            .acceptor { { _, _ -> handler() } } // server handler RSocket
            .transport(WebsocketServerTransport.create("0.0.0.0", port))
            .start()
}
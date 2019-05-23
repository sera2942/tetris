package ru.populated.tetris.game.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.rsocket.kotlin.DefaultPayload
import io.rsocket.kotlin.Payload
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.RSocketFactory
import io.rsocket.kotlin.transport.netty.server.NettyContextCloseable
import io.rsocket.kotlin.transport.netty.server.WebsocketServerTransport
import io.rsocket.kotlin.util.AbstractRSocket
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import ru.populated.tetris.game.model.BoardState
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.model.Event
import ru.populated.tetris.game.model.TypeState
import ru.populated.tetris.game.service.ContextService
import ru.populated.tetris.game.service.UserService
import ru.populated.tetris.game.service.actions.Action
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit

@Controller
class GameController {

    private val LOG = LoggerFactory.getLogger(this.javaClass.name)
    private val port = 9988
    private val mapper = ObjectMapper().registerKotlinModule()
    private val closeable: Single<NettyContextCloseable> = initializeRSocket()
    private var generationSpeed = 500L
    private var tickerSubject = BehaviorSubject.createDefault<Long>(generationSpeed)
    private val source = PublishSubject.create<BoardState>()

    private val queue: Queue<Event> = ConcurrentLinkedQueue()

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var contextService: ContextService

    @Autowired
    lateinit var actions: List<Action>

    init {
        createIntervalWithVariableTimer()
        LOG.info("GameController")
        closeable
                .subscribe({
                    LOG.info("subscribed = $it")
                }, {
                    LOG.error("it = $it")
                })
    }

    private fun handler(): Single<RSocket> {
        return Single.just(object : AbstractRSocket() {

            override fun requestStream(payload: Payload): Flowable<Payload> {
                return source
                        .toFlowable(BackpressureStrategy.DROP)
                        .onBackpressureBuffer(100, { LOG.info("Dropping due to backpressure") }, BackpressureOverflowStrategy.DROP_OLDEST)
                        .observeOn(Schedulers.io()).map { DefaultPayload.text(mapper.writeValueAsString(it)) }
            }

            override fun fireAndForget(payload: Payload): Completable {

                LOG.info("USER EVENT :: " + payload.dataUtf8)

                queue.add(mapper.readValue(payload.dataUtf8, Event::class.java))
                return Completable.complete()
            }
        })
    }

    private fun initializeRSocket(): Single<NettyContextCloseable> = RSocketFactory
            .receive()
            .acceptor { { _, _ -> handler() } }
            .transport(WebsocketServerTransport.create("0.0.0.0", port))
            .start()

    private fun createIntervalWithVariableTimer() {
        tickerSubject
                .switchMap { Observable.interval(generationSpeed, TimeUnit.MILLISECONDS) }
                .subscribe {
                    if (!queue.isEmpty()) {
                        val context = play()
                        val boardState = BoardState(context!!.id, TypeState.IN_GAME, context.gameField.bord)
                        source.onNext(boardState)
                        tickerSubject.onNext(10)
                    } else {
                        tickerSubject.onNext(generationSpeed)
                    }

                }
    }

    fun play(): Context? {

        val event = queue.poll()
        val context = contextService.getContextById(event.contextId) ?: return null
        val user = userService.findUserById(event.userId) ?: return null
        actions.stream()
                .forEach { it.doAction(user, context, event) }

        return context
    }
}
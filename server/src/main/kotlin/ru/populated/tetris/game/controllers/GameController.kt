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
import ru.populated.tetris.game.model.*
import ru.populated.tetris.game.service.ContextService
import ru.populated.tetris.game.service.UserService
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import kotlin.random.Random

@Controller
class GameController {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var contextService: ContextService

    private val queue: Queue<Event> = ConcurrentLinkedQueue()
    private val LOG = LoggerFactory.getLogger(this.javaClass.name)
    private val port = 9988
    private val mapper = ObjectMapper().registerKotlinModule()
    private val closeable: Single<NettyContextCloseable> = initializeRSocket()
    private var generationSpeed = 1000L
    private var tickerSubject = BehaviorSubject.createDefault<Long>(generationSpeed)
    private val source = PublishSubject.create<MutableList<MutableList<Cell>>>()

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

    /**
     * Handler for the socket. Connects the sightings to the RSocket
     * and maps the items into JSON. If we wanted different types of handlers or a handle to the client side
     * RSocket, we could pass the acceptor lambda parameters here.
     */
    private fun handler(): Single<RSocket> {
        return Single.just(object : AbstractRSocket() {
            // Here we could implement more of the API from AbstractSocket and provide e.g. single request/response
            // data. We want just a stream and a single fire and forget without paying attention to the payload

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

    /**
     *  Initialize the RSocket listener with WebSocket as the Server transport and listen to port. Sets handler()
     */
    private fun initializeRSocket(): Single<NettyContextCloseable> = RSocketFactory
            .receive()
            .acceptor { { _, _ -> handler() } } // server handler RSocket
            .transport(WebsocketServerTransport.create("0.0.0.0", port))
            .start()

    private fun createIntervalWithVariableTimer() {
        tickerSubject
                .switchMap { Observable.interval(generationSpeed, TimeUnit.MILLISECONDS) }
                .subscribe {
                    if (!queue.isEmpty()) {
                        var bord = play()?.bord
                        bord?.let { snapshot -> source.onNext(snapshot) }
                    }
                    tickerSubject.onNext(generationSpeed)
                }
    }

    fun nextEvent(): Event {
        return queue.poll()
    }

    fun play(): GameField? {

        val event = nextEvent()
        val context = contextService.getContextById(event.contextId) ?: return null
        val user = userService.findUserById(event.userId) ?: return null

        if (initUser(user, context)) {
            user.figure.forEach {
                putUserFigure(context, it, user)
            }
        } else {

            var doUserHaveAbilityOfMoving = haveAbilityOfMoving(user, context.gameField, event)
            var cellsBelongToUser = false
            var cellsIsEmpty = false

            if (doUserHaveAbilityOfMoving) {
                cellsBelongToUser = cellsBelongToUser(user, context.gameField, event)
                cellsIsEmpty = !cellsIsNotEmpty(user, context.gameField, event)
            }

            if (doUserHaveAbilityOfMoving && !cellsBelongToUser && cellsIsEmpty) {
                user.figure.forEach {
                    if (it.y!! >= 0) {
                        deleteUserFigure(context, it)
                    }
                }
                user.figure.forEach {

                    it.y = it.y!!.plus(event.direction.deltaY)
                    it.x = it.x!!.plus(event.direction.deltaX)
                    putUserFigure(context, it, user)

                }


            } else clearUserFigure(user, context.gameField, event, cellsBelongToUser, cellsIsEmpty, doUserHaveAbilityOfMoving)
        }


        return context.gameField
    }

    private fun deleteUserFigure(context: Context, cell: Part) {
        context.gameField.bord[cell.y!!][cell.x!!].userId = null
        context.gameField.bord[cell.y!!][cell.x!!].color = null
    }

    private fun putUserFigure(context: Context, cell: Part, user: User) {
        if (cell.x!! >= 0 && cell.y!! >= 0) {
            context.gameField.bord[cell.y!!][cell.x!!].userId = user.id
            context.gameField.bord[cell.y!!][cell.x!!].color = user.figure[3].color
        }
    }

    private fun clearUserFigure(user: User,
                                field: GameField,
                                event: Event,
                                cellsBelongToUser: Boolean,
                                cellsIsEmpty: Boolean,
                                doUserHaveAbilityOfMoving: Boolean) {

        var isItEndFreeGameSpace = !cellsBelongToUser && !cellsIsEmpty
        var isItFreeGameSpace = isItEndFreeGameSpace || !doUserHaveAbilityOfMoving
        if (Direction.SOUTH == event.direction && isItFreeGameSpace) {
            user.figure.forEach {
                if (it.y!! >= 0) {
                    field.bord[it.y!!][it.x!!].userId = null
                }
            }
            user.figure.clear()
        }
    }

    private fun initUser(user: User, context: Context): Boolean {
        if (user.figure.isEmpty()) {
            user.figure.addAll(Figures.values()[Random.nextInt(0, Figures.values().size)].form!!.stream().map { it.copy() }
                    .collect(Collectors.toList()))
            user.figure.stream()
                    .forEach {
                        it.x = it.x?.plus(context.users.indexOf(user) * 3 + 2)
                        it.color = Colors.values()[Random.nextInt(0, Colors.values().size)].name
                    }
            return true
        }
        return false
    }

    fun haveAbilityOfMoving(user: User, field: GameField, event: Event): Boolean {
        return !user.figure.stream()
                .filter { it.y!! >= 0 }
                .filter {
                    it.x?.plus(event.direction.deltaX)!! > field.length
                            || it.y?.plus(event.direction.deltaY)!! > field.width
                            || it.x?.plus(event.direction.deltaX)!! < 0
                            || it.y?.plus(event.direction.deltaY)!! < 0
                }
                .findFirst()
                .isPresent
    }

    fun cellsBelongToUser(user: User, field: GameField, event: Event): Boolean {
        return user.figure.stream()
                .filter { it.y!! >= 0 }
                .filter {
                    field.bord[it.y?.plus(event.direction.deltaY)!!][it.x?.plus(event.direction.deltaX)!!]
                            .userId != null
                            && user.id != field
                            .bord[it.y?.plus(event.direction.deltaY)!!][it.x?.plus(event.direction.deltaX)!!]
                            .userId
                }
                .findFirst()
                .isPresent
    }

    fun cellsIsNotEmpty(user: User, field: GameField, event: Event): Boolean {
        return user.figure.stream()
                .filter { it.y!! >= 0 }
                .filter {
                    field
                            .bord[it.y?.plus(event.direction.deltaY)!!][it.x?.plus(event.direction.deltaX)!!]
                            .color != null && user.id != field
                            .bord[it.y?.plus(event.direction.deltaY)!!][it.x?.plus(event.direction.deltaX)!!]
                            .userId
                }
                .findFirst()
                .isPresent
    }
}
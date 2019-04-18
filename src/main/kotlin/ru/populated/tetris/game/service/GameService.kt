package ru.populated.tetris.game.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.model.Event
import ru.populated.tetris.game.model.Figure
import ru.populated.tetris.game.model.GameField
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.stream.Collectors

@Service
class GameService {
    private val queue: Queue<Event> = ConcurrentLinkedQueue()

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var contextService: ContextService

    fun addEvent(event: Event) {
        queue.add(event)
    }

    fun nextEvent(): Event {
        return queue.poll()
    }

    fun play(): GameField {
        val event = nextEvent()
        val context = contextService.getContextById(event.contextId)
        val user = userService.findUserById(event.userId)

        var oldBord: MutableList<MutableList<Int>>? = context?.gameField?.bord
                ?.stream()
                ?.map { it.toMutableList() }
                ?.collect(Collectors.toList())

        context?.let { ctx -> user?.figure?.let { figure -> remove(ctx, figure) } }

        if (context?.let { ctx -> user?.figure?.let { figure -> render(ctx, figure, event) } }!!) {
            return context.gameField
        }

        context.gameField.bord = oldBord!!

        return context.gameField

    }

    fun remove(context: Context, figure: Figure) {
        figure.form.stream()
                .forEach {
                    val gameFiled = context.gameField
                    gameFiled.bord
                            .get(gameFiled.width + figure.y + it.deltaY)
                            .set(gameFiled.length + figure.x + it.deltaX, 0)
                }
    }

    fun render(context: Context, figure: Figure, event: Event): Boolean {

        val result: Boolean = !figure.form.stream()
                .map {
                    val gameFiled = context.gameField
                    val cell: Int = gameFiled.bord
                            .get(gameFiled.width + figure.y + event.direction.deltaY + it.deltaY)
                            .get(gameFiled.length + figure.x + event.direction.deltaX + it.deltaX)
                    if (cell == 0) {
                        gameFiled.bord
                                .get(gameFiled.width + figure.y + event.direction.deltaY + it.deltaY)
                                .set(gameFiled.width + figure.x + event.direction.deltaX + it.deltaX, 1)
                    } else return@map 3
                }
                .anyMatch { it == 3 }

        if (result) {
            figure.x = figure.x + event.direction.deltaX
            figure.y = figure.y + event.direction.deltaY
        }

        return result
    }
}
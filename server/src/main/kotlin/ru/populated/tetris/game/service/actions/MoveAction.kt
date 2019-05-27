package ru.populated.tetris.game.service.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.*
import ru.populated.tetris.game.service.conditions.Condition
import java.util.stream.Collectors
import kotlin.random.Random

@Component
class MoveAction : Action {

    @Autowired
    lateinit var endFreeGameSpace: Condition


    @Autowired
    lateinit var notFreeSpace: Condition


    @Autowired
    lateinit var gameSpace: Condition

    override fun doAction(user: User, context: Context, event: Event) {

        if (Direction.SOUTH != event.direction
                && Direction.EAST != event.direction
                && Direction.WEST != event.direction
                && Direction.NORTH != event.direction) {
            return
        }

        if (gameSpace.check(user, context, event)) {
            val isFreeSpace = notFreeSpace.check(user, context, event)
            val isEndFreeGameSpace = endFreeGameSpace.check(user, context, event)

            if (!isFreeSpace && !isEndFreeGameSpace) {
                move(user, context, event)
            }
        }
    }

    private fun move(user: User, context: Context, event: Event) {
        if (!initUser(user, context)) {
            deleteUserFigure(user.figure, context)

            putUserFigure(context, user, event)
        } else {
            user.figure.form.forEach {
                if (it.y >= 0 && it.x >= 0) {
                    context.gameField.board[it.y][it.x].userId = user.id
                    context.gameField.board[it.y][it.x].color = user.color!!.name
                }
            }
        }
        user.baseX = user.baseX!!.plus(event.direction.deltaX)
        user.baseY = user.baseY!!.plus(event.direction.deltaY)
    }

    protected fun initUser(user: User, context: Context): Boolean {
        if (user.figure.form.isEmpty()) {
            user.figure.figureNumber = Random.nextInt(0, Figures.values().size)
            takeNewFigureForUser(user)
            user.figure.form.stream()
                    .forEach {
                        it.x = it.x.plus(context.users.indexOf(user) * 3 + 2)
                    }
            user.color = Colors.values()[Random.nextInt(0, Colors.values().size)]
            initBaseCoordinates(user)
            return true
        }
        return false
    }

    private fun initBaseCoordinates(user: User) {
        user.figure.form.sortWith(compareBy({ it.x }, { it.y }))

        user.baseX = user.figure.form[0].x
        user.baseY = user.figure.form[0].y
    }

    private fun takeNewFigureForUser(user: User) {
        user.figure.form.addAll(Figures.values()[user.figure.figureNumber].form?.get(user.figure.position)!!
                .stream()
                .map { it.copy() }
                .peek {
                    if (it.base) {
                        user.baseX = it.x
                        user.baseY = it.y
                    }
                }
                .collect(Collectors.toList())
        )
    }

    protected fun deleteUserFigure(figure: Figure, context: Context) {
        figure.form.forEach {
            if (it.y >= 0 && it.x >= 0 && it.y <= context.gameField.width && it.x <= context.gameField.length) {
                context.gameField.board[it.y][it.x].userId = null
                context.gameField.board[it.y][it.x].color = null
            }
        }

    }

    protected fun putUserFigure(context: Context, user: User, event: Event) {
        user.figure.form.forEach {

            if (it.x.plus(event.direction.deltaX) <= context.gameField.length
                    && it.y.plus(event.direction.deltaY) <= context.gameField.width
                    && it.x.plus(event.direction.deltaX) >= 0) {
                it.x = it.x.plus(event.direction.deltaX)
                it.y = it.y.plus(event.direction.deltaY)
            }

            if (it.x >= 0 && it.y >= 0
                    && it.y <= context.gameField.width
                    && it.x <= context.gameField.length) {
                context.gameField.board[it.y][it.x].userId = user.id
                context.gameField.board[it.y][it.x].color = user.color!!.name
            }
        }
    }
}
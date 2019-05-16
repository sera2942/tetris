package ru.populated.tetris.game.service.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.*
import ru.populated.tetris.game.service.conditions.Condition
import ru.populated.tetris.game.service.conditions.UserSpaceCondition

@Component
class RemoveFullLineAction : Action {
    @Autowired
    lateinit var endFreeGameSpace: Condition


    @Autowired
    lateinit var notFreeSpace: Condition


    @Autowired
    lateinit var gameSpace: Condition

    @Autowired
    lateinit var userSpace: UserSpaceCondition

    override fun doAction(user: User, context: Context, event: Event) {

        if (Direction.SOUTH != event.direction
                && Direction.EAST != event.direction
                && Direction.WEST != event.direction
                && Direction.NORTH != event.direction) {
            return
        }


        val isItEndFreeGameSpace = !notFreeSpace.check(user, context, event) && endFreeGameSpace.check(user, context, event)
        var isItFreeGameSpace = isItEndFreeGameSpace || !gameSpace.check(user, context, event)


        if (Direction.SOUTH == event.direction && isItFreeGameSpace && !userSpace.check(user, context, event)) {

            takeFigureOfUser(user, context)
            removeFullLine(context.gameField)
        }
    }


    private fun takeFigureOfUser(user: User, context: Context) {
        user.figure.form.forEach {
            if (it.y >= 0 && it.x >= 0) {
                context.gameField.bord[it.y][it.x].userId = null
            }
        }

        user.baseX = null
        user.baseY = null
        user.figure.position = 0
        user.figure.form.clear()
    }

    private fun removeFullLine(gameField: GameField) {

        val bord: MutableList<MutableList<Cell>> = gameField.bord
        for (y in 1..gameField.width) {

            var doOverwriteLine = true
            for (x in 0..gameField.length) {
                if (bord[y][x].userId == null && bord[y][x].color == null) {
                    doOverwriteLine = false
                    break
                }
            }

            if (doOverwriteLine) {
                for (innerY in y downTo 1) {
                    for (x in 0..gameField.length) {
                        if (bord[innerY - 1][x].userId == null && bord[innerY][x].userId == null) {
                            bord[innerY][x].color = bord[innerY - 1][x].color
                            bord[innerY - 1][x] = Cell()
                        }
                    }
                }
            }
        }
    }
}
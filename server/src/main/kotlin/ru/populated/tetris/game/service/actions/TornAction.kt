package ru.populated.tetris.game.service.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.*
import ru.populated.tetris.game.service.conditions.UserSpaceCondition
import java.util.stream.Collectors

@Component
class TornAction : MoveAction() {

    @Autowired
    lateinit var userSpace: UserSpaceCondition

    override fun doAction(user: User, context: Context, event: Event) {
        if (event.direction == Direction.TURN) {

            var oldFigure = user.figure
            user.figure = getNewFigure(user)

            if (gameSpace.check(user, context, event)) {
                val isFreeSpace = notFreeSpace.check(user, context, event)
                val isEndFreeGameSpace = endFreeGameSpace.check(user, context, event)

                if (!isFreeSpace && !isEndFreeGameSpace && !userSpace.check(user, context, event)) {
                    turn(user, context, event, oldFigure)
                    resetFigurePosition(user)
                }
                return
            }
            user.figure = oldFigure

        }
    }

    private fun resetFigurePosition(user: User) {
        if (user.figure.position.plus(1) >= Figures.values()[user.figure.figureNumber].form!!.size) {
            user.figure.position = -1
        }
    }

    private fun turn(user: User, context: Context, event: Event, figure: Figure) {
        deleteUserFigure(figure, context)
        user.figure.position = user.figure.position.plus(1)
        putUserFigure(context, user, event)
    }

    private fun getNewFigure(user: User): Figure {

        resetFigurePosition(user)

        var index = user.figure.position

        return Figure(user.figure.figureNumber,
                user.figure.position,
                Figures.values()[user.figure.figureNumber]
                        .form?.get(index + 1)!!.stream()
                        .map { it.copy() }
                        .peek {
                            it.x = user.baseX?.let { it1 -> it.x.plus(it1) }!!
                            it.y = user.baseY?.let { it1 -> it.y.plus(it1) }!!
                        }.collect(Collectors.toList()))
    }
}
package ru.populated.tetris.game.service.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.*
import ru.populated.tetris.game.service.conditions.GameStatAggregator
import ru.populated.tetris.game.web.model.ActionType
import ru.populated.tetris.game.web.model.Event
import ru.populated.tetris.game.web.model.StateSign
import java.util.stream.Collectors

@Component
class TornAction : MoveAction() {

    @Autowired
    lateinit var gameStatAggregator: GameStatAggregator

    override fun doAction(user: User, context: Context, event: Event) {
        if (event.actionType == ActionType.TURN) {

            val oldFigure = user.figure
            user.figure = getNewFigure(user)
            gameStatAggregator.aggregate(user, context, event)

            if (user.stateActionUser == StateSign.TO_MOVE) {
                turn(user, context, event, oldFigure)
                resetFigurePosition(user)
            } else {
                user.figure = oldFigure
            }
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

        val index = user.figure.position

        return Figure(user.figure.figureNumber,
                user.figure.position,
                Figures.values()[user.figure.figureNumber]
                        .form?.get(index + 1)!!.stream()
                        .map { it.copy() }
                        .peek {
                            it.x = user.deltaX?.let { it1 -> it.x.plus(it1) }!!
                            it.y = user.deltaY?.let { it1 -> it.y.plus(it1) }!!
                        }.collect(Collectors.toList()))
    }
}
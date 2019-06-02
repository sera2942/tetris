package ru.populated.tetris.game.service.conditions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.*
import ru.populated.tetris.game.model.Cell
import ru.populated.tetris.game.web.model.Event
import ru.populated.tetris.game.web.model.StateSign

@Component
class MoveStateAggregator : Aggregator {

    @Autowired
    private lateinit var renderStatusAggregator: RenderStatusAggregator

    override fun aggregate(user: User, context: Context, event: Event) {

        renderStatusAggregator.aggregate(user, context, event)

        val bord: MutableList<MutableList<Cell>> = context.gameField.board

        val isNotFreeGameSpace: Boolean = user.figure.form.stream()
                .filter { it.render }
                .filter {
                    val coordinateX = it.x.plus(event.actionType.deltaX)
                    val coordinateY = it.y.plus(event.actionType.deltaY)
                    coordinateX > context.gameField.length
                            || coordinateX < 0
                            || coordinateY > context.gameField.width
                            || (bord[coordinateY][coordinateX].color != null && user.id != bord[coordinateY][coordinateX].userId)
                }
                .findFirst()
                .isPresent

        if (isNotFreeGameSpace) {
            user.stateActionUser = StateSign.NONE
        } else {
            user.stateActionUser = StateSign.TO_MOVE
        }
    }
}
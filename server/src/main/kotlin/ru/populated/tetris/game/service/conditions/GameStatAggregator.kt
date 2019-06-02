package ru.populated.tetris.game.service.conditions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.web.model.Event
import ru.populated.tetris.game.web.model.StateSign
import ru.populated.tetris.game.model.User

@Component
class GameStatAggregator : Aggregator {

    @Autowired
    private lateinit var moveStateAggregator: MoveStateAggregator

    override fun aggregate(user: User, context: Context, event: Event) {

        moveStateAggregator.aggregate(user, context, event)

        user.figure.form.stream()
                .filter { StateSign.NONE == user.stateActionUser }
                .filter { it.render }
                .filter {
                    it.x.plus(event.actionType.deltaX) >= 0
                            && it.x.plus(event.actionType.deltaX) <= context.gameField.length
                }
                .filter {
                    val coordinateX = it.x.plus(event.actionType.deltaX)
                    val coordinateY = it.y.plus(event.actionType.deltaY)
                    coordinateY > context.gameField.width
                            || (context.gameField.board[coordinateY][coordinateX].color != null
                                        && context.gameField.board[coordinateY][coordinateX].userId == null)
                }
                .findFirst()
                .ifPresent { user.stateActionUser = StateSign.ARCHIVE_END_OF_GAME_SPACE }
    }
}
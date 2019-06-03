package ru.populated.tetris.game.service.actions

import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.Cell
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.model.Figure
import ru.populated.tetris.game.model.User
import ru.populated.tetris.game.web.model.ActionType
import ru.populated.tetris.game.web.model.Event
import ru.populated.tetris.game.web.model.StateSign

@Component
class MoveAction : Action {

    private val actionType: MutableList<ActionType> = mutableListOf()

    init {
        actionType.add(ActionType.EAST)
        actionType.add(ActionType.NORTH)
        actionType.add(ActionType.SOUTH)
        actionType.add(ActionType.WEST)
    }

    override fun doAction(user: User, context: Context, event: Event) {

        if (actionType.contains(event.actionType) && user.stateActionUser == StateSign.TO_MOVE) {
            move(user, context, event)
        }
    }

    private fun move(user: User, context: Context, event: Event) {

        erase(user.figure, context.gameField.board)

        render(context, user, event)

        user.deltaX = user.deltaX!!.plus(event.actionType.deltaX)
        user.deltaY = user.deltaY!!.plus(event.actionType.deltaY)
    }

    protected fun erase(figure: Figure, board: MutableList<MutableList<Cell>>) {
        figure.form
                .stream()
                .filter { it.previousRenderState }
                .forEach {
                    if (it.previousRenderState) {
                        board[it.y][it.x].userId = null
                        board[it.y][it.x].color = null
                        board[it.y][it.x].base = false
                    }
                }
    }

    protected fun render(context: Context, user: User, event: Event) {
        user.figure.form.forEach {

            if (user.stateActionUser == StateSign.TO_MOVE) {
                it.x = it.x.plus(event.actionType.deltaX)
                it.y = it.y.plus(event.actionType.deltaY)
            }

            if (it.render) {
                context.gameField.board[it.y][it.x].userId = user.id
                context.gameField.board[it.y][it.x].color = user.color!!.name
            }
        }
    }
}
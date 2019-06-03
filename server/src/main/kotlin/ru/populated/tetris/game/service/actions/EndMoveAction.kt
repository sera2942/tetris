package ru.populated.tetris.game.service.actions

import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.*
import ru.populated.tetris.game.web.model.ActionType
import ru.populated.tetris.game.web.model.Event
import ru.populated.tetris.game.web.model.StateSign

@Component
class EndMoveAction : Action {


    override fun doAction(user: User, context: Context, event: Event) {
        if (user.stateActionUser == StateSign.ARCHIVE_END_OF_GAME_SPACE && event.actionType == ActionType.SOUTH) {
            endMove(user, context)
            removeFullLine(context.gameField)
        }
    }

    private fun endMove(user: User, context: Context) {
        user.figure
                .form
                .stream()
                .filter { it.y == 0 }
                .findAny()
                .ifPresent { context.typeState = TypeState.GAME_OVER }

        user.figure.form
                .stream()
                .filter { context.typeState != TypeState.GAME_OVER }
                .filter { it.render }
                .forEach {
                    context.gameField.board[it.y][it.x].userId = null
                }

        user.deltaX = null
        user.deltaY = null
        user.figure.position = 0
        user.figure.form.clear()
    }

    private fun removeFullLine(gameField: GameField) {

        val board: MutableList<MutableList<Cell>> = gameField.board
        for (y in 1..gameField.width) {

            var isOverwriteLine = true
            for (x in 0..gameField.length) {
                if (board[y][x].userId == null && board[y][x].color == null) {
                    isOverwriteLine = false
                    break
                }
            }

            if (isOverwriteLine) {
                eraseFullLine(y, gameField, board)
            }
        }
    }

    private fun eraseFullLine(y: Int, gameField: GameField, board: MutableList<MutableList<Cell>>) {
        for (innerY in y downTo 1) {
            for (x in 0..gameField.length) {
                if (board[innerY - 1][x].userId == null && board[innerY][x].userId == null) {
                    board[innerY][x].color = board[innerY - 1][x].color
                    board[innerY - 1][x] = Cell()
                }
            }
        }
    }
}
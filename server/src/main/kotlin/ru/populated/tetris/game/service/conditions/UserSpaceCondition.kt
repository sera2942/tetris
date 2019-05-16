package ru.populated.tetris.game.service.conditions

import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.Cell
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.model.Event
import ru.populated.tetris.game.model.User

@Component
class UserSpaceCondition : Condition {
    override fun check(user: User, context: Context, event: Event): Boolean {
        var bord: MutableList<MutableList<Cell>> = context.gameField.bord

        return user.figure.form.stream()
                .filter { it.y >= 0 && it.x >= 0 }
                .filter { it.x.plus(event.direction.deltaX) >= 0 }
                .filter { it.x.plus(event.direction.deltaX) <= context.gameField.length && it.y.plus(event.direction.deltaY) <= context.gameField.width }
                .filter {
                    val coordinateX = it.x.plus(event.direction.deltaX)
                    val coordinateY = it.y.plus(event.direction.deltaY)
                    bord[coordinateY][coordinateX].userId != null && user.id != bord[coordinateY][coordinateX].userId
                }
                .findFirst()
                .isPresent
    }
}
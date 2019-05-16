package ru.populated.tetris.game.service.conditions

import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.model.Event
import ru.populated.tetris.game.model.User

@Component("notFreeSpace")
class NotFreeSpaceCondition : Condition {
    override fun check(user: User, context: Context, event: Event): Boolean {
        return user.figure.form.stream()
                .filter { it.y >= 0 && it.x >= 0 }
                .filter { it.y <= 0 && it.x <= 0 }
                .filter { it.x.plus(event.direction.deltaX) >= 0 && it.y.plus(event.direction.deltaY) >= 0 }
                .filter { it.x.plus(event.direction.deltaX) <= 0 && it.y.plus(event.direction.deltaY) <= 0 }
                .filter {
                    val coordinateX = it.x.plus(event.direction.deltaX)
                    val coordinateY = it.y.plus(event.direction.deltaY)
                    context.gameField.bord[coordinateY][coordinateX].userId != null
                            && user.id != context.gameField.bord[coordinateY][coordinateX].userId
                }
                .findFirst()
                .isPresent
    }
}
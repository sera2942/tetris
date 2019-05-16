package ru.populated.tetris.game.service.conditions

import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.model.Event
import ru.populated.tetris.game.model.User

@Component("gameSpace")
class GameSpaceCondition : Condition {

    override fun check(user: User, context: Context, event: Event): Boolean {
        return !user.figure.form.stream()
                .filter { it.y >= 0 && it.x >= 0 }
                .filter {
                    val coordinateX = it.x.plus(event.direction.deltaX)
                    val coordinateY = it.y.plus(event.direction.deltaY)
                    coordinateX > context.gameField.length
                            || coordinateY > context.gameField.width
                            || coordinateX < 0
                            || coordinateY < 0
                }
                .findFirst()
                .isPresent
    }

}
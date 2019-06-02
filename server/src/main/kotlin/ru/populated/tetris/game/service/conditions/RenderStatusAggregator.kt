package ru.populated.tetris.game.service.conditions

import org.springframework.stereotype.Component
import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.web.model.Event
import ru.populated.tetris.game.model.User

@Component
class RenderStatusAggregator : Aggregator {

    override fun aggregate(user: User, context: Context, event: Event) {
        user.figure.form.stream()
                .peek {
                    it.previousRenderState = it.render
                    it.render = false
                }
                .filter {
                    it.y.plus(event.actionType.deltaY) >= 0
                }
                .forEach {
                    it.render = true
                }
    }

}
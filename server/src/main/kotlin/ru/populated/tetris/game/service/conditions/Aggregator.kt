package ru.populated.tetris.game.service.conditions

import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.web.model.Event
import ru.populated.tetris.game.model.User

interface Aggregator {
    fun aggregate(user: User, context: Context, event: Event)
}
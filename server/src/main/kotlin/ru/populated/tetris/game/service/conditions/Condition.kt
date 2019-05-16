package ru.populated.tetris.game.service.conditions

import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.model.Event
import ru.populated.tetris.game.model.User

interface Condition {
    fun check(user: User, context: Context, event: Event): Boolean
}
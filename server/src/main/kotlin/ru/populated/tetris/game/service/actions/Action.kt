package ru.populated.tetris.game.service.actions

import ru.populated.tetris.game.model.Context
import ru.populated.tetris.game.web.model.Event
import ru.populated.tetris.game.model.User

interface Action {
    fun doAction(user: User, context: Context, event: Event)
}
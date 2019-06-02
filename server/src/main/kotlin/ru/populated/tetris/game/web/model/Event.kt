package ru.populated.tetris.game.web.model

import java.util.*

data class Event(val contextId: UUID, val userId: UUID, val actionType: ActionType)
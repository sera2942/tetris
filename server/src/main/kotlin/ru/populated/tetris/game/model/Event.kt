package ru.populated.tetris.game.model

import java.util.*

data class Event(val contextId: UUID, val userId: UUID, val direction: Direction)
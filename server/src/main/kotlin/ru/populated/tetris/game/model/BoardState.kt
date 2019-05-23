package ru.populated.tetris.game.model

import java.util.*

data class BoardState(var contextId: UUID, var typeState: TypeState, var board: MutableList<MutableList<Cell>>)
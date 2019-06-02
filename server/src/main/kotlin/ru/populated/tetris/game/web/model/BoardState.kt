package ru.populated.tetris.game.web.model

import ru.populated.tetris.game.model.Cell
import ru.populated.tetris.game.model.TypeState
import java.util.*

data class BoardState(var contextId: UUID, var typeState: TypeState, var board: MutableList<MutableList<Cell>>)
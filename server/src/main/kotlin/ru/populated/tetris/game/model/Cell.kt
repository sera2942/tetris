package ru.populated.tetris.game.model

import java.util.*

class Cell {
    var color: String? = null
    var userId: UUID? = null
    override fun toString(): String {
        return "{color:\"$color\", userId: \"$userId\"}"
    }
}
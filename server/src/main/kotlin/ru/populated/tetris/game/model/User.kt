package ru.populated.tetris.game.model

import java.io.Serializable
import java.util.*

class User : Serializable {
    val id: UUID = UUID.randomUUID()
    var figure: MutableList<Part> = mutableListOf()
    var name: String? = null
}
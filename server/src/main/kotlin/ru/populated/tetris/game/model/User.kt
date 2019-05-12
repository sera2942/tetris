package ru.populated.tetris.game.model

import java.io.Serializable
import java.util.*

class User : Serializable {
    val id: UUID = UUID.randomUUID()
    var figure: Figure = Figure(0, 0, mutableListOf())
    var name: String? = null
    var color: Colors? = null
    var baseX: Int? = null
    var baseY: Int? = null
}
package ru.populated.tetris.game.model

import java.io.Serializable
import java.util.*

class User : Serializable {
    val id: UUID = UUID.randomUUID()
    var figure: Figure = Figure()
    var name: String
        get() = this.name
        set(value) {
            this.name = value
        }
}
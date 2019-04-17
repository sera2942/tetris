package ru.populated.tetris.game.model

import java.util.*
import kotlin.collections.ArrayList

class Context {

    val id: UUID = UUID.randomUUID()

    var users: MutableList<User> = ArrayList()

    var gameField: GemeField = GemeField(320, 128)
}

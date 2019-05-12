package ru.populated.tetris.game.model

class Part(x: Int, y: Int) {
    var x: Int? = x
    var y: Int? = y

    fun pluseOneToX() {
        x = x?.plus(1)
    }

    fun pluseOneToY() {
        y = y?.plus(1)
    }

    fun copy(): Part {
        return Part(x!!, y!!)
    }
}
package ru.populated.tetris.game.model

class Part(var x: Int,
           var y: Int,
           var base: Boolean,
           var render: Boolean = false,
           var previousRenderState: Boolean = false) {

    fun pluseOneToX() {
        x = x.plus(1)
    }

    fun pluseOneToY() {
        y = y.plus(1)
    }

    fun copy(): Part {
        return Part(x, y, base)
    }
}
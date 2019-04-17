package ru.populated.tetris.game.model

class GemeField(width: Int, length: Int) {

    private var width: Int
        get() = this.width
        set(value) {
            this.width = value
        }

    private var length: Int
        get() = this.length
        set(value) {
            this.length = value
        }

    init {
        this.width = width
        this.length = length
    }
}
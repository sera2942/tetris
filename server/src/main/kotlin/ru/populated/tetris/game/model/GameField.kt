package ru.populated.tetris.game.model

class GameField(width: Int, length: Int) {

    var width: Int
        get() = this.width
        set(value) {
            this.width = value
        }

    var length: Int
        get() = this.length
        set(value) {
            this.length = value
        }

    var bord: MutableList<MutableList<Int>>
        get() = this.bord
        set(value) {
            this.bord = value
        }

    init {
        this.width = width
        this.length = length
    }
}
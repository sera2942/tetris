package ru.populated.tetris.game.model

class GameField(width: Int, length: Int) {

    var width: Int = width

    var length: Int = length

    var bord: MutableList<MutableList<Cell>> = mutableListOf()

    init {
        for (y in 0..width) {
            bord.add(mutableListOf())
            for (x in 0..length)
                bord[y].add(Cell())
        }
    }

    fun expendGameField(x: Int, y: Int) {
        var lastLength = length
        length = length.plus(x)
        width = width.plus(y)

        for (innerY in 0..width) {
            for (innerX in lastLength until length)
                bord[innerY].add(Cell())
        }

    }
}
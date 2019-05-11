package ru.populated.tetris.game.model

class GameField(width: Int, length: Int) {

    var width: Int = width

    var length: Int = length

    var bord: MutableList<MutableList<Cell>> = mutableListOf()

    init {
        for (i in 0..width) {
            bord.add(mutableListOf())
            for (j in 0..length)
                bord[i].add(Cell())
        }
    }

    fun expendGameField(x: Int, y: Int) {
        var lastLength = length
        length = length.plus(x)
        width = width.plus(y)

        for (i in 0..width) {
            for (j in lastLength until length)
                bord[i].add(Cell())
        }

    }
}
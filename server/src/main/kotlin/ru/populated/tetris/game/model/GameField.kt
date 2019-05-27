package ru.populated.tetris.game.model

class GameField(var width: Int, var length: Int) {

    var board: MutableList<MutableList<Cell>> = mutableListOf()

    init {
        for (y in 0..width) {
            board.add(mutableListOf())
            for (x in 0..length)
                board[y].add(Cell())
        }
    }

    fun expendGameField(x: Int, y: Int) {
        val lastLength = length
        length = length.plus(x)
        width = width.plus(y)

        for (innerY in 0..width) {
            for (innerX in lastLength until length)
                board[innerY].add(Cell())
        }

    }
}
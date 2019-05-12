package ru.populated.tetris.game.model

data class Figure(var figureNumber: Int, var position: Int, var form: MutableList<Part>)
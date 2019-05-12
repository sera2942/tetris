package ru.populated.tetris.game.model

enum class Direction(val deltaX: Int, val deltaY: Int) {
    NORTH(0, 0), SOUTH(0, 1), WEST(1, 0), EAST(-1, 0), TURN(0, 0)
}
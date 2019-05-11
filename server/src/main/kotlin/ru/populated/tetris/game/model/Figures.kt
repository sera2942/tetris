package ru.populated.tetris.game.model

import java.util.*


enum class Figures(form: List<Part>) {
    SHELF(Arrays.asList(Part(0, -3), Part(0, -2), Part(0, -1), Part(0, 0))),
    ZIGZUG(Arrays.asList(Part(0, -2), Part(0, -1), Part(1, -1), Part(1, 0))),
    SQUARE(Arrays.asList(Part(0, -1), Part(1, -1), Part(0, 0), Part(1, 0))),
    LADDER(Arrays.asList(Part(0, -2), Part(1, -1), Part(0, -1), Part(0, 0)));

    var form: List<Part>? = form
}
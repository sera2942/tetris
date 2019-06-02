package ru.populated.tetris.game.model

import java.util.*

enum class Figures(form: List<List<Part>>) {
    SHELF(
            Arrays.asList(
                    Arrays.asList(Part(0, -3, false), Part(0, -2, false), Part(0, -1, false), Part(0, 0, true)),
                    Arrays.asList(Part(3, 0, false), Part(2, 0, false), Part(1, 0, false), Part(0, 0, true))
            )

    ),
    ZIGZUG(
            Arrays.asList(
                    Arrays.asList(Part(0, -2, false), Part(0, -1, true), Part(1, -1, false), Part(1, 0, false)),
                    Arrays.asList(Part(0, -1, true), Part(-1, -2, false), Part(0, -2, false), Part(1, -1, false))
            )
    ),
    SQUARE(Arrays.asList(Arrays.asList(Part(0, -1, true), Part(1, -1, false), Part(0, 0, false), Part(1, 0, false)))),
    LADDER(
            Arrays.asList(
                    Arrays.asList(Part(1, 0, false), Part(1, -1, true), Part(1, -2, false), Part(0, -1, false)),
                    Arrays.asList(Part(1, 0, false), Part(2, -1, false), Part(1, -1, true), Part(0, -1, false)),
                    Arrays.asList(Part(1, -2, false), Part(1, -1, true), Part(1, 0, false), Part(2, -1, false)),
                    Arrays.asList(Part(0, -1, false), Part(1, -1, false), Part(2, -1, false), Part(1, -2, true))
            )
    );

    var form: List<List<Part>>? = form
}
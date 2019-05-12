package ru.populated.tetris.game.model

import java.util.*

enum class Figures(form: List<List<Part>>) {
    SHELF(
            Arrays.asList(
                    Arrays.asList(Part(0, -3), Part(0, -2), Part(0, -1), Part(0, 0)),
                    Arrays.asList(Part(3, 0), Part(2, 0), Part(1, 0), Part(0, 0))
            )

    ),
    ZIGZUG(
            Arrays.asList(
                    Arrays.asList(Part(0, -2), Part(0, -1), Part(1, -1), Part(1, 0)),
                    Arrays.asList(Part(0, -1), Part(1, -1), Part(1, 0), Part(2, 0))
            )
    ),
    SQUARE(Arrays.asList(Arrays.asList(Part(0, -1), Part(1, -1), Part(0, 0), Part(1, 0)))),
    LADDER(
            Arrays.asList(
                    Arrays.asList(Part(0, -2), Part(1, -1), Part(0, -1), Part(0, 0)),
                    Arrays.asList(Part(1, 0), Part(2, -1), Part(1, -1), Part(0, -1)),
                    Arrays.asList(Part(0, -1), Part(1, -1), Part(1, -2), Part(1, 0)),
                    Arrays.asList(Part(0, 0), Part(1, 0), Part(2, 0), Part(1, -1))
            )
    );

    var form: List<List<Part>>? = form
}
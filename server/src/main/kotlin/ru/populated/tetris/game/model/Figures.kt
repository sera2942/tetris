package ru.populated.tetris.game.model

import java.util.*

enum class Figures(form: List<List<Part>>) {
    SHELF(
            Arrays.asList(
                    Arrays.asList(Part(0, -4), Part(0, -3), Part(0, -2), Part(0, -1)),
                    Arrays.asList(Part(3, -1), Part(2, -1), Part(1, -1), Part(0, -1))
            )

    ),
    ZIGZUG(
            Arrays.asList(
                    Arrays.asList(Part(0, -1), Part(0, -2), Part(1, -2), Part(1, -3)),
                    Arrays.asList(Part(0, -2), Part(1, -2), Part(1, -1), Part(2, -1))
            )
    ),
    SQUARE(Arrays.asList(Arrays.asList(Part(0, -2), Part(1, -2), Part(0, -1), Part(1, -1)))),
    LADDER(
            Arrays.asList(
                    Arrays.asList(Part(1, -1), Part(1, -2), Part(1, -3), Part(0, -2)),
                    Arrays.asList(Part(1, -1), Part(2, -2), Part(1, -2), Part(0, -2)),
                    Arrays.asList(Part(1, -3), Part(1, -2), Part(1, -1), Part(2, -2)),
                    Arrays.asList(Part(0, -2), Part(1, -2), Part(2, -2), Part(1, -3))
            )
    );

    var form: List<List<Part>>? = form
}
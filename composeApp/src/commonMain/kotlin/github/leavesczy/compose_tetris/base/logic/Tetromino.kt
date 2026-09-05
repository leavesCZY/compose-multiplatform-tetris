package github.leavesczy.compose_tetris.base.logic

import androidx.compose.runtime.Stable
import kotlin.random.Random

@Stable
enum class PieceType {
    I, O, T, S, Z, J, L
}

@Stable
data class Tetromino(
    val type: PieceType,
    val rotations: List<List<Cell>>,
    val rotationIndex: Int,
    val offset: Cell
) {

    val cells: List<Cell>
        get() = rotations[rotationIndex]

    fun rotated(): Tetromino {
        if (rotations.size == 1) {
            return this
        }
        val nextIndex = (rotationIndex + 1) % rotations.size
        return copy(rotationIndex = nextIndex)
    }

    fun resetForHold(): Tetromino {
        return copy(
            rotationIndex = 0,
            offset = spawnOffset()
        )
    }

    companion object {

        private val shapes: Map<PieceType, List<List<Cell>>> = mapOf(
            PieceType.I to listOf(
                listOf(Cell(0, 3), Cell(1, 3), Cell(2, 3), Cell(3, 3)),
                listOf(Cell(1, 0), Cell(1, 1), Cell(1, 2), Cell(1, 3))
            ),
            PieceType.S to listOf(
                listOf(Cell(0, 3), Cell(1, 2), Cell(1, 3), Cell(2, 2)),
                listOf(Cell(0, 1), Cell(0, 2), Cell(1, 2), Cell(1, 3))
            ),
            PieceType.Z to listOf(
                listOf(Cell(0, 2), Cell(1, 2), Cell(1, 3), Cell(2, 3)),
                listOf(Cell(0, 2), Cell(0, 3), Cell(1, 1), Cell(1, 2))
            ),
            PieceType.L to listOf(
                listOf(Cell(0, 1), Cell(0, 2), Cell(0, 3), Cell(1, 3)),
                listOf(Cell(0, 2), Cell(0, 3), Cell(1, 2), Cell(2, 2)),
                listOf(Cell(0, 1), Cell(1, 1), Cell(1, 2), Cell(1, 3)),
                listOf(Cell(0, 3), Cell(1, 3), Cell(2, 3), Cell(2, 2))
            ),
            PieceType.O to listOf(
                listOf(Cell(0, 2), Cell(0, 3), Cell(1, 2), Cell(1, 3))
            ),
            PieceType.J to listOf(
                listOf(Cell(0, 3), Cell(1, 1), Cell(1, 2), Cell(1, 3)),
                listOf(Cell(0, 2), Cell(0, 3), Cell(1, 3), Cell(2, 3)),
                listOf(Cell(0, 1), Cell(0, 2), Cell(0, 3), Cell(1, 1)),
                listOf(Cell(0, 2), Cell(1, 2), Cell(2, 2), Cell(2, 3))
            ),
            PieceType.T to listOf(
                listOf(Cell(0, 2), Cell(1, 2), Cell(2, 2), Cell(1, 3)),
                listOf(Cell(1, 1), Cell(0, 2), Cell(1, 2), Cell(1, 3)),
                listOf(Cell(1, 2), Cell(0, 3), Cell(1, 3), Cell(2, 3)),
                listOf(Cell(0, 1), Cell(0, 2), Cell(0, 3), Cell(1, 2))
            )
        )

        fun spawn(type: PieceType): Tetromino {
            val rotations = shapes.getValue(key = type)
            return Tetromino(
                type = type,
                rotations = rotations,
                rotationIndex = 0,
                offset = spawnOffset()
            )
        }

        fun spawnOffset(): Cell {
            return Cell(x = (BoardWidth - 4) / 2, y = -4)
        }

    }

}

internal fun createShuffledBag(): List<PieceType> {
    return PieceType.entries.toList().shuffled(random = Random.Default)
}

internal fun List<PieceType>.drawNext(): Pair<Tetromino, List<PieceType>> {
    var remaining = this
    if (remaining.isEmpty()) {
        remaining = createShuffledBag()
    }
    val type = remaining.first()
    return Tetromino.spawn(type = type) to remaining.drop(n = 1)
}
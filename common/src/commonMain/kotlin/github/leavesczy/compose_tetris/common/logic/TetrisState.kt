package github.leavesczy.compose_tetris.common.logic

import github.leavesczy.compose_tetris.platform.getScreenSize
import kotlin.random.Random

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 23:43
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */

private val BRICK_WIDTH = getScreenSize().x
private val BRICK_HEIGHT = getScreenSize().y

data class Location(val x: Int, val y: Int)

data class Tetris constructor(
    val shapes: List<List<Location>>, //此方块所有可能的旋转结果
    val type: Int, //用于标记当前处于哪种旋转状态
    val offset: Location, //方块相对屏幕左上角的偏移量
) {

    //此方块当前的形状
    val shape: List<Location>
        get() = shapes[type]

    companion object {

        private val allShapes = listOf(
            //I
            listOf(
                listOf(Location(0, 3), Location(1, 3), Location(2, 3), Location(3, 3)),
                listOf(Location(1, 0), Location(1, 1), Location(1, 2), Location(1, 3)),
            ),
            //S
            listOf(
                listOf(Location(0, 3), Location(1, 2), Location(1, 3), Location(2, 2)),
                listOf(Location(0, 1), Location(0, 2), Location(1, 2), Location(1, 3)),
            ),
            //Z
            listOf(
                listOf(Location(0, 2), Location(1, 2), Location(1, 3), Location(2, 3)),
                listOf(Location(0, 2), Location(0, 3), Location(1, 1), Location(1, 2)),
            ),
            //L
            listOf(
                listOf(Location(0, 1), Location(0, 2), Location(0, 3), Location(1, 3)),
                listOf(Location(0, 2), Location(0, 3), Location(1, 2), Location(2, 2)),
                listOf(Location(0, 1), Location(1, 1), Location(1, 2), Location(1, 3)),
                listOf(Location(0, 3), Location(1, 3), Location(2, 3), Location(2, 2)),
            ),
            //O
            listOf(
                listOf(Location(0, 2), Location(0, 3), Location(1, 2), Location(1, 3)),
            ),
            //J
            listOf(
                listOf(Location(0, 3), Location(1, 1), Location(1, 2), Location(1, 3)),
                listOf(Location(0, 2), Location(0, 3), Location(1, 3), Location(2, 3)),
                listOf(Location(0, 1), Location(0, 2), Location(0, 3), Location(1, 1)),
                listOf(Location(0, 2), Location(1, 2), Location(2, 2), Location(2, 3)),
            ),
            //T
            listOf(
                listOf(Location(0, 2), Location(1, 2), Location(2, 2), Location(1, 3)),
                listOf(Location(1, 1), Location(0, 2), Location(1, 2), Location(1, 3)),
                listOf(Location(1, 2), Location(0, 3), Location(1, 3), Location(2, 3)),
                listOf(Location(0, 1), Location(0, 2), Location(0, 3), Location(1, 2)),
            ),
        )

        operator fun invoke(): Tetris {
            val shapes = allShapes.random()
            val type = Random.nextInt(0, shapes.size)
            return Tetris(
                shapes = shapes,
                type = type,
                offset = Location(
                    Random.nextInt(
                        0,
                        BRICK_WIDTH - 3
                    ), -4
                )
            )
        }

    }

}

enum class GameStatus {
    Welcome,
    Running,
    Paused,
    LineClearing,
    ScreenClearing,
    GameOver
}

data class TetrisState(
    val brickArray: Array<IntArray>, //屏幕坐标系
    val tetris: Tetris, //下落的方块
    val gameStatus: GameStatus = GameStatus.Welcome, //游戏状态
    val soundEnable: Boolean = true, //是否开启音效
    val nextTetris: Tetris = Tetris(), //下一个方块
) {

    companion object {

        private val BrickArrayCache = Array(BRICK_HEIGHT) {
            IntArray(BRICK_WIDTH) {
                0
            }
        }

        private val ScreenMatrixCache = Array(BRICK_HEIGHT) {
            IntArray(BRICK_WIDTH) {
                0
            }
        }

        private fun Array<IntArray>.clearBrickArray() {
            forEach {
                for (index in it.indices) {
                    it[index] = 0
                }
            }
        }

        operator fun invoke(): TetrisState {
            BrickArrayCache.clearBrickArray()
            ScreenMatrixCache.clearBrickArray()
            return TetrisState(
                brickArray = BrickArrayCache,
                tetris = Tetris()
            )
        }

    }

    val width: Int
        get() = brickArray[0].size

    val height: Int
        get() = brickArray.size

    val isRunning: Boolean
        get() = gameStatus == GameStatus.Running

    val isPaused: Boolean
        get() = gameStatus == GameStatus.Paused

    val canStartGame: Boolean
        get() {
            return when (gameStatus) {
                GameStatus.Welcome, GameStatus.Paused, GameStatus.GameOver -> {
                    true
                }
                GameStatus.Running, GameStatus.LineClearing, GameStatus.ScreenClearing -> {
                    false
                }
            }
        }

    val screenMatrix: Array<IntArray>
        get() {
            ScreenMatrixCache.clearBrickArray()
            brickArray.forEachIndexed { y, ints ->
                ints.forEachIndexed { x, value ->
                    ScreenMatrixCache[y][x] = value
                }
            }
            for (location in tetris.shape) {
                val realX = location.x + tetris.offset.x
                if (realX < 0 || realX >= width) {
                    continue
                }
                val realY = location.y + tetris.offset.y
                if (realY < 0 || realY >= height) {
                    continue
                }
                ScreenMatrixCache[realY][realX] = 1
            }
            return ScreenMatrixCache
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TetrisState
        if (!brickArray.contentDeepEquals(other.brickArray)) return false
        if (tetris != other.tetris) return false
        if (gameStatus != other.gameStatus) return false
        if (soundEnable != other.soundEnable) return false
        if (nextTetris != other.nextTetris) return false
        return true
    }

    override fun hashCode(): Int {
        var result = brickArray.contentDeepHashCode()
        result = 31 * result + tetris.hashCode()
        result = 31 * result + gameStatus.hashCode()
        result = 31 * result + soundEnable.hashCode()
        result = 31 * result + nextTetris.hashCode()
        return result
    }

}

sealed class Action {
    object Welcome : Action()
    object Start : Action()
    object Pause : Action()
    object Reset : Action()
    object Sound : Action()
    object Background : Action()
    object Resume : Action()
    data class Transformation(val transformationType: TransformationType) : Action()
}

enum class TransformationType {
    Left, Right, Rotate, Down, FastDown, Fall
}

data class PlayListener constructor(
    val onStart: () -> Unit,
    val onPause: () -> Unit,
    val onReset: () -> Unit,
    val onSound: () -> Unit,
    val onTransformation: (TransformationType) -> Unit
)

fun combinedPlayListener(
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onReset: () -> Unit = {},
    onSound: () -> Unit = {},
    onTransformation: (TransformationType) -> Unit = {}
) = PlayListener(
    onStart = onStart,
    onPause = onPause,
    onReset = onReset,
    onSound = onSound,
    onTransformation = onTransformation
)

fun TetrisState.onTransformation(transformationType: TransformationType): TetrisState {
    return when (transformationType) {
        TransformationType.Left -> {
            onLeft()
        }
        TransformationType.Right -> {
            onRight()
        }
        TransformationType.Down -> {
            onDown()
        }
        TransformationType.FastDown -> {
            onFastDown()
        }
        TransformationType.Fall -> {
            onFall()
        }
        TransformationType.Rotate -> {
            onRotate()
        }
    }?.finalize() ?: this.finalize()
}

private fun TetrisState.onLeft(): TetrisState? {
    return copy(
        tetris = tetris.copy(offset = Location(tetris.offset.x - 1, tetris.offset.y))
    ).takeIf { it.isValidInMatrix() }
}

private fun TetrisState.onRight(): TetrisState? {
    return copy(
        tetris = tetris.copy(offset = Location(tetris.offset.x + 1, tetris.offset.y))
    ).takeIf { it.isValidInMatrix() }
}

private fun TetrisState.onDown(): TetrisState? {
    return copy(
        tetris = tetris.copy(
            offset = Location(tetris.offset.x, tetris.offset.y + 1)
        )
    ).takeIf { it.isValidInMatrix() }
}

private fun TetrisState.onFastDown(): TetrisState? {
    return copy(
        tetris = tetris.copy(
            offset = Location(tetris.offset.x, tetris.offset.y + 2)
        )
    ).takeIf { it.isValidInMatrix() }
}

private fun TetrisState.onFall(): TetrisState? {
    while (true) {
        val result = onDown() ?: return this
        return result.onFall()
    }
}

private fun TetrisState.onRotate(): TetrisState? {
    if (tetris.shapes.size == 1) {
        return null
    }
    val nextType = if (tetris.type + 1 < tetris.shapes.size) {
        tetris.type + 1
    } else {
        0
    }
    return copy(
        tetris = tetris.copy(
            type = nextType,
        )
    ).adjustOffset().takeIf { it.isValidInMatrix() }
}

private fun TetrisState.adjustOffset(): TetrisState {
    val offsetX = tetris.offset.x
    val offsetY = tetris.offset.y
    return when {
        offsetX == -1 -> {
            copy(tetris = tetris.copy(offset = Location(x = offsetX + 1, y = offsetY)))
        }
        width - offsetX == 3 -> {
            if (tetris.shape.find { it.x == 3 } == null) {
                this
            } else {
                copy(
                    tetris = tetris.copy(
                        offset = Location(
                            x = offsetX - 1,
                            y = offsetY
                        )
                    )
                )
            }
        }
        width - offsetX == 2 -> {
            var move = 0
            val location = tetris.shape
            if (location.find { it.x == 2 } != null) {
                move++
            }
            if (location.find { it.x == 3 } != null) {
                move++
            }
            if (move > 0) {
                copy(
                    tetris = tetris.copy(
                        offset = Location(
                            x = offsetX - move,
                            y = offsetY
                        )
                    )
                )
            } else {
                this
            }
        }
        else -> {
            this
        }
    }
}

private fun TetrisState.isValidInMatrix(): Boolean {
    val offsetX = tetris.offset.x
    val offsetY = tetris.offset.y
    for (sh in tetris.shape) {
        val realX = sh.x + offsetX
        if (realX < 0 || realX >= width) {
            return false
        }
        val realY = sh.y + offsetY
        if (realY < 0) {
            continue
        }
        if (realY >= height) {
            return false
        }
        if (brickArray[realY][realX] == 1) {
            return false
        }
    }
    return true
}

private fun TetrisState.finalize(): TetrisState {
    if (canDown()) {
        return this
    } else {
        var gameOver = false
        for (location in tetris.shape) {
            val x = location.x + tetris.offset.x
            val y = location.y + tetris.offset.y
            if (x in 0 until width && y in 0 until height) {
                brickArray[y][x] = 1
            } else {
                gameOver = true
            }
        }
        return if (gameOver) {
            copy(gameStatus = GameStatus.GameOver)
        } else {
            val clearRes = clearIfNeed()
            if (clearRes == null) {
                copy(
                    gameStatus = GameStatus.Running,
                    tetris = nextTetris,
                    nextTetris = Tetris()
                )
            } else {
                copy(
                    gameStatus = GameStatus.LineClearing,
                    tetris = nextTetris,
                    nextTetris = Tetris()
                )
            }
        }
    }
}

private fun TetrisState.canDown(): Boolean {
    val offsetX = tetris.offset.x
    val offsetY = tetris.offset.y
    for (sp in tetris.shape) {
        val realX = sp.x + offsetX
        if (realX < 0 || realX >= width) {
            return false
        }
        val realY = sp.y + offsetY
        if (realY + 1 < 0) {
            continue
        }
        if (realY + 1 >= height) {
            return false
        }
        if (brickArray[realY + 1][realX] == 1) {
            return false
        }
    }
    return true
}

private fun TetrisState.clearIfNeed(): TetrisState? {
    var index = height - 1
    var removed = false
    while (true) {
        if (index < 0 || index >= height) {
            break
        }
        val lastLine = brickArray[index]
        if (lastLine.find { it == 1 } == null) {
            break
        }
        if (lastLine.find { it == 0 } == null) {
            removed = true
            for (i in index - 1 downTo 0) {
                brickArray[i].forEachIndexed { tempIndex, tempValue ->
                    brickArray[i + 1][tempIndex] = tempValue
                }
            }
        } else {
            index--
        }
    }
    return if (removed) this else null
}
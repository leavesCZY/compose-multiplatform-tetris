package github.leavesczy.compose_tetris.base.logic

import androidx.compose.runtime.Stable

/** Classic Tetris playfield size, shared by Android and Desktop. */
internal const val BoardWidth = 10

internal const val BoardHeight = 20

internal const val CellEmpty = 0

internal const val CellFilled = 1

internal const val LinesPerLevel = 10

@Stable
data class Cell(
    val x: Int,
    val y: Int
)

@Stable
enum class GameStatus {
    Welcome,
    Running,
    Paused,
    LineClearing,
    ScreenClearing,
    GameOver
}

@Stable
sealed class GameAction {

    @Stable
    data object Welcome : GameAction()

    @Stable
    data object Start : GameAction()

    @Stable
    data object Pause : GameAction()

    @Stable
    data object Reset : GameAction()

    @Stable
    data object ToggleSound : GameAction()

    @Stable
    data object EnterBackground : GameAction()

    @Stable
    data object Hold : GameAction()

    @Stable
    data class MovePiece(val move: PieceMove, val pieceSequence: Long = -1L) : GameAction()

    @Stable
    data class LockTimeout(val pieceSequence: Long) : GameAction()

    @Stable
    data class SetClearingLinesVisible(val visible: Boolean) : GameAction()

    @Stable
    data object FinishLineClear : GameAction()

}

@Stable
enum class PieceMove {
    Left,
    Right,
    Rotate,
    Gravity,
    SoftDrop,
    HardDrop
}

@Stable
data class GameState(
    val board: Array<IntArray>,
    val boardRevision: Long,
    val activePiece: Tetromino,
    val nextPiece: Tetromino,
    val holdPiece: Tetromino?,
    val holdUsedThisTurn: Boolean,
    val bagRemaining: List<PieceType>,
    val gameStatus: GameStatus,
    val soundEnabled: Boolean,
    val score: Int,
    val lines: Int,
    val level: Int,
    val highScore: Int,
    val pendingDropScore: Int,
    val scorePopup: Int,
    val lockResetCount: Int = 0,
    val clearingLines: Set<Int> = emptySet(),
    val clearingLinesVisible: Boolean = true,
    val screenWipeFilledRows: Int = 0,
    val screenWipeClearingRows: Int = 0
) {

    companion object {

        fun create(
            soundEnabled: Boolean = true,
            highScore: Int = 0
        ): GameState {
            var bag = createShuffledBag()
            val (active, bagAfterActive) = bag.drawNext()
            bag = bagAfterActive
            val (next, bagAfterNext) = bag.drawNext()
            return GameState(
                board = createEmptyBoard(),
                boardRevision = 0L,
                activePiece = active,
                nextPiece = next,
                holdPiece = null,
                holdUsedThisTurn = false,
                bagRemaining = bagAfterNext,
                gameStatus = GameStatus.Welcome,
                soundEnabled = soundEnabled,
                score = 0,
                lines = 0,
                level = 1,
                highScore = highScore,
                pendingDropScore = 0,
                scorePopup = 0,
                lockResetCount = 0
            )
        }

        fun createEmptyBoard(): Array<IntArray> {
            return Array(size = BoardHeight) {
                IntArray(size = BoardWidth)
            }
        }

        fun lineClearScore(linesCleared: Int, level: Int): Int {
            val base = when (linesCleared) {
                1 -> 100
                2 -> 300
                3 -> 500
                4 -> 800
                else -> 0
            }
            return base * level
        }

        fun gravityIntervalMs(level: Int): Long {
            return (500L - (level - 1L) * 42L).coerceAtLeast(minimumValue = 60L)
        }

    }

    val width: Int
        get() = board[0].size

    val height: Int
        get() = board.size

    val isRunning: Boolean
        get() = gameStatus == GameStatus.Running

    val isPaused: Boolean
        get() = gameStatus == GameStatus.Paused

    val canStartGame: Boolean
        get() = when (gameStatus) {
            GameStatus.Welcome,
            GameStatus.Paused,
            GameStatus.GameOver -> true

            GameStatus.Running,
            GameStatus.LineClearing,
            GameStatus.ScreenClearing -> false
        }

    val showActivePiece: Boolean
        get() = when (gameStatus) {
            GameStatus.Running,
            GameStatus.Paused -> true

            GameStatus.Welcome,
            GameStatus.LineClearing,
            GameStatus.ScreenClearing,
            GameStatus.GameOver -> false
        }

    val showSideHud: Boolean
        get() = when (gameStatus) {
            GameStatus.Running,
            GameStatus.Paused,
            GameStatus.LineClearing,
            GameStatus.GameOver -> true

            GameStatus.Welcome,
            GameStatus.ScreenClearing -> false
        }

    val displayScore: Int
        get() = if (gameStatus == GameStatus.LineClearing) {
            score + scorePopup
        } else {
            score
        }

    val displayLines: Int
        get() = if (gameStatus == GameStatus.LineClearing) {
            lines + clearingLines.size
        } else {
            lines
        }

    val displayLevel: Int
        get() = if (gameStatus == GameStatus.LineClearing) {
            ((lines + clearingLines.size) / LinesPerLevel) + 1
        } else {
            level
        }

    val displayHighScore: Int
        get() = maxOf(a = highScore, b = displayScore)

    fun copyBoard(): Array<IntArray> {
        return Array(size = height) { y ->
            board[y].copyOf()
        }
    }

    fun withBoard(newBoard: Array<IntArray>): GameState {
        return copy(
            board = newBoard,
            boardRevision = boardRevision + 1L
        )
    }

    fun applyLineClear(): GameState {
        val totalGain = scorePopup
        val clearedCount = clearingLines.size
        val newLines = lines + clearedCount
        val newLevel = (newLines / LinesPerLevel) + 1
        val newScore = score + totalGain
        val newHighScore = maxOf(a = highScore, b = newScore)
        val newBoard = createEmptyBoard()
        var writeIndex = height - 1
        for (readIndex in height - 1 downTo 0) {
            if (readIndex in clearingLines) {
                continue
            }
            for (x in 0 until width) {
                newBoard[writeIndex][x] = board[readIndex][x]
            }
            writeIndex--
        }
        return withBoard(newBoard = newBoard).copy(
            clearingLines = emptySet(),
            clearingLinesVisible = true,
            score = newScore,
            lines = newLines,
            level = newLevel,
            highScore = newHighScore,
            pendingDropScore = 0,
            scorePopup = 0
        ).spawnNextPiece(addDropScore = false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameState) return false
        return boardRevision == other.boardRevision &&
                soundEnabled == other.soundEnabled &&
                clearingLinesVisible == other.clearingLinesVisible &&
                gameStatus == other.gameStatus &&
                clearingLines == other.clearingLines &&
                screenWipeFilledRows == other.screenWipeFilledRows &&
                screenWipeClearingRows == other.screenWipeClearingRows &&
                activePiece == other.activePiece &&
                nextPiece == other.nextPiece &&
                holdPiece == other.holdPiece &&
                holdUsedThisTurn == other.holdUsedThisTurn &&
                bagRemaining == other.bagRemaining &&
                score == other.score &&
                lines == other.lines &&
                level == other.level &&
                highScore == other.highScore &&
                pendingDropScore == other.pendingDropScore &&
                scorePopup == other.scorePopup &&
                lockResetCount == other.lockResetCount
    }

    override fun hashCode(): Int {
        var result = boardRevision.hashCode()
        result = 31 * result + soundEnabled.hashCode()
        result = 31 * result + clearingLinesVisible.hashCode()
        result = 31 * result + gameStatus.hashCode()
        result = 31 * result + clearingLines.hashCode()
        result = 31 * result + screenWipeFilledRows
        result = 31 * result + screenWipeClearingRows
        result = 31 * result + activePiece.hashCode()
        result = 31 * result + nextPiece.hashCode()
        result = 31 * result + (holdPiece?.hashCode() ?: 0)
        result = 31 * result + holdUsedThisTurn.hashCode()
        result = 31 * result + bagRemaining.hashCode()
        result = 31 * result + score
        result = 31 * result + lines
        result = 31 * result + level
        result = 31 * result + highScore
        result = 31 * result + pendingDropScore
        result = 31 * result + scorePopup
        result = 31 * result + lockResetCount
        return result
    }

}
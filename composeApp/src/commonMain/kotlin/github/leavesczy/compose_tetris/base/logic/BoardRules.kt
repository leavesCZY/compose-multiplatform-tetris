package github.leavesczy.compose_tetris.base.logic

private val RotateKickOffsets = listOf(
    Cell(x = 0, y = 0),
    Cell(x = -1, y = 0),
    Cell(x = 1, y = 0),
    Cell(x = -2, y = 0),
    Cell(x = 2, y = 0),
    Cell(x = 0, y = -1),
    Cell(x = -1, y = -1),
    Cell(x = 1, y = -1),
    Cell(x = 0, y = 1)
)

internal const val MaxLockResets = 15

internal data class MoveOutcome(
    val state: GameState,
    val shouldLockImmediately: Boolean = false,
    val resetLockDelay: Boolean = false
)

internal fun GameState.applyMove(move: PieceMove): MoveOutcome {
    return when (move) {
        PieceMove.Left -> {
            lateralOrRotate(moved = tryShift(deltaX = -1, deltaY = 0))
        }

        PieceMove.Right -> {
            lateralOrRotate(moved = tryShift(deltaX = 1, deltaY = 0))
        }

        PieceMove.Rotate -> {
            lateralOrRotate(moved = tryRotate())
        }

        PieceMove.Gravity -> {
            dropWithoutLock(steps = 1, pointsPerCell = 0)
        }

        PieceMove.SoftDrop -> {
            dropWithoutLock(steps = 1, pointsPerCell = 1)
        }

        PieceMove.HardDrop -> {
            val (dropped, cells) = hardDropWithDistance()
            val scored = dropped.copy(pendingDropScore = pendingDropScore + cells * 2)
            MoveOutcome(
                state = scored.lockPiece(),
                shouldLockImmediately = true
            )
        }
    }
}

internal fun GameState.applyHold(): GameState {
    if (!isRunning || holdUsedThisTurn) {
        return this
    }
    val swapping = activePiece.resetForHold()
    val heldState = if (holdPiece == null) {
        val (spawned, bag) = bagRemaining.drawNext()
        copy(
            holdPiece = swapping,
            activePiece = nextPiece,
            nextPiece = spawned,
            bagRemaining = bag,
            holdUsedThisTurn = true,
            pendingDropScore = 0,
            lockResetCount = 0
        )
    } else {
        copy(
            holdPiece = swapping,
            activePiece = holdPiece.resetForHold(),
            holdUsedThisTurn = true,
            pendingDropScore = 0,
            lockResetCount = 0
        )
    }
    return heldState.withSpawnCheck()
}

internal fun GameState.canMoveDown(): Boolean {
    return tryShift(deltaX = 0, deltaY = 1) != null
}

internal fun GameState.isSpawnBlocked(): Boolean {
    return !isPieceValid()
}

internal fun GameState.lockPiece(): GameState {
    if (canMoveDown()) {
        return this
    }
    val newBoard = copyBoard()
    var lockOut = false
    for (cell in activePiece.cells) {
        val x = cell.x + activePiece.offset.x
        val y = cell.y + activePiece.offset.y
        if (x in 0 until width && y in 0 until height) {
            newBoard[y][x] = CellFilled
        } else {
            // Above the visible board, or otherwise off-grid → lock-out.
            lockOut = true
        }
    }
    val locked = withBoard(newBoard = newBoard).copy(lockResetCount = 0)
    if (lockOut) {
        return locked.toGameOver()
    }
    val fullLines = locked.findFullLines()
    return if (fullLines.isEmpty()) {
        locked.spawnNextPiece(addDropScore = true)
    } else {
        val gain = GameState.lineClearScore(
            linesCleared = fullLines.size,
            level = locked.level
        ) + locked.pendingDropScore
        val projectedScore = locked.score + gain
        locked.copy(
            gameStatus = GameStatus.LineClearing,
            clearingLines = fullLines,
            clearingLinesVisible = true,
            pendingDropScore = 0,
            scorePopup = gain,
            highScore = maxOf(a = locked.highScore, b = projectedScore)
        )
    }
}

internal fun GameState.spawnNextPiece(addDropScore: Boolean): GameState {
    val (spawned, bag) = bagRemaining.drawNext()
    val newScore = if (addDropScore) {
        score + pendingDropScore
    } else {
        score
    }
    return copy(
        gameStatus = GameStatus.Running,
        activePiece = nextPiece,
        nextPiece = spawned,
        bagRemaining = bag,
        holdUsedThisTurn = false,
        score = newScore,
        highScore = maxOf(a = highScore, b = newScore),
        pendingDropScore = 0,
        scorePopup = 0,
        lockResetCount = 0
    ).withSpawnCheck()
}

private fun GameState.withSpawnCheck(): GameState {
    return if (isSpawnBlocked()) {
        toGameOver()
    } else {
        this
    }
}

private fun GameState.toGameOver(): GameState {
    val finalScore = score + pendingDropScore
    return copy(
        gameStatus = GameStatus.GameOver,
        score = finalScore,
        highScore = maxOf(a = highScore, b = finalScore),
        pendingDropScore = 0,
        scorePopup = 0
    )
}

private fun GameState.lateralOrRotate(moved: GameState?): MoveOutcome {
    if (moved == null) {
        return MoveOutcome(state = this)
    }
    if (moved.canMoveDown()) {
        return MoveOutcome(state = moved.copy(lockResetCount = 0))
    }
    val resets = lockResetCount + 1
    if (resets > MaxLockResets) {
        return MoveOutcome(
            state = moved.copy(lockResetCount = resets).lockPiece(),
            shouldLockImmediately = true
        )
    }
    return MoveOutcome(
        state = moved.copy(lockResetCount = resets),
        resetLockDelay = true
    )
}

private fun GameState.dropWithoutLock(steps: Int, pointsPerCell: Int): MoveOutcome {
    var current = this
    var dropped = 0
    repeat(times = steps) {
        val moved = current.tryShift(deltaX = 0, deltaY = 1) ?: return MoveOutcome(
            state = current.copy(
                pendingDropScore = current.pendingDropScore + dropped * pointsPerCell
            ),
            resetLockDelay = false
        )
        current = moved
        dropped++
    }
    val scored = current.copy(
        pendingDropScore = current.pendingDropScore + dropped * pointsPerCell,
        lockResetCount = if (current.canMoveDown()) 0 else current.lockResetCount
    )
    return MoveOutcome(
        state = scored,
        resetLockDelay = false
    )
}

private fun GameState.hardDropWithDistance(): Pair<GameState, Int> {
    var current = this
    var cells = 0
    while (true) {
        val moved = current.tryShift(deltaX = 0, deltaY = 1) ?: return current to cells
        current = moved
        cells++
    }
}

private fun GameState.tryShift(deltaX: Int, deltaY: Int): GameState? {
    val shifted = copy(
        activePiece = activePiece.copy(
            offset = Cell(
                x = activePiece.offset.x + deltaX,
                y = activePiece.offset.y + deltaY
            )
        )
    )
    return shifted.takeIf { it.isPieceValid() }
}

private fun GameState.tryRotate(): GameState? {
    if (activePiece.rotations.size == 1) {
        return null
    }
    val rotatedPiece = activePiece.rotated()
    for (kick in RotateKickOffsets) {
        val candidate = copy(
            activePiece = rotatedPiece.copy(
                offset = Cell(
                    x = activePiece.offset.x + kick.x,
                    y = activePiece.offset.y + kick.y
                )
            )
        )
        if (candidate.isPieceValid()) {
            return candidate
        }
    }
    return null
}

private fun GameState.isPieceValid(): Boolean {
    val offsetX = activePiece.offset.x
    val offsetY = activePiece.offset.y
    for (cell in activePiece.cells) {
        val realX = cell.x + offsetX
        if (realX !in 0 until width) {
            return false
        }
        val realY = cell.y + offsetY
        if (realY < 0) {
            continue
        }
        if (realY >= height) {
            return false
        }
        if (board[realY][realX] == CellFilled) {
            return false
        }
    }
    return true
}

private fun GameState.findFullLines(): Set<Int> {
    val lines = mutableSetOf<Int>()
    for (y in 0 until height) {
        val row = board[y]
        var full = true
        for (value in row) {
            if (value == CellEmpty) {
                full = false
                break
            }
        }
        if (full) {
            lines.add(y)
        }
    }
    return lines
}

internal fun GameState.boardCellAt(x: Int, y: Int): Int {
    if (gameStatus == GameStatus.ScreenClearing) {
        val rowFromBottom = height - 1 - y
        val filled = rowFromBottom < screenWipeFilledRows && y >= screenWipeClearingRows
        return if (filled) {
            CellFilled
        } else {
            CellEmpty
        }
    }
    return board[y][x]
}
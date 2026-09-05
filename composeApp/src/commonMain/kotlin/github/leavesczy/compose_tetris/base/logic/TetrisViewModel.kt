package github.leavesczy.compose_tetris.base.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TetrisViewModel(
    private val soundPlayer: SoundPlayer
) : ViewModel() {

    private val screenWipeIntervalMs = 30L
    private val lineClearBlinkIntervalMs = 90L
    private val lineClearBlinkTimes = 3
    private val lockDelayMs = 500L

    private val actionChannel = Channel<GameAction>(capacity = Channel.UNLIMITED)

    private var gravityJob: Job? = null
    private var screenWipeJob: Job? = null
    private var lineClearJob: Job? = null
    private var lockDelayJob: Job? = null
    private var soundReady = false
    private var released = false
    private var introStarted = false
    private var pieceSequence = 0L
    private var bestHighScore = 0

    var gameState by mutableStateOf(value = GameState.create())
        private set

    init {
        viewModelScope.launch {
            soundPlayer.init()
            soundReady = true
        }
        viewModelScope.launch {
            for (action in actionChannel) {
                handleAction(action = action)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releaseResources()
    }

    fun releaseResources() {
        if (released) {
            return
        }
        released = true
        cancelGravityJob()
        cancelScreenWipeJob()
        cancelLineClearJob()
        cancelLockDelayJob()
        actionChannel.close()
        soundPlayer.release()
    }

    fun dispatch(action: GameAction) {
        if (released) {
            return
        }
        val stamped = when (action) {
            is GameAction.MovePiece -> action.copy(pieceSequence = pieceSequence)
            else -> action
        }
        actionChannel.trySend(element = stamped)
    }

    private fun handleAction(action: GameAction) {
        when (action) {
            GameAction.Welcome -> {
                startIntroIfNeeded()
            }

            GameAction.Reset -> {
                invalidatePieceSequence()
                startScreenWipe(nextStatus = GameStatus.Welcome)
            }

            GameAction.Start -> {
                startGame()
            }

            GameAction.Pause,
            GameAction.EnterBackground -> {
                pauseGame()
            }

            GameAction.ToggleSound -> {
                toggleSound()
            }

            GameAction.Hold -> {
                holdPiece()
            }

            is GameAction.MovePiece -> {
                if (action.pieceSequence != pieceSequence) {
                    return
                }
                movePiece(move = action.move)
            }

            is GameAction.LockTimeout -> {
                if (action.pieceSequence != pieceSequence) {
                    return
                }
                if (gameState.isRunning && !gameState.canMoveDown()) {
                    applyResultState(next = gameState.lockPiece(), move = null, didMove = false)
                }
            }

            is GameAction.SetClearingLinesVisible -> {
                if (gameState.gameStatus == GameStatus.LineClearing) {
                    commitState(newState = gameState.copy(clearingLinesVisible = action.visible))
                }
            }

            GameAction.FinishLineClear -> {
                finishLineClear()
            }
        }
    }

    private fun startIntroIfNeeded() {
        if (introStarted || gameState.gameStatus != GameStatus.Welcome) {
            return
        }
        introStarted = true
        startScreenWipe(nextStatus = GameStatus.Welcome)
    }

    private fun startGame() {
        if (!gameState.canStartGame) {
            return
        }
        if (gameState.isPaused) {
            invalidatePieceSequence()
            commitState(newState = gameState.copy(gameStatus = GameStatus.Running))
        } else {
            invalidatePieceSequence()
            commitState(
                newState = GameState.create(
                    soundEnabled = gameState.soundEnabled,
                    highScore = bestSessionHighScore()
                ).copy(gameStatus = GameStatus.Running)
            )
        }
        playSound(soundType = SoundType.Transform)
    }

    private fun pauseGame() {
        if (gameState.isRunning) {
            cancelLockDelayJob()
            invalidatePieceSequence()
            commitState(newState = gameState.copy(gameStatus = GameStatus.Paused))
            playSound(soundType = SoundType.Transform)
        }
    }

    private fun toggleSound() {
        val enabled = gameState.soundEnabled
        commitState(newState = gameState.copy(soundEnabled = !enabled))
        if (enabled) {
            soundPlayer.pause()
        } else {
            playSound(soundType = SoundType.Transform)
        }
    }

    private fun holdPiece() {
        if (!gameState.isRunning) {
            return
        }
        val previous = gameState
        val next = previous.applyHold()
        if (next == previous) {
            return
        }
        cancelLockDelayJob()
        invalidatePieceSequence()
        applyResultState(next = next, move = null, didMove = true)
    }

    private fun movePiece(move: PieceMove) {
        if (!gameState.isRunning) {
            return
        }
        val previous = gameState
        val outcome = previous.applyMove(move = move)
        val next = outcome.state
        when (next.gameStatus) {
            GameStatus.Running -> {
                if (outcome.shouldLockImmediately) {
                    invalidatePieceSequence()
                }
                commitState(newState = next)
                playMoveSound(move = move, didMove = next != previous)
                if (outcome.shouldLockImmediately) {
                    cancelLockDelayJob()
                    if (!next.canMoveDown()) {
                        startLockDelay()
                    }
                } else if (!next.canMoveDown()) {
                    if (outcome.resetLockDelay) {
                        startLockDelay()
                    } else if (lockDelayJob?.isActive != true) {
                        startLockDelay()
                    }
                } else {
                    cancelLockDelayJob()
                }
            }

            GameStatus.LineClearing,
            GameStatus.GameOver -> {
                applyResultState(next = next, move = move, didMove = next != previous)
            }

            else -> {
                commitState(newState = next)
            }
        }
    }

    private fun applyResultState(next: GameState, move: PieceMove?, didMove: Boolean) {
        cancelLockDelayJob()
        when (next.gameStatus) {
            GameStatus.LineClearing,
            GameStatus.GameOver -> {
                invalidatePieceSequence()
            }

            GameStatus.Running -> {
                invalidatePieceSequence()
            }

            else -> {
            }
        }
        commitState(newState = next)
        if (move != null) {
            playMoveSound(move = move, didMove = didMove)
        } else if (didMove) {
            playSound(soundType = SoundType.Transform)
        }
        when (next.gameStatus) {
            GameStatus.LineClearing -> {
                playSound(soundType = SoundType.LineClear)
                startLineClearAnimation()
            }

            GameStatus.GameOver -> {
                playSound(soundType = SoundType.Welcome)
                startScreenWipe(nextStatus = GameStatus.GameOver)
            }

            GameStatus.Running -> {
                if (!next.canMoveDown()) {
                    startLockDelay()
                }
            }

            else -> {
            }
        }
    }

    private fun playMoveSound(move: PieceMove, didMove: Boolean) {
        if (!didMove && move != PieceMove.HardDrop) {
            return
        }
        when (move) {
            PieceMove.Left,
            PieceMove.Right,
            PieceMove.SoftDrop -> {
                playSound(soundType = SoundType.Transform)
            }

            PieceMove.HardDrop -> {
                playSound(soundType = SoundType.HardDrop)
            }

            PieceMove.Rotate -> {
                playSound(soundType = SoundType.Rotate)
            }

            PieceMove.Gravity -> {
            }
        }
    }

    private fun startGravityJob() {
        cancelGravityJob()
        gravityJob = viewModelScope.launch {
            while (isActive) {
                val interval = GameState.gravityIntervalMs(level = gameState.level)
                delay(timeMillis = interval)
                if (gameState.isRunning) {
                    dispatch(action = GameAction.MovePiece(move = PieceMove.Gravity))
                } else {
                    break
                }
            }
        }
    }

    private fun startLockDelay() {
        cancelLockDelayJob()
        val sequence = pieceSequence
        lockDelayJob = viewModelScope.launch {
            delay(timeMillis = lockDelayMs)
            actionChannel.send(element = GameAction.LockTimeout(pieceSequence = sequence))
        }
    }

    private fun startLineClearAnimation() {
        cancelGravityJob()
        cancelLineClearJob()
        lineClearJob = viewModelScope.launch {
            repeat(times = lineClearBlinkTimes) {
                delay(timeMillis = lineClearBlinkIntervalMs)
                actionChannel.send(
                    element = GameAction.SetClearingLinesVisible(visible = false)
                )
                delay(timeMillis = lineClearBlinkIntervalMs)
                actionChannel.send(
                    element = GameAction.SetClearingLinesVisible(visible = true)
                )
            }
            actionChannel.send(element = GameAction.FinishLineClear)
        }
    }

    private fun finishLineClear() {
        if (gameState.gameStatus != GameStatus.LineClearing) {
            return
        }
        val cleared = gameState.applyLineClear()
        invalidatePieceSequence()
        commitState(newState = cleared)
        if (cleared.gameStatus == GameStatus.GameOver) {
            playSound(soundType = SoundType.Welcome)
            startScreenWipe(nextStatus = GameStatus.GameOver)
        } else if (cleared.isRunning && !cleared.canMoveDown()) {
            startLockDelay()
        }
    }

    private fun startScreenWipe(nextStatus: GameStatus) {
        cancelGravityJob()
        cancelLineClearJob()
        cancelLockDelayJob()
        cancelScreenWipeJob()
        invalidatePieceSequence()
        if (nextStatus == GameStatus.Welcome) {
            playSound(soundType = SoundType.Welcome)
        }
        screenWipeJob = viewModelScope.launch {
            val height = gameState.height
            val keepSound = gameState.soundEnabled
            val keepHigh = bestSessionHighScore(candidate = gameState.highScore)
            commitState(
                newState = gameState.copy(
                    gameStatus = GameStatus.ScreenClearing,
                    clearingLines = emptySet(),
                    screenWipeFilledRows = 0,
                    screenWipeClearingRows = 0,
                    board = GameState.createEmptyBoard(),
                    boardRevision = gameState.boardRevision + 1L,
                    scorePopup = 0
                )
            )
            repeat(times = 2) {
                for (filledRows in 1..height) {
                    if (!isActive) {
                        return@launch
                    }
                    commitState(
                        newState = gameState.copy(
                            gameStatus = GameStatus.ScreenClearing,
                            screenWipeFilledRows = filledRows,
                            screenWipeClearingRows = 0
                        )
                    )
                    delay(timeMillis = screenWipeIntervalMs)
                }
                for (clearedRows in 1..height) {
                    if (!isActive) {
                        return@launch
                    }
                    commitState(
                        newState = gameState.copy(
                            gameStatus = GameStatus.ScreenClearing,
                            screenWipeFilledRows = height,
                            screenWipeClearingRows = clearedRows
                        )
                    )
                    delay(timeMillis = screenWipeIntervalMs)
                }
            }
            delay(timeMillis = 100L)
            invalidatePieceSequence()
            commitState(
                newState = GameState.create(
                    soundEnabled = keepSound,
                    highScore = keepHigh
                ).copy(gameStatus = nextStatus)
            )
        }
    }

    private fun commitState(newState: GameState) {
        val levelChanged = gameState.level != newState.level
        if (newState.highScore > bestHighScore) {
            bestHighScore = newState.highScore
        }
        gameState = newState
        if (newState.gameStatus == GameStatus.Running) {
            if (gravityJob?.isActive != true || levelChanged) {
                startGravityJob()
            }
        } else {
            cancelGravityJob()
        }
    }

    private fun bestSessionHighScore(candidate: Int = gameState.highScore): Int {
        return maxOf(a = bestHighScore, b = candidate)
    }

    private fun invalidatePieceSequence() {
        pieceSequence++
    }

    private fun cancelGravityJob() {
        gravityJob?.cancel()
        gravityJob = null
    }

    private fun cancelScreenWipeJob() {
        screenWipeJob?.cancel()
        screenWipeJob = null
    }

    private fun cancelLineClearJob() {
        lineClearJob?.cancel()
        lineClearJob = null
    }

    private fun cancelLockDelayJob() {
        lockDelayJob?.cancel()
        lockDelayJob = null
    }

    private fun playSound(soundType: SoundType) {
        if (soundReady && gameState.soundEnabled) {
            soundPlayer.play(soundType = soundType)
        }
    }

}
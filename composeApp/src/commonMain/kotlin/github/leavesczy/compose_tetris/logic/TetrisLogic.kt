package github.leavesczy.compose_tetris.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:06
 * @Desc:
 */
class TetrisLogic(
    private val coroutineScope: CoroutineScope,
    private val soundPlayer: SoundPlayer
) {

    companion object {

        private const val DOWN_SPEED = 500L

        private const val CLEAR_SCREEN_SPEED = 30L

    }

    var tetrisViewState by mutableStateOf(value = TetrisViewState())
        private set

    private var downJob: Job? = null

    private var clearScreenJob: Job? = null

    fun dispatch(action: Action) {
        coroutineScope.launch(context = Dispatchers.IO) {
            when (action) {
                Action.Welcome, Action.Reset -> {
                    onWelcome()
                }

                Action.Start -> {
                    onStartGame()
                }

                Action.Background, Action.Pause -> {
                    onPauseGame()
                }

                Action.Resume -> {

                }

                Action.Sound -> {
                    onSound()
                }

                is Action.Transformation -> {
                    onTransformation(transformation = action)
                }
            }
            playSound(action = action)
        }
    }

    private suspend fun onWelcome() {
        startClearScreen(nextStatus = GameStatus.Welcome)
    }

    private suspend fun onGameOver() {
        startClearScreen(nextStatus = GameStatus.GameOver)
    }

    private fun onStartGame() {
        if (!tetrisViewState.canStartGame) {
            return
        }
        if (tetrisViewState.isPaused) {
            dispatchState(newState = tetrisViewState.copy(gameStatus = GameStatus.Running))
        } else {
            dispatchState(
                newState = TetrisViewState().copy(
                    gameStatus = GameStatus.Running,
                    soundEnable = tetrisViewState.soundEnable
                )
            )
        }
    }

    private fun onPauseGame() {
        if (tetrisViewState.isRunning) {
            dispatchState(newState = tetrisViewState.copy(gameStatus = GameStatus.Paused))
        }
    }

    private fun onSound() {
        dispatchState(newState = tetrisViewState.copy(soundEnable = !tetrisViewState.soundEnable))
    }

    private suspend fun onTransformation(transformation: Action.Transformation) {
        if (!tetrisViewState.isRunning) {
            return
        }
        val viewState =
            tetrisViewState.onTransformation(transformationType = transformation.transformationType)
        when (viewState.gameStatus) {
            GameStatus.Running -> {
                dispatchState(newState = viewState)
            }

            GameStatus.LineClearing -> {
                playSound(soundType = SoundType.Clean)
                dispatchState(newState = viewState.copy(gameStatus = GameStatus.Running))
            }

            GameStatus.GameOver -> {
                playSound(soundType = SoundType.Welcome)
                dispatchState(newState = viewState)
                onGameOver()
            }

            else -> {
                throw RuntimeException("error game status")
            }
        }
    }

    private fun startDownJob() {
        cancelDownJob()
        cancelClearScreenJob()
        downJob = coroutineScope.launch {
            while (tetrisViewState.isRunning) {
                delay(timeMillis = DOWN_SPEED)
                if (tetrisViewState.isRunning) {
                    dispatch(action = Action.Transformation(TransformationType.Down))
                }
            }
        }
    }

    private suspend fun startClearScreen(nextStatus: GameStatus) {
        cancelDownJob()
        if (clearScreenJob?.isActive == true) {
            return
        }
        clearScreenJob = coroutineScope.launch {
            val clearScreen = suspend {
                val width = tetrisViewState.width
                val height = tetrisViewState.height
                for (y in height - 1 downTo 0) {
                    val brickArray = tetrisViewState.brickArray
                    for (x in 0 until width) {
                        brickArray[y][x] = 1
                    }
                    dispatchState(
                        tetrisViewState.copy(
                            tetris = Tetris(),
                            gameStatus = GameStatus.ScreenClearing
                        )
                    )
                    delay(CLEAR_SCREEN_SPEED)
                }
                for (y in 0 until height) {
                    val brickArray = tetrisViewState.brickArray
                    for (x in 0 until width) {
                        brickArray[y][x] = 0
                    }
                    dispatchState(
                        tetrisViewState.copy(
                            tetris = Tetris(),
                            gameStatus = GameStatus.ScreenClearing
                        )
                    )
                    delay(CLEAR_SCREEN_SPEED)
                }
            }
            clearScreen()
            clearScreen()
            delay(100)
            dispatchState(
                TetrisViewState().copy(
                    gameStatus = nextStatus,
                    soundEnable = tetrisViewState.soundEnable
                )
            )
        }
    }

    private fun cancelDownJob() {
        downJob?.cancel()
        downJob = null
    }

    private fun cancelClearScreenJob() {
        clearScreenJob?.cancel()
        clearScreenJob = null
    }

    private fun dispatchState(newState: TetrisViewState) {
        tetrisViewState = newState
        if (newState.gameStatus == GameStatus.Running) {
            if (downJob?.isActive != true) {
                startDownJob()
                return
            }
        } else {
            cancelDownJob()
        }
    }

    private suspend fun playSound(action: Action) {
        if (!tetrisViewState.soundEnable) {
            return
        }
        when (action) {
            Action.Welcome, Action.Reset -> {
                playSound(soundType = SoundType.Welcome)
            }

            Action.Start, Action.Pause -> {
                playSound(soundType = SoundType.Transformation)
            }

            Action.Sound -> {
                playSound(soundType = SoundType.Transformation)
            }

            is Action.Transformation -> {
                if (tetrisViewState.isRunning) {
                    when (action.transformationType) {
                        TransformationType.Left, TransformationType.Right, TransformationType.FastDown -> {
                            playSound(soundType = SoundType.Transformation)
                        }

                        TransformationType.Fall -> {
                            playSound(soundType = SoundType.Fall)
                        }

                        TransformationType.Down -> {

                        }

                        TransformationType.Rotate -> {
                            playSound(soundType = SoundType.Rotate)
                        }
                    }
                }
            }

            Action.Background -> {

            }

            Action.Resume -> {

            }
        }
    }

    private suspend fun playSound(soundType: SoundType) {
        if (tetrisViewState.soundEnable) {
            withContext(context = Dispatchers.IO) {
                delay(timeMillis = 100L)
                soundPlayer.play(soundType)
            }
        }
    }

    fun release() {
        soundPlayer.release()
    }

}
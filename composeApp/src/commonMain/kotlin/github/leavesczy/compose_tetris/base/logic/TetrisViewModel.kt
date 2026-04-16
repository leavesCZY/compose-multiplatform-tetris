package github.leavesczy.compose_tetris.base.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:03
 * @Desc:
 */
class TetrisViewModel(private val soundPlayer: SoundPlayer) : ViewModel() {

    private val downSpeed = 500L

    private val clearScreenSpeed = 30L

    private var downJob: Job? = null

    private var clearScreenJob: Job? = null

    var tetrisViewState by mutableStateOf(value = TetrisViewState())
        private set

    init {
        viewModelScope.launch {
            soundPlayer.init()
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundPlayer.release()
    }

    fun dispatch(action: Action) {
        viewModelScope.launch(context = Dispatchers.IO) {
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

    private fun onWelcome() {
        startClearScreen(nextStatus = GameStatus.Welcome)
    }

    private fun onGameOver() {
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

    private fun onTransformation(transformation: Action.Transformation) {
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
        downJob = viewModelScope.launch {
            while (tetrisViewState.isRunning) {
                delay(timeMillis = downSpeed)
                if (tetrisViewState.isRunning) {
                    dispatch(action = Action.Transformation(TransformationType.Down))
                }
            }
        }
    }

    private fun startClearScreen(nextStatus: GameStatus) {
        cancelDownJob()
        if (clearScreenJob?.isCompleted == true) {
            return
        }
        clearScreenJob = viewModelScope.launch {
            val clearScreen = suspend {
                val width = tetrisViewState.width
                val height = tetrisViewState.height
                for (y in height - 1 downTo 0) {
                    val brickArray = tetrisViewState.brickArray
                    for (x in 0 until width) {
                        brickArray[y][x] = 1
                    }
                    dispatchState(
                        newState = tetrisViewState.copy(
                            tetris = Tetris(),
                            gameStatus = GameStatus.ScreenClearing
                        )
                    )
                    delay(timeMillis = clearScreenSpeed)
                }
                for (y in 0 until height) {
                    val brickArray = tetrisViewState.brickArray
                    for (x in 0 until width) {
                        brickArray[y][x] = 0
                    }
                    dispatchState(
                        newState = tetrisViewState.copy(
                            tetris = Tetris(),
                            gameStatus = GameStatus.ScreenClearing
                        )
                    )
                    delay(timeMillis = clearScreenSpeed)
                }
            }
            clearScreen()
            clearScreen()
            delay(timeMillis = 100L)
            dispatchState(
                newState = TetrisViewState().copy(
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
            if (downJob?.isCompleted != true) {
                startDownJob()
                return
            }
        } else {
            cancelDownJob()
        }
    }

    private fun playSound(action: Action) {
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
                        TransformationType.Left,
                        TransformationType.Right,
                        TransformationType.FastDown -> {
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

            Action.Background,
            Action.Resume -> {

            }
        }
    }

    private fun playSound(soundType: SoundType) {
        if (tetrisViewState.soundEnable) {
            soundPlayer.play(soundType)
        }
    }

}
package github.leavesczy.compose_tetris.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.leavesczy.compose_tetris.utils.SoundPlayerInstance
import github.leavesczy.compose_tetris.utils.SoundType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @Author: leavesCZY
 * @Date: 2021/5/31 18:20
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class TetrisViewModel : ViewModel() {

    companion object {

        private const val DOWN_SPEED = 500L

        private const val CLEAR_SCREEN_SPEED = 30L

    }

    private val _tetrisStateFlow = MutableStateFlow(TetrisState())

    val tetrisStateFlow = _tetrisStateFlow.asStateFlow()

    private val tetrisState: TetrisState
        get() = _tetrisStateFlow.value

    private var downJob: Job? = null

    private var clearScreenJob: Job? = null

    fun dispatch(action: Action) {
        playSound(action)
        when (action) {
            Action.Welcome, Action.Reset -> {
                onWelcome()
            }
            Action.Start -> {
                if (!tetrisState.canStartGame) {
                    return
                }
                if (tetrisState.gameStatus == GameStatus.Paused) {
                    dispatchState(tetrisState.copy(gameStatus = GameStatus.Running))
                    startDownJob()
                } else {
                    onStartGame()
                }
            }
            Action.Background, Action.Pause -> {
                onPauseGame()
            }
            Action.Resume -> {

            }
            Action.Sound -> {
                dispatchState(tetrisState.copy(soundEnable = !tetrisState.soundEnable))
            }
            is Action.Transformation -> {
                if (!tetrisState.isRunning) {
                    return
                }
                val viewState =
                    tetrisState.onTransformation(transformationType = action.transformationType)
                when (viewState.gameStatus) {
                    GameStatus.Running -> {
                        dispatchState(viewState)
                        when (action.transformationType) {
                            TransformationType.Left, TransformationType.Right -> {

                            }
                            TransformationType.FastDown -> {
                                startDownJob()
                            }
                            TransformationType.Fall -> {

                            }
                            TransformationType.Down -> {
                                startDownJob()
                            }
                            TransformationType.Rotate -> {

                            }
                        }
                    }
                    GameStatus.LineClearing -> {
                        playSound(SoundType.Clean)
                        dispatchState(viewState.copy(gameStatus = GameStatus.Running))
                        startDownJob()
                    }
                    GameStatus.GameOver -> {
                        dispatchState(viewState)
                        onGameOver()
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun onWelcome() {
        startClearScreenJob {
            dispatchState(TetrisState().copy(gameStatus = GameStatus.Welcome))
        }
    }

    private fun onStartGame() {
        dispatchState(TetrisState().copy(gameStatus = GameStatus.Running))
        startDownJob()
    }

    private fun onPauseGame() {
        if (tetrisState.isRunning) {
            cancelDownJob()
            dispatchState(tetrisState.copy(gameStatus = GameStatus.Paused))
        }
    }

    private fun onGameOver() {
        startClearScreenJob {
            dispatchState(
                TetrisState().copy(gameStatus = GameStatus.GameOver)
            )
        }
    }

    private fun startClearScreenJob(invokeOnCompletion: () -> Unit) {
        cancelDownJob()
        cancelClearScreenJob()
        clearScreenJob = viewModelScope.launch {
            playSound(Action.Welcome)
            val width = tetrisState.width
            val height = tetrisState.height
            for (y in height - 1 downTo 0) {
                val brickArray = tetrisState.brickArray
                for (x in 0 until width) {
                    brickArray[y][x] = 1
                }
                dispatchState(
                    tetrisState.copy(
                        gameStatus = GameStatus.ScreenClearing,
                        tetris = Tetris()
                    )
                )
                delay(CLEAR_SCREEN_SPEED)
            }
            for (y in 0 until height) {
                val brickArray = tetrisState.brickArray
                for (x in 0 until width) {
                    brickArray[y][x] = 0
                }
                dispatchState(
                    tetrisState.copy(
                        gameStatus = GameStatus.ScreenClearing,
                        tetris = Tetris()
                    )
                )
                delay(CLEAR_SCREEN_SPEED)
            }
            delay(100)
        }.apply {
            invokeOnCompletion {
                if (it == null) {
                    invokeOnCompletion()
                }
            }
        }
    }

    private fun cancelClearScreenJob() {
        clearScreenJob?.cancel()
        clearScreenJob = null
    }

    private fun startDownJob() {
        cancelDownJob()
        cancelClearScreenJob()
        downJob = viewModelScope.launch {
            delay(DOWN_SPEED)
            dispatch(Action.Transformation(TransformationType.Down))
        }
    }

    private fun cancelDownJob() {
        downJob?.cancel()
        downJob = null
    }

    private fun dispatchState(tetrisState: TetrisState) {
        _tetrisStateFlow.value = tetrisState
    }

    private fun playSound(action: Action) {
        when (action) {
            Action.Welcome -> {
                playSound(SoundType.Welcome)
            }
            Action.Start, Action.Pause -> {
                playSound(SoundType.Transformation)
            }
            Action.Reset -> {

            }
            Action.Background -> {

            }
            Action.Resume -> {

            }
            Action.Sound -> {
                playSound(SoundType.Transformation)
            }
            is Action.Transformation -> {
                when (action.transformationType) {
                    TransformationType.Left, TransformationType.Right, TransformationType.FastDown -> {
                        playSound(SoundType.Transformation)
                    }
                    TransformationType.Fall -> {
                        playSound(SoundType.Fall)
                    }
                    TransformationType.Down -> {

                    }
                    TransformationType.Rotate -> {
                        playSound(SoundType.Rotate)
                    }
                }
            }
        }
    }

    private fun playSound(soundType: SoundType) {
        if (tetrisState.soundEnable) {
            SoundPlayerInstance.play(soundType)
        }
    }

}
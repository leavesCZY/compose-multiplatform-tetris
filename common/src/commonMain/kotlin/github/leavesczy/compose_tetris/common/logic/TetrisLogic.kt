package github.leavesczy.compose_tetris.common.logic

import github.leavesczy.compose_tetris.platform.getSoundPlayer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:06
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
interface ITetrisLogic {

    val tetrisStateFlow: StateFlow<TetrisState>

    fun provideScope(coroutineScope: CoroutineScope)

    fun dispatch(action: Action)

}

class TetrisLogicImpl : ITetrisLogic {

    companion object {

        private const val DOWN_SPEED = 500L

        private const val CLEAR_SCREEN_SPEED = 30L

    }

    private lateinit var coroutineScope: CoroutineScope

    private val _tetrisStateFlow = MutableStateFlow(TetrisState())

    override val tetrisStateFlow = _tetrisStateFlow.asStateFlow()

    private val tetrisState: TetrisState
        get() = _tetrisStateFlow.value

    private var downJob: Job? = null

    private var clearScreenJob: Job? = null

    private val soundPlayer = getSoundPlayer()


    override fun provideScope(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    override fun dispatch(action: Action) {
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

    private suspend fun onStartGame() {
        if (!tetrisState.canStartGame) {
            return
        }
        if (tetrisState.isPaused) {
            dispatchState(newState = tetrisState.copy(gameStatus = GameStatus.Running))
        } else {
            dispatchState(
                newState = TetrisState().copy(
                    gameStatus = GameStatus.Running,
                    soundEnable = tetrisState.soundEnable
                )
            )
        }
    }

    private suspend fun onPauseGame() {
        if (tetrisState.isRunning) {
            dispatchState(newState = tetrisState.copy(gameStatus = GameStatus.Paused))
        }
    }

    private suspend fun onSound() {
        dispatchState(newState = tetrisState.copy(soundEnable = !tetrisState.soundEnable))
    }

    private suspend fun onTransformation(transformation: Action.Transformation) {
        if (!tetrisState.isRunning) {
            return
        }
        val viewState =
            tetrisState.onTransformation(transformationType = transformation.transformationType)
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
                throw RuntimeException("非法状态")
            }
        }
    }

    private fun startDownJob() {
        cancelDownJob()
        cancelClearScreenJob()
        downJob = coroutineScope.launch {
            while (tetrisState.isRunning) {
                delay(timeMillis = DOWN_SPEED)
                if (tetrisState.isRunning) {
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
                val width = tetrisState.width
                val height = tetrisState.height
                for (y in height - 1 downTo 0) {
                    val brickArray = tetrisState.brickArray
                    for (x in 0 until width) {
                        brickArray[y][x] = 1
                    }
                    dispatchState(
                        tetrisState.copy(
                            tetris = Tetris(),
                            gameStatus = GameStatus.ScreenClearing
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
                TetrisState().copy(
                    gameStatus = nextStatus,
                    soundEnable = tetrisState.soundEnable
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

    private suspend fun dispatchState(newState: TetrisState) {
        _tetrisStateFlow.emit(newState)
        if (newState.gameStatus == GameStatus.Running) {
            if (downJob?.isActive != true) {
                startDownJob()
                return
            }
        } else {
            cancelDownJob()
        }
    }

    private fun playSound(action: Action) {
        when (action) {
            Action.Welcome, Action.Reset -> {
                playSound(soundType = SoundType.Welcome)
            }
            Action.Start, Action.Pause -> {
                playSound(soundType = SoundType.Transformation)
            }
            Action.Background -> {

            }
            Action.Resume -> {

            }
            Action.Sound -> {
                playSound(soundType = SoundType.Transformation)
            }
            is Action.Transformation -> {
                if (tetrisState.isRunning) {
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
        }
    }

    private fun playSound(soundType: SoundType) {
        if (tetrisState.soundEnable) {
            coroutineScope.launch(context = Dispatchers.IO) {
                soundPlayer.play(soundType)
            }
        }
    }

}
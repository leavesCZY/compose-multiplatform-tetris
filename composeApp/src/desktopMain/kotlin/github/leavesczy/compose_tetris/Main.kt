package github.leavesczy.compose_tetris

import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import github.leavesczy.compose_tetris.base.logic.GameAction
import github.leavesczy.compose_tetris.base.logic.PieceMove
import github.leavesczy.compose_tetris.base.logic.TetrisViewModel
import github.leavesczy.compose_tetris.base.ui.InputRepeatTiming
import github.leavesczy.compose_tetris.base.ui.TetrisPage
import github.leavesczy.compose_tetris.resources.Res
import github.leavesczy.compose_tetris.resources.app_name
import github.leavesczy.compose_tetris.resources.desktop_launch_icon
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.Toolkit
import kotlin.math.min

fun main() = application {
    val viewModel = remember {
        TetrisViewModel(soundPlayer = DesktopSoundPlayer())
    }
    var pressedMoves by remember { mutableStateOf(value = setOf<PieceMove>()) }
    var pressedOneShotKeys by remember { mutableStateOf(value = setOf<Key>()) }
    DisposableEffect(key1 = viewModel) {
        onDispose {
            viewModel.releaseResources()
        }
    }
    LaunchedEffect(key1 = pressedMoves) {
        val moves = pressedMoves
        if (moves.isEmpty()) {
            return@LaunchedEffect
        }
        delay(timeMillis = InputRepeatTiming.DasDelayMs)
        while (isActive) {
            for (move in moves) {
                viewModel.dispatch(action = GameAction.MovePiece(move = move))
            }
            delay(timeMillis = InputRepeatTiming.ArrIntervalMs)
        }
    }
    val windowState = rememberWindowState(
        size = preferredWindowSize(),
        position = WindowPosition.Aligned(alignment = Alignment.Center)
    )
    Window(
        title = stringResource(resource = Res.string.app_name),
        icon = painterResource(Res.drawable.desktop_launch_icon),
        resizable = true,
        state = windowState,
        onKeyEvent = { keyEvent ->
            handleKeyEvent(
                keyEvent = keyEvent,
                viewModel = viewModel,
                pressedMoves = pressedMoves,
                onPressedMovesChanged = { pressedMoves = it },
                pressedOneShotKeys = pressedOneShotKeys,
                onPressedOneShotKeysChanged = { pressedOneShotKeys = it }
            )
        },
        onCloseRequest = {
            viewModel.releaseResources()
            exitApplication()
        }
    ) {
        window.minimumSize = java.awt.Dimension(640, 720)
        val windowSizeClass = calculateWindowSizeClass()
        TetrisPage(
            modifier = Modifier,
            windowSizeClass = windowSizeClass,
            viewModel = viewModel
        )
    }
}

private fun preferredWindowSize(): DpSize {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    // Vertical layout: board on top, controls below.
    val targetWidth = min(980.0, screenSize.width * 0.55).toFloat()
    val targetHeight = min(1000.0, screenSize.height * 0.88).toFloat()
    val width = targetWidth.coerceAtLeast(640f)
    val height = targetHeight.coerceAtLeast(720f)
    return DpSize(width = width.dp, height = height.dp)
}

private fun handleKeyEvent(
    keyEvent: KeyEvent,
    viewModel: TetrisViewModel,
    pressedMoves: Set<PieceMove>,
    onPressedMovesChanged: (Set<PieceMove>) -> Unit,
    pressedOneShotKeys: Set<Key>,
    onPressedOneShotKeysChanged: (Set<Key>) -> Unit
): Boolean {
    val key = keyEvent.key
    val repeatableMove = key.toRepeatableMove()
    when (keyEvent.type) {
        KeyEventType.KeyDown -> {
            val oneShotAction = key.toOneShotAction()
            if (oneShotAction != null) {
                if (key in pressedOneShotKeys) {
                    return true
                }
                onPressedOneShotKeysChanged(pressedOneShotKeys + key)
                viewModel.dispatch(action = oneShotAction)
                return true
            }
            if (repeatableMove != null && repeatableMove !in pressedMoves) {
                viewModel.dispatch(action = GameAction.MovePiece(move = repeatableMove))
                onPressedMovesChanged(pressedMoves + repeatableMove)
                return true
            }
            return repeatableMove != null
        }

        KeyEventType.KeyUp -> {
            if (key in pressedOneShotKeys) {
                onPressedOneShotKeysChanged(pressedOneShotKeys - key)
                return true
            }
            if (repeatableMove != null && repeatableMove in pressedMoves) {
                onPressedMovesChanged(pressedMoves - repeatableMove)
                return true
            }
            return false
        }

        else -> {
            return false
        }
    }
}

private fun Key.toOneShotAction(): GameAction? {
    return when (this) {
        Key.Enter -> GameAction.Start
        Key.P -> GameAction.Pause
        Key.R -> GameAction.Reset
        Key.M -> GameAction.ToggleSound
        Key.C, Key.ShiftLeft, Key.ShiftRight -> GameAction.Hold
        Key.Spacebar, Key.DirectionUp, Key.W, Key.NumPad8 -> {
            GameAction.MovePiece(move = PieceMove.Rotate)
        }

        Key.PageDown, Key.CtrlLeft, Key.CtrlRight -> {
            GameAction.MovePiece(move = PieceMove.HardDrop)
        }

        else -> null
    }
}

private fun Key.toRepeatableMove(): PieceMove? {
    return when (this) {
        Key.DirectionLeft, Key.A, Key.NumPad4 -> PieceMove.Left
        Key.DirectionRight, Key.D, Key.NumPad6 -> PieceMove.Right
        Key.DirectionDown, Key.S, Key.NumPad5 -> PieceMove.SoftDrop
        else -> null
    }
}
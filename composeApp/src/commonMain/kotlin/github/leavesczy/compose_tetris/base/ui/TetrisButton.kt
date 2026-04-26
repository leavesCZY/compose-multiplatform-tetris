package github.leavesczy.compose_tetris.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.base.logic.Action
import github.leavesczy.compose_tetris.base.logic.TetrisViewModel
import github.leavesczy.compose_tetris.base.logic.TransformationType
import github.leavesczy.compose_tetris.resources.Res
import github.leavesczy.compose_tetris.resources.pause
import github.leavesczy.compose_tetris.resources.reset
import github.leavesczy.compose_tetris.resources.sound
import github.leavesczy.compose_tetris.resources.start
import org.jetbrains.compose.resources.stringResource

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:04
 * @Desc:
 */
@Composable
fun TetrisButton(
    modifier: Modifier,
    windowSizeClass: WindowSizeClass,
    tetrisViewModel: TetrisViewModel
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            TetrisButtonCompact(
                modifier = modifier,
                tetrisViewModel = tetrisViewModel
            )
        }

        WindowWidthSizeClass.Medium,
        WindowWidthSizeClass.Expanded -> {
            TetrisButtonCompactDesktop(
                modifier = modifier,
                tetrisViewModel = tetrisViewModel
            )
        }
    }
}

@Composable
private fun TetrisButtonCompact(
    modifier: Modifier,
    tetrisViewModel: TetrisViewModel
) {
    val controlButtonSize = 32.dp
    val playButtonSize = 80.dp
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.Top
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.85f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ControlButton(
                modifier = Modifier,
                text = stringResource(resource = Res.string.start),
                size = controlButtonSize,
                onClick = {
                    tetrisViewModel.dispatch(action = Action.Start)
                }
            )
            ControlButton(
                modifier = Modifier,
                text = stringResource(resource = Res.string.pause),
                size = controlButtonSize,
                onClick = {
                    tetrisViewModel.dispatch(action = Action.Pause)
                }
            )
            ControlButton(
                modifier = Modifier,
                text = stringResource(resource = Res.string.reset),
                size = controlButtonSize,
                onClick = {
                    tetrisViewModel.dispatch(action = Action.Reset)
                }
            )
            ControlButton(
                modifier = Modifier,
                text = stringResource(resource = Res.string.sound),
                size = controlButtonSize,
                isEnabled = tetrisViewModel.tetrisViewState.soundEnable,
                onClick = {
                    tetrisViewModel.dispatch(action = Action.Sound)
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.98f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayButton(
                modifier = Modifier,
                icon = Icons.AutoMirrored.Filled.ArrowLeft,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.Left))
            }
            PlayButton(
                modifier = Modifier,
                icon = Icons.AutoMirrored.Filled.RotateRight,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.Rotate))
            }
            PlayButton(
                modifier = Modifier,
                icon = Icons.AutoMirrored.Filled.ArrowRight,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.Right))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.95f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayButton(
                modifier = Modifier,
                icon = Icons.Filled.ArrowDropDown,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.FastDown))
            }
            PlayButton(
                modifier = Modifier
                    .rotate(degrees = 90f),
                icon = Icons.Filled.FastForward,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.Fall))
            }
        }
    }
}

@Composable
private fun TetrisButtonCompactDesktop(
    modifier: Modifier,
    tetrisViewModel: TetrisViewModel
) {
    val controlButtonSize = 30.dp
    val playButtonSize = 70.dp
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.Top
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.75f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ControlButton(
                modifier = Modifier,
                text = stringResource(resource = Res.string.start),
                size = controlButtonSize,
                onClick = {
                    tetrisViewModel.dispatch(action = Action.Start)
                }
            )
            ControlButton(
                modifier = Modifier,
                text = stringResource(resource = Res.string.pause),
                size = controlButtonSize,
                onClick = {
                    tetrisViewModel.dispatch(action = Action.Pause)
                }
            )
            ControlButton(
                modifier = Modifier,
                text = stringResource(resource = Res.string.reset),
                size = controlButtonSize,
                onClick = {
                    tetrisViewModel.dispatch(action = Action.Reset)
                }
            )
            ControlButton(
                modifier = Modifier,
                text = stringResource(resource = Res.string.sound),
                size = controlButtonSize,
                isEnabled = tetrisViewModel.tetrisViewState.soundEnable,
                onClick = {
                    tetrisViewModel.dispatch(action = Action.Sound)
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.80f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayButton(
                modifier = Modifier,
                icon = Icons.AutoMirrored.Filled.ArrowLeft,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.Left))
            }
            PlayButton(
                modifier = Modifier,
                icon = Icons.AutoMirrored.Filled.RotateRight,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.Rotate))
            }
            PlayButton(
                modifier = Modifier,
                icon = Icons.AutoMirrored.Filled.ArrowRight,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.Right))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.80f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayButton(
                modifier = Modifier,
                icon = Icons.Filled.ArrowDropDown,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.FastDown))
            }
            PlayButton(
                modifier = Modifier
                    .rotate(degrees = 90f),
                icon = Icons.Filled.FastForward,
                size = playButtonSize
            ) {
                tetrisViewModel.dispatch(action = Action.Transformation(transformationType = TransformationType.Fall))
            }
        }
    }
}

@Composable
private fun ControlButton(
    modifier: Modifier,
    text: String,
    size: Dp,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.Top
        )
    ) {
        Text(
            modifier = Modifier,
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
        Box(
            modifier = Modifier
                .size(size = size)
                .clip(shape = CircleShape)
                .addShadow(
                    color = if (isEnabled) {
                        ButtonNormalColor
                    } else {
                        ButtonDisabledColor
                    }
                )
                .clickable(onClick = onClick)
        )
    }
}

@Composable
private fun PlayButton(
    modifier: Modifier,
    icon: ImageVector,
    size: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .size(size = size)
                .addShadow(color = ButtonNormalColor)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(size = 40.dp),
                imageVector = icon,
                tint = Color.White,
                contentDescription = null
            )
        }
    }
}

private fun Modifier.addShadow(color: Brush): Modifier {
    return shadow(
        elevation = 6.dp,
        shape = CircleShape
    ).background(brush = color)
}
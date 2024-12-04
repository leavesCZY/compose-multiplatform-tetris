package github.leavesczy.compose_tetris.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.logic.Action
import github.leavesczy.compose_tetris.logic.TetrisLogic
import github.leavesczy.compose_tetris.logic.TransformationType

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 17:10
 * @Desc:
 */
@Composable
fun TetrisButton(tetrisLogic: TetrisLogic) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val controlButtonSize = maxHeight / 10
        val playButtonSize = maxHeight / 4
        val buttonSpace = maxHeight / 4
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.9f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlButton(
                    text = "Start",
                    size = controlButtonSize,
                    onClick = {
                        tetrisLogic.dispatch(action = Action.Start)
                    }
                )
                ControlButton(
                    text = "Pause",
                    size = controlButtonSize,
                    onClick = {
                        tetrisLogic.dispatch(action = Action.Pause)
                    }
                )
                ControlButton(
                    text = "Reset",
                    size = controlButtonSize,
                    onClick = {
                        tetrisLogic.dispatch(action = Action.Reset)
                    }
                )
                SoundButton(
                    size = controlButtonSize,
                    enabled = tetrisLogic.tetrisViewState.soundEnable,
                    onClick = {
                        tetrisLogic.dispatch(action = Action.Sound)
                    }
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 2f)
            )
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(space = buttonSpace),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayButton(
                    icon = Icons.AutoMirrored.Filled.ArrowLeft,
                    size = playButtonSize
                ) {
                    tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.Left))
                }
                PlayButton(
                    icon = Icons.AutoMirrored.Filled.ArrowRight,
                    size = playButtonSize
                ) {
                    tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.Right))
                }
                PlayButton(
                    icon = Icons.AutoMirrored.Filled.RotateRight,
                    size = playButtonSize
                ) {
                    tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.Rotate))
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 2f)
            )
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(space = buttonSpace),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayButton(
                    modifier = Modifier,
                    icon = Icons.Filled.ArrowDropDown,
                    size = playButtonSize
                ) {
                    tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.FastDown))
                }
                PlayButton(
                    modifier = Modifier
                        .rotate(degrees = 90f),
                    icon = Icons.Filled.FastForward,
                    size = playButtonSize
                ) {
                    tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.Fall))
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 2f)
            )
        }
    }
}

@Composable
private fun ControlButton(
    text: String,
    size: Dp,
    color: Brush = ButtonNormalColor,
    colorOnPressed: Brush = ButtonOnPressedColor,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(all = 1.dp),
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val buttonColor = if (isPressed) {
            colorOnPressed
        } else {
            color
        }
        Box(
            modifier = Modifier
                .size(size = size)
                .addShadow(color = buttonColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = onClick
                )
        )
    }
}

@Composable
private fun SoundButton(
    modifier: Modifier = Modifier,
    size: Dp,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(all = 1.dp),
            text = "Sound",
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val buttonColor = if (isPressed) {
            ButtonOnPressedColor
        } else {
            if (enabled) {
                ButtonNormalColor
            } else {
                ButtonDisabledColor
            }
        }
        Box(
            modifier = Modifier
                .size(size = size)
                .addShadow(color = buttonColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = onClick
                )
        )
    }
}

@Composable
private fun PlayButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    size: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val buttonColor = if (isPressed) {
            ButtonOnPressedColor
        } else {
            ButtonNormalColor
        }
        Box(
            modifier = Modifier
                .size(size = size)
                .addShadow(color = buttonColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(size = size / 1.6f),
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
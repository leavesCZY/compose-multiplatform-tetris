package github.leavesczy.compose_tetris.common.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.leavesczy.compose_tetris.common.logic.PlayListener
import github.leavesczy.compose_tetris.common.logic.TetrisViewState
import github.leavesczy.compose_tetris.common.logic.TransformationType.Fall
import github.leavesczy.compose_tetris.common.logic.TransformationType.FastDown
import github.leavesczy.compose_tetris.common.logic.TransformationType.Left
import github.leavesczy.compose_tetris.common.logic.TransformationType.Right
import github.leavesczy.compose_tetris.common.logic.TransformationType.Rotate
import github.leavesczy.compose_tetris.common.ui.theme.ButtonDisenabledColor
import github.leavesczy.compose_tetris.common.ui.theme.ButtonNormalColor
import github.leavesczy.compose_tetris.common.ui.theme.ButtonOnPressedColor

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 17:10
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun TetrisButton(tetrisViewState: TetrisViewState, playListener: PlayListener) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlButton(
                text = "Start",
                onClick = playListener.onStart
            )
            ControlButton(
                text = "Pause",
                onClick = playListener.onPause
            )
            ControlButton(
                text = "Reset",
                onClick = playListener.onReset
            )
            ControlButton(
                text = "Sound",
                color = if (tetrisViewState.soundEnable) {
                    ButtonNormalColor
                } else {
                    ButtonDisenabledColor
                },
                onClick = playListener.onSound
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlayButton(
                icon = Icons.Filled.ArrowLeft
            ) {
                playListener.onTransformation(Left)
            }
            PlayButton(
                icon = Icons.Filled.ArrowRight
            ) {
                playListener.onTransformation(Right)
            }
            PlayButton(
                icon = Icons.Filled.RotateRight
            ) {
                playListener.onTransformation(Rotate)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 36.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            PlayButton(
                modifier = Modifier,
                icon = Icons.Filled.ArrowDropDown
            ) {
                playListener.onTransformation(FastDown)
            }
            PlayButton(
                modifier = Modifier
                    .rotate(degrees = 90f),
                icon = Icons.Filled.FastForward
            ) {
                playListener.onTransformation(Fall)
            }
        }
    }
}

@Composable
private fun ControlButton(
    text: String,
    color: Brush = ButtonNormalColor,
    colorOnPressed: Brush = ButtonOnPressedColor,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(all = 2.dp),
            text = text,
            fontSize = 14.sp,
            fontFamily = FontFamily.Serif,
            color = Color.Black.copy(alpha = 0.8f),
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
                .size(size = 34.dp)
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
    size: Dp = 70.dp,
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
    return shadow(elevation = 3.dp, shape = CircleShape)
        .background(brush = color)
}
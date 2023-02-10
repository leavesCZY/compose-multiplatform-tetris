package github.leavesczy.compose_tetris.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.leavesczy.compose_tetris.common.logic.PlayListener
import github.leavesczy.compose_tetris.common.logic.TetrisState
import github.leavesczy.compose_tetris.common.logic.TransformationType.*
import github.leavesczy.compose_tetris.common.ui.theme.ButtonDisableColor
import github.leavesczy.compose_tetris.common.ui.theme.ButtonEnabledColor

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 17:10
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun TetrisButton(tetrisState: TetrisState, playListener: PlayListener) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ControlButton(
                modifier = Modifier
                    .weight(weight = 1f),
                text = "Start"
            ) {
                playListener.onStart()
            }
            ControlButton(
                modifier = Modifier
                    .weight(weight = 1f),
                text = "Pause"
            ) {
                playListener.onPause()
            }
            ControlButton(
                modifier = Modifier
                    .weight(weight = 1f),
                text = "Reset"
            ) {
                playListener.onReset()
            }
            ControlButton(
                modifier = Modifier
                    .weight(weight = 1f),
                text = "Sound",
                btnColor = if (tetrisState.soundEnable) {
                    ButtonEnabledColor
                } else {
                    ButtonDisableColor
                }
            ) {
                playListener.onSound()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            PlayButton(
                modifier = Modifier.weight(weight = 1f),
                icon = Icons.Filled.ArrowLeft
            ) {
                playListener.onTransformation(Left)
            }
            PlayButton(
                modifier = Modifier.weight(weight = 1f),
                icon = Icons.Filled.ArrowRight
            ) {
                playListener.onTransformation(Right)
            }
            PlayButton(
                modifier = Modifier.weight(weight = 1f),
                icon = Icons.Filled.RotateRight
            ) {
                playListener.onTransformation(Rotate)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PlayButton(
                modifier = Modifier.padding(horizontal = 18.dp),
                icon = Icons.Filled.ArrowDropDown
            ) {
                playListener.onTransformation(FastDown)
            }
            PlayButton(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
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
    modifier: Modifier,
    text: String,
    fontSize: TextUnit = 14.sp,
    btnSize: Dp = 34.dp,
    btnColor: Brush = ButtonEnabledColor,
    onPress: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(all = 2.dp),
            text = text,
            fontSize = fontSize,
            fontFamily = FontFamily.Serif,
            color = Color.Black.copy(alpha = 0.8f),
        )
        Box(
            modifier = Modifier
                .size(width = btnSize, height = btnSize)
                .addShadow(color = btnColor)
                .clickable {
                    onPress()
                }
        )
    }
}

@Composable
private fun PlayButton(
    modifier: Modifier,
    icon: ImageVector,
    size: Dp = 70.dp,
    onPress: () -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        Box(
            modifier = Modifier
                .size(size = size)
                .addShadow(color = ButtonEnabledColor)
                .clickable {
                    onPress()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(size = size / 1.3f),
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

private fun Modifier.addShadow(color: Brush): Modifier {
    return shadow(elevation = 3.dp, shape = CircleShape)
        .background(
            brush = color
        )
}
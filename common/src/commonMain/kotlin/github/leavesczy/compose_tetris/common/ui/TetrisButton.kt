package github.leavesczy.compose_tetris.common.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.leavesczy.compose_tetris.common.ui.theme.ButtonNormalColor
import github.leavesczy.compose_tetris.common.ui.theme.ButtonOnPressedColor

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 17:10
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun ControlButton(
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
            modifier = Modifier.padding(all = 2.dp),
            text = text,
            fontSize = 14.sp,
            color = Color.Black,
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
fun PlayButton(
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

fun Modifier.addShadow(color: Brush): Modifier {
    return shadow(
        elevation = 3.dp,
        shape = CircleShape
    ).background(brush = color)
}
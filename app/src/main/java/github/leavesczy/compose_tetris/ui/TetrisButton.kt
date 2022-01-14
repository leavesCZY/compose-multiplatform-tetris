package github.leavesczy.compose_tetris.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import github.leavesczy.compose_tetris.logic.PlayListener
import github.leavesczy.compose_tetris.logic.TransformationType.*
import github.leavesczy.compose_tetris.logic.combinedPlayListener
import github.leavesczy.compose_tetris.ui.theme.PlayButtonColor
import github.leavesczy.compose_tetris.ui.theme.PlayButtonColor2
import github.leavesczy.compose_tetris.ui.theme.PlayButtonColor3
import github.leavesczy.compose_tetris.ui.theme.PlayButtonShape

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 17:10
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Preview(backgroundColor = 0xffefcc19, showBackground = true)
@Composable
fun TetrisButton(
    playListener: PlayListener = combinedPlayListener()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {
            val controlPadding = 20.dp
            ControlButton(hint = "Start", modifier = Modifier.padding(end = controlPadding)) {
                playListener.onStart()
            }
            ControlButton(
                hint = "Pause",
                modifier = Modifier.padding(start = controlPadding, end = controlPadding)
            ) {
                playListener.onPause()
            }
            ControlButton(
                hint = "Reset",
                modifier = Modifier.padding(start = controlPadding, end = controlPadding)
            ) {
                playListener.onReset()
            }
            ControlButton(hint = "Sound", modifier = Modifier.padding(start = controlPadding)) {
                playListener.onSound()
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            val (leftBtn, rightBtn, fastDownBtn, rotateBtn, fallBtn) = createRefs()
            val innerMargin = 24.dp
            PlayButton(icon = "◀", modifier = Modifier.constrainAs(leftBtn) {
                start.linkTo(anchor = parent.start)
                top.linkTo(anchor = parent.top)
                end.linkTo(anchor = rightBtn.start, margin = innerMargin)
            }) {
                playListener.onTransformation(Left)
            }
            PlayButton(icon = "▶", modifier = Modifier.constrainAs(rightBtn) {
                start.linkTo(anchor = leftBtn.end, margin = innerMargin)
                top.linkTo(anchor = leftBtn.top)
                bottom.linkTo(anchor = leftBtn.bottom)
            }) {
                playListener.onTransformation(Right)
            }
            PlayButton(
                icon = "Rotate",
                fontSize = 18.sp,
                modifier = Modifier.constrainAs(rotateBtn) {
                    top.linkTo(anchor = rightBtn.top)
                    start.linkTo(anchor = rightBtn.end, margin = innerMargin)
                }) {
                playListener.onTransformation(Rotate)
            }
            PlayButton(icon = "▼", modifier = Modifier.constrainAs(fastDownBtn) {
                top.linkTo(anchor = leftBtn.bottom)
                start.linkTo(anchor = leftBtn.start)
                end.linkTo(anchor = rightBtn.end)
            }) {
                playListener.onTransformation(FastDown)
            }
            PlayButton(
                icon = "▼\n▼",
                modifier = Modifier.constrainAs(fallBtn) {
                    top.linkTo(anchor = fastDownBtn.top)
                    start.linkTo(anchor = rightBtn.end)
                    end.linkTo(anchor = rotateBtn.start)
                }) {
                playListener.onTransformation(Fall)
            }
        }
    }

}

private val brush = Brush.linearGradient(
    colors = listOf(
        PlayButtonColor,
        PlayButtonColor2,
        PlayButtonColor3
    )
)

@Composable
fun ControlButton(
    modifier: Modifier,
    hint: String,
    fontSize: TextUnit = 18.sp,
    btnSize: Dp = 46.dp,
    onPress: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(2.dp),
            text = hint,
            fontSize = fontSize,
            color = Color.Black.copy(0.8f),
            maxLines = 1
        )
        Box(
            modifier = Modifier
                .size(width = btnSize, height = btnSize)
                .shadow(elevation = 60.dp, shape = PlayButtonShape)
                .clip(shape = PlayButtonShape)
                .background(
                    brush = brush
                )
                .clickable {
                    onPress()
                }
        )
    }
}

@Composable
fun PlayButton(
    size: Dp = 70.dp, icon: String,
    modifier: Modifier,
    fontSize: TextUnit = 22.sp,
    onPress: () -> Unit
) {
    Box(
        modifier = modifier
            .size(width = size, height = size)
            .shadow(elevation = 60.dp, shape = PlayButtonShape)
            .clip(shape = PlayButtonShape)
            .background(
                brush = brush
            )
            .clickable {
                onPress()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = fontSize,
            color = Color.Black.copy(0.8f)
        )
    }
}
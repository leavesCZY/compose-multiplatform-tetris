package github.leavesc.compose_tetris.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.leavesc.compose_tetris.ui.theme.BodyBackground
import github.leavesc.compose_tetris.ui.theme.BrickAlpha
import github.leavesc.compose_tetris.ui.theme.ScreenBackground

/**
 * @Author: leavesC
 * @Date: 2021/6/6 15:31
 * @Desc:
 * @Github：https://github.com/leavesC
 */
@Preview(widthDp = 400, heightDp = 400, backgroundColor = 0x00000000, showBackground = true)
@Composable
fun PreviewTetrisIcon() {
    val previewWidth = 400.dp
    val screenWidth = 340.dp
    val screenHeight = 200.dp
    val paddingTop = (previewWidth - screenWidth) / 2
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BodyBackground, shape = RoundedCornerShape(16))
            .padding(top = paddingTop, bottom = paddingTop * 2),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(
            modifier = Modifier
                .requiredSize(width = screenWidth, height = screenHeight)
                .background(color = ScreenBackground, shape = RoundedCornerShape(16))
                .clip(shape = RoundedCornerShape(16)),
        ) {
            val width = size.width
            val height = size.height

            val spiritHeight = 10
            val spiritPaddingPx = 1.dp.toPx()
            val brickSize = height / spiritHeight - spiritPaddingPx
            val spiritWidth = (width / (brickSize + spiritPaddingPx)).toInt()

            for (x in 0 until spiritWidth) {
                for (y in 0 until spiritHeight) {
                    translate(
                        left = x * (brickSize + spiritPaddingPx),
                        top = y * (brickSize + spiritPaddingPx)
                    ) {
                        drawBrick(
                            brickSize = brickSize,
                            color = BrickAlpha
                        )
                    }
                }
            }
        }

        Text(
            text = "TETRIS",
            textAlign = TextAlign.Center,
            color = Color.Black.copy(alpha = 0.8f),
            fontSize = 90.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
        ) {
            val controlPadding = 16.dp
            val btnSize = 54.dp
            ControlButton(
                hint = "",
                btnSize = btnSize,
                modifier = Modifier.padding(end = controlPadding)
            )
            ControlButton(
                hint = "",
                btnSize = btnSize,
                modifier = Modifier.padding(
                    start = controlPadding,
                    end = controlPadding
                )
            )
            ControlButton(
                hint = "",
                btnSize = btnSize,
                modifier = Modifier.padding(
                    start = controlPadding,
                    end = controlPadding
                )
            )
            ControlButton(
                hint = "",
                btnSize = btnSize,
                modifier = Modifier.padding(start = controlPadding)
            )
        }
    }
}


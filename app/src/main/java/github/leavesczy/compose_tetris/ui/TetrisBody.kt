package github.leavesczy.compose_tetris.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 17:19
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun TetrisBody(
    tetrisScreen: @Composable (() -> Unit),
    tetrisButton: @Composable (() -> Unit),
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(height = 40.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
                .align(alignment = Alignment.CenterHorizontally)
                .padding(horizontal = 40.dp),
        ) {
            val borderPadding = 8.dp
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawScreenBorder(
                    leftTop = Offset(x = 0f, y = 0f),
                    width = size.width,
                    height = size.height,
                    borderPadding = borderPadding,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = borderPadding)
            ) {
                tetrisScreen()
            }
        }
        Spacer(modifier = Modifier.height(height = 6.dp))
        tetrisButton()
        Spacer(modifier = Modifier.height(height = 30.dp))
    }
}

private fun DrawScope.drawScreenBorder(
    leftTop: Offset,
    width: Float,
    height: Float,
    borderPadding: Dp
) {
    val padding = borderPadding.toPx()
    val leftBottom = Offset(leftTop.x, leftTop.y + height)
    val rightTop = Offset(leftTop.x + width, leftTop.y)
    val rightBottom = Offset(rightTop.x, leftBottom.y)

    val path = Path().apply {
        lineTo(padding, padding)
        lineTo(rightTop.x - padding, padding)
        lineTo(rightTop.x, rightTop.y)
        close()
    }
    drawPath(path, Color.Black.copy(alpha = 0.7f))

    path.apply {
        reset()
        lineTo(padding, padding)
        lineTo(padding, leftBottom.y - padding)
        lineTo(leftBottom.x, leftBottom.y)
        close()
    }
    drawPath(path, Color.Black.copy(alpha = 0.5f))

    path.apply {
        reset()
        moveTo(leftBottom.x, leftBottom.y)
        relativeLineTo(padding, -padding)
        lineTo(rightBottom.x - padding, rightBottom.y - padding)
        lineTo(rightBottom.x, rightBottom.y)
        close()
    }
    drawPath(path, Color.Black.copy(alpha = 0.7f))

    path.apply {
        reset()
        moveTo(rightTop.x, rightTop.y)
        relativeLineTo(-padding, padding)
        lineTo(rightBottom.x - padding, rightBottom.y - padding)
        lineTo(rightBottom.x, rightBottom.y)
        close()
    }
    drawPath(path, Color.Black.copy(alpha = 0.5f))
}
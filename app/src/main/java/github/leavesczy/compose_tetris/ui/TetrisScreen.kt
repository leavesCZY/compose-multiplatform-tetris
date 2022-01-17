package github.leavesczy.compose_tetris.ui

import android.graphics.Paint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.logic.GameStatus
import github.leavesczy.compose_tetris.logic.TetrisState
import github.leavesczy.compose_tetris.ui.theme.BrickAlpha
import github.leavesczy.compose_tetris.ui.theme.BrickFill
import github.leavesczy.compose_tetris.ui.theme.ScreenBackground

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 17:19
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun TetrisScreen(
    tetrisState: TetrisState
) {
    val screenMatrix = tetrisState.screenMatrix
    val matrixHeight = tetrisState.height
    val matrixWidth = tetrisState.width
    val spiritPadding = 2.dp
    val screenPadding = 8.dp
    val animateValue by rememberInfiniteTransition().animateFloat(
        initialValue = 0.8f, targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ScreenBackground)
            .padding(
                start = screenPadding, top = screenPadding,
                end = screenPadding, bottom = screenPadding
            )
    ) {
        val width = size.width
        val height = size.height
        val screenPaddingPx = screenPadding.toPx()
        val spiritPaddingPx = spiritPadding.toPx()
        val brickSize = (height - spiritPaddingPx * (matrixHeight - 1)) / matrixHeight

        kotlin.run {
            screenMatrix.forEachIndexed { y, ints ->
                ints.forEachIndexed { x, isFill ->
                    translate(
                        left = x * (brickSize + spiritPaddingPx),
                        top = y * (brickSize + spiritPaddingPx)
                    ) {
                        drawBrick(
                            brickSize = brickSize,
                            brickColor = if (isFill == 1) BrickFill else BrickAlpha
                        )
                    }
                }
            }
        }

        kotlin.run {
            val borderOffset = screenPaddingPx / 2
            val borderWidth =
                matrixWidth * (brickSize + spiritPaddingPx) + screenPaddingPx - spiritPaddingPx
            val borderHeight =
                matrixHeight * (brickSize + spiritPaddingPx) + screenPaddingPx - spiritPaddingPx
            translate(left = -borderOffset, top = -borderOffset) {
                val path = Path().apply {
                    relativeLineTo(
                        dx = borderWidth,
                        dy = 0f
                    )
                    relativeLineTo(
                        dx = 0f,
                        dy = borderHeight
                    )
                    relativeLineTo(
                        dx = -borderWidth,
                        dy = 0f
                    )
                    close()
                }
                drawPath(
                    path = path, color = Color.Black.copy(alpha = 0.6f),
                    style = Stroke(width = 3f)
                )
            }
        }

        val leftBordOffsetX = matrixWidth * (brickSize + spiritPaddingPx) + screenPaddingPx

        kotlin.run {
            val leftBordWidth = width - leftBordOffsetX
            translate(left = leftBordOffsetX) {
                drawRightPanel(
                    tetrisState = tetrisState,
                    width = leftBordWidth, height = height
                )
            }
        }

        kotlin.run {
            drawHint(
                tetrisState = tetrisState,
                width = leftBordOffsetX, height = height,
                alpha = animateValue
            )
        }
    }
}

fun DrawScope.drawBrick(brickSize: Float, brickColor: Color) {
    drawRect(color = brickColor, size = Size(brickSize, brickSize))
    val strokeWidth = brickSize / 9f
//    translate(left = strokeWidth, top = strokeWidth) {
//        drawRect(
//            color = ScreenBackground,
//            size = Size(
//                width = brickSize - 2 * strokeWidth,
//                height = brickSize - 2 * strokeWidth
//            )
//        )
//    }
    val brickInnerSize = brickSize / 2.0f
    val translateLeft = (brickSize - brickInnerSize) / 2
    translate(left = translateLeft, top = translateLeft) {
        drawRect(
            color = brickColor,
            size = Size(brickInnerSize, brickInnerSize)
        )
    }
}

private fun DrawScope.drawRightPanel(tetrisState: TetrisState, width: Float, height: Float) {
    drawIntoCanvas {
        val textPaint = Paint().apply {
            color = BrickFill.toArgb()
            textSize = 60f
            textAlign = Paint.Align.CENTER
            style = Paint.Style.FILL
            strokeWidth = 12f
            isAntiAlias = true
        }
        it.nativeCanvas.drawText(
            "Next",
            width / 2f,
            height / 8f,
            textPaint
        )
        if (tetrisState.gameStatus == GameStatus.Running || tetrisState.gameStatus == GameStatus.Paused) {
            val brickSize = 15.dp.toPx()
            val spiritPaddingPx = 1.dp.toPx()
            translate(
                left = minOf(brickSize, 50f),
                top = height / 7f
            ) {
                for (location in tetrisState.nextTetris.shape) {
                    val x = location.x.toFloat()
                    val y = location.y.toFloat()
                    translate(
                        left = x * (brickSize + spiritPaddingPx),
                        top = y * (brickSize + spiritPaddingPx)
                    ) {
                        drawBrick(
                            brickSize = brickSize,
                            brickColor = BrickFill
                        )
                    }
                }
            }
        }
    }

}

private fun DrawScope.drawHint(
    tetrisState: TetrisState,
    width: Float, height: Float,
    alpha: Float
) {
    val drawText = { hint: String, fontSize: Float ->
        drawIntoCanvas {
            it.nativeCanvas.drawText(
                hint,
                width / 2,
                height / 3,
                Paint().apply {
                    color = Color.Black.copy(alpha = alpha).toArgb()
                    textSize = fontSize
                    textAlign = Paint.Align.CENTER
                    style = Paint.Style.FILL_AND_STROKE
                    strokeWidth = fontSize / 12
                    isAntiAlias = true
                }
            )
        }
    }
    val unit = when (tetrisState.gameStatus) {
        GameStatus.Welcome -> {
            drawText("TETRIS", 90f)
        }
        GameStatus.Paused -> {
            drawText("PAUSE", 90f)
        }
        GameStatus.GameOver -> {
            drawText("GAME OVER", 66f)
        }
        GameStatus.Running -> {

        }
        GameStatus.ScreenClearing -> {

        }
        GameStatus.LineClearing -> {

        }
    }
}
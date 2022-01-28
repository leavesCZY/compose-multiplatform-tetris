package github.leavesczy.compose_tetris.common.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.common.logic.GameStatus
import github.leavesczy.compose_tetris.common.logic.TetrisState
import github.leavesczy.compose_tetris.common.ui.theme.BrickColorAlpha
import github.leavesczy.compose_tetris.common.ui.theme.BrickColorFill
import github.leavesczy.compose_tetris.platform.drawText

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
    val matrixWidth = tetrisState.width
    val matrixHeight = tetrisState.height
    val brickMarginDp = 2.dp
    val screenInnerMarginDp = 8.dp
    val alphaAnimate by rememberInfiniteTransition().animateFloat(
        initialValue = 1.0f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val bgColor = MaterialTheme.colorScheme.onBackground
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(color = bgColor)
    ) {
        val borderWidthDp = 8.dp
        val borderWidth = borderWidthDp.toPx()

        drawBorder(
            width = size.width,
            height = size.height,
            borderWidth = borderWidth,
        )

        val brickMargin = brickMarginDp.toPx()
        val screenInnerMargin = screenInnerMarginDp.toPx()
        val brickSize = (size.height - 2 * borderWidth - 2 * screenInnerMargin -
                brickMargin * (matrixHeight - 1)) / matrixHeight
        val brickSizeWithMargin = brickSize + brickMargin

        val leftPanelWith =
            matrixWidth * brickSize + (matrixWidth - 1) * brickMargin + screenInnerMargin * 2
        val leftPanelHeight =
            matrixHeight * brickSize + (matrixHeight - 1) * brickMargin + screenInnerMargin * 2

        translate(
            left = borderWidth,
            top = borderWidth
        ) {
            val startPoint = screenInnerMargin / 2f
            val lineWidth =
                screenInnerMargin + brickSize * matrixWidth + brickMargin * (matrixWidth - 1)
            val lineHeight =
                screenInnerMargin + brickSize * matrixHeight + brickMargin * (matrixHeight - 1)
            val path = Path().apply {
                moveTo(x = startPoint, y = startPoint)
                relativeLineTo(
                    dx = lineWidth,
                    dy = 0f
                )
                relativeLineTo(
                    dx = 0f,
                    dy = lineHeight
                )
                relativeLineTo(
                    dx = -lineWidth,
                    dy = 0f
                )
                close()
            }
            drawPath(
                path = path,
                color = Color.Black.copy(alpha = 0.6f),
                style = Stroke(width = 3f)
            )
        }

        translate(
            left = borderWidth + screenInnerMargin,
            top = borderWidth + screenInnerMargin
        ) {
            screenMatrix.forEachIndexed { y, ints ->
                ints.forEachIndexed { x, isFill ->
                    translate(
                        left = x * brickSizeWithMargin,
                        top = y * brickSizeWithMargin
                    ) {
                        drawBrick(
                            bgColor = bgColor,
                            brickSize = brickSize,
                            brickColor = if (isFill == 1) {
                                BrickColorFill
                            } else {
                                BrickColorAlpha
                            }
                        )
                    }
                }
            }
        }

        translate(
            left = borderWidth,
            top = borderWidth
        ) {
            drawText(
                tetrisState = tetrisState,
                width = leftPanelWith,
                height = leftPanelHeight,
                alpha = alphaAnimate
            )
        }

        translate(
            left = borderWidth + leftPanelWith - screenInnerMargin / 2,
            top = borderWidth + screenInnerMargin
        ) {
            drawRightPanel(
                bgColor = bgColor,
                tetrisState = tetrisState,
                width = size.width - 2 * borderWidth - leftPanelWith + screenInnerMargin / 2,
                height = size.height - 2 * screenInnerMargin - 2 * borderWidth
            )
        }
    }
}

private fun DrawScope.drawBorder(
    width: Float,
    height: Float,
    borderWidth: Float
) {
    val leftBottom = Offset(0f, height)
    val rightTop = Offset(width, 0f)
    val rightBottom = Offset(width, height)

    val path = Path().apply {
        lineTo(borderWidth, borderWidth)
        lineTo(rightTop.x - borderWidth, borderWidth)
        lineTo(rightTop.x, rightTop.y)
        close()
    }
    drawPath(path, Color.Black.copy(alpha = 0.7f))

    path.apply {
        reset()
        lineTo(borderWidth, borderWidth)
        lineTo(borderWidth, leftBottom.y - borderWidth)
        lineTo(leftBottom.x, leftBottom.y)
        close()
    }
    drawPath(path, Color.Black.copy(alpha = 0.5f))

    path.apply {
        reset()
        moveTo(leftBottom.x, leftBottom.y)
        relativeLineTo(borderWidth, -borderWidth)
        lineTo(rightBottom.x - borderWidth, rightBottom.y - borderWidth)
        lineTo(rightBottom.x, rightBottom.y)
        close()
    }
    drawPath(path, Color.Black.copy(alpha = 0.7f))

    path.apply {
        reset()
        moveTo(rightTop.x, rightTop.y)
        relativeLineTo(-borderWidth, borderWidth)
        lineTo(rightBottom.x - borderWidth, rightBottom.y - borderWidth)
        lineTo(rightBottom.x, rightBottom.y)
        close()
    }
    drawPath(path, Color.Black.copy(alpha = 0.5f))
}

private fun DrawScope.drawRightPanel(
    bgColor: Color,
    tetrisState: TetrisState,
    width: Float,
    height: Float
) {
    if (tetrisState.gameStatus == GameStatus.Running || tetrisState.gameStatus == GameStatus.Paused) {
        val nextTetrisShape = tetrisState.nextTetris.shape
        val shapeMaxWidth = nextTetrisShape.map { it.x }.toSet().size
        val brickSize = 15.dp.toPx()
        val brickMargin = 1.dp.toPx()
        val brickSizeWithMargin = brickSize + brickMargin
        translate(
            left = (width - brickSize * shapeMaxWidth + brickMargin * (shapeMaxWidth - 1)) / 2f,
            top = height / 6.5f
        ) {
            for (location in nextTetrisShape) {
                translate(
                    left = location.x * brickSizeWithMargin,
                    top = location.y * brickSizeWithMargin
                ) {
                    drawBrick(
                        bgColor = bgColor,
                        brickSize = brickSize,
                        brickColor = BrickColorFill
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawBrick(bgColor: Color, brickSize: Float, brickColor: Color) {
    drawRect(color = brickColor, size = Size(width = brickSize, height = brickSize))
    val strokeWidth = brickSize / 9f
    translate(left = strokeWidth, top = strokeWidth) {
        drawRect(
            color = bgColor,
            size = Size(
                width = brickSize - 2 * strokeWidth,
                height = brickSize - 2 * strokeWidth
            )
        )
    }
    val brickInnerSize = brickSize / 2.0f
    val translateLeft = (brickSize - brickInnerSize) / 2
    translate(left = translateLeft, top = translateLeft) {
        drawRect(
            color = brickColor,
            size = Size(width = brickInnerSize, height = brickInnerSize)
        )
    }
}

private fun DrawScope.drawText(
    tetrisState: TetrisState,
    width: Float,
    height: Float,
    alpha: Float
) {
    val drawText = { text: String, fontSize: Float ->
        val canvas = drawContext.canvas
        canvas.drawText(
            text = text,
            color = Color.Black.copy(alpha = alpha).toArgb(),
            fontSize = fontSize,
            strokeWidth = 10f,
            x = width,
            y = height
        )
    }
    return when (tetrisState.gameStatus) {
        GameStatus.Welcome -> {
            drawText("TETRIS", 130f)
        }
        GameStatus.Paused -> {
            drawText("PAUSE", 130f)
        }
        GameStatus.GameOver -> {
            drawText("GAME OVER", 100f)
        }
        GameStatus.Running -> {

        }
        GameStatus.ScreenClearing -> {

        }
        GameStatus.LineClearing -> {

        }
    }
}
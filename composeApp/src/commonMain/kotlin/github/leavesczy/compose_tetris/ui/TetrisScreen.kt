package github.leavesczy.compose_tetris.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.leavesczy.compose_tetris.getFontSize
import github.leavesczy.compose_tetris.logic.Action
import github.leavesczy.compose_tetris.logic.GameStatus
import github.leavesczy.compose_tetris.logic.TetrisLogic
import github.leavesczy.compose_tetris.logic.TetrisViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 17:19
 * @Desc:
 */
@Composable
internal fun TetrisPage(
    modifier: Modifier,
    tetrisLogic: TetrisLogic
) {
    LaunchedEffect(key1 = Unit) {
        tetrisLogic.dispatch(action = Action.Welcome)
    }
    TetrisTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .then(other = modifier),
            contentWindowInsets = WindowInsets(
                left = 0.dp,
                top = 0.dp,
                right = 0.dp,
                bottom = 0.dp
            ),
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .padding(top = 10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TetrisPage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 11f)
                        .padding(horizontal = 30.dp),
                    tetrisViewState = tetrisLogic.tetrisViewState
                )
                TetrisButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 5f),
                    tetrisLogic = tetrisLogic
                )
            }
        }
    }
}

@Composable
private fun TetrisPage(
    modifier: Modifier,
    tetrisViewState: TetrisViewState
) {
    val screenMatrix = tetrisViewState.screenMatrix
    val matrixWidth = tetrisViewState.width
    val matrixHeight = tetrisViewState.height
    val brickMarginDp = 2.dp
    val screenInnerMarginDp = 8.dp
    val alphaAnimate = remember {
        Animatable(initialValue = 1f)
    }
    LaunchedEffect(key1 = Unit) {
        withContext(context = Dispatchers.Default) {
            var targetValue = 0f
            while (true) {
                alphaAnimate.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = LinearEasing
                    )
                )
                targetValue = if (targetValue == 0f) {
                    1f
                } else {
                    0f
                }
            }
        }
    }
    val backgroundColor = MaterialTheme.colorScheme.background
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(color = onBackgroundColor)
    ) {
        val borderWidthDp = 8.dp
        val borderWidth = borderWidthDp.toPx()
        drawBorder(
            width = size.width,
            height = size.height,
            borderWidth = borderWidth
        )
        val brickMargin = brickMarginDp.toPx()
        val screenInnerMargin = screenInnerMarginDp.toPx()
        val brickSize =
            (size.height - 2 * borderWidth - 2 * screenInnerMargin - brickMargin * (matrixHeight - 1)) / matrixHeight
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
                            bgColor = onBackgroundColor,
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
            val text = when (tetrisViewState.gameStatus) {
                GameStatus.Welcome -> {
                    "TETRIS"
                }

                GameStatus.Paused -> {
                    "PAUSE"
                }

                GameStatus.GameOver -> {
                    "GAME OVER"
                }

                GameStatus.Running, GameStatus.LineClearing, GameStatus.ScreenClearing -> {
                    ""
                }
            }
            if (text.isNotBlank()) {
                val fontSize = getFontSize(gameStatus = tetrisViewState.gameStatus)
                val textLayoutResult = textMeasurer.measure(
                    text = text,
                    style = TextStyle(
                        fontSize = fontSize.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                )
                drawText(
                    textLayoutResult = textLayoutResult,
                    color = backgroundColor,
                    alpha = alphaAnimate.value,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = alphaAnimate.value),
                        offset = Offset(x = 14.0f, y = 14.0f),
                        blurRadius = 8f
                    ),
                    topLeft = Offset(
                        x = (leftPanelWith - textLayoutResult.size.width) / 2,
                        y = (leftPanelHeight - textLayoutResult.size.height) / 2
                    )
                )
            }
        }
        translate(
            left = borderWidth + leftPanelWith - screenInnerMargin / 2,
            top = borderWidth + screenInnerMargin
        ) {
            drawRightPanel(
                bgColor = onBackgroundColor,
                tetrisViewState = tetrisViewState,
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
    tetrisViewState: TetrisViewState,
    width: Float,
    height: Float
) {
    if (tetrisViewState.gameStatus == GameStatus.Running || tetrisViewState.gameStatus == GameStatus.Paused) {
        val nextTetrisShape = tetrisViewState.nextTetris.shape
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
            size = Size(
                width = brickInnerSize,
                height = brickInnerSize
            )
        )
    }
}
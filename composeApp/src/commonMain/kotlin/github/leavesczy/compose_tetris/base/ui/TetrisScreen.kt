package github.leavesczy.compose_tetris.base.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import github.leavesczy.compose_tetris.base.logic.Action
import github.leavesczy.compose_tetris.base.logic.GameStatus
import github.leavesczy.compose_tetris.base.logic.TetrisViewModel
import github.leavesczy.compose_tetris.base.logic.TetrisViewState
import github.leavesczy.compose_tetris.getFontSize
import github.leavesczy.compose_tetris.resources.Res
import github.leavesczy.compose_tetris.resources.game_over
import github.leavesczy.compose_tetris.resources.pause_capital_letter
import github.leavesczy.compose_tetris.resources.tetris
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:03
 * @Desc:
 */
@Composable
fun TetrisPage(
    modifier: Modifier,
    windowSizeClass: WindowSizeClass,
    tetrisViewModel: TetrisViewModel
) {
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 200L)
        tetrisViewModel.dispatch(action = Action.Welcome)
    }
    TetrisTheme {
        Scaffold(
            modifier = modifier
                .fillMaxSize(),
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(weight = 1f)
                )
                TetrisPage(
                    modifier = Modifier
                        .weight(weight = 26f)
                        .padding(horizontal = 30.dp)
                        .fillMaxWidth(),
                    tetrisViewState = tetrisViewModel.tetrisViewState
                )
                Spacer(
                    modifier = Modifier
                        .weight(weight = 1f)
                )
                TetrisButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    windowSizeClass = windowSizeClass,
                    tetrisViewModel = tetrisViewModel
                )
                Spacer(
                    modifier = Modifier
                        .weight(weight = 1f)
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
    val backgroundColor = MaterialTheme.colorScheme.background
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val gameStatus by rememberUpdatedState(newValue = tetrisViewState.gameStatus)
    var hintText by remember {
        mutableStateOf(value = "")
    }
    val textAlphaAnimate = remember {
        Animatable(initialValue = 1f)
    }
    val textMeasurer = rememberTextMeasurer()
    val tetrisText = stringResource(resource = Res.string.tetris)
    val pauseText = stringResource(resource = Res.string.pause_capital_letter)
    val gameOverText = stringResource(resource = Res.string.game_over)
    LaunchedEffect(key1 = Unit) {
        launch {
            snapshotFlow {
                gameStatus
            }.collect {
                hintText = when (it) {
                    GameStatus.Welcome -> {
                        tetrisText
                    }

                    GameStatus.Paused -> {
                        pauseText
                    }

                    GameStatus.GameOver -> {
                        gameOverText
                    }

                    GameStatus.Running,
                    GameStatus.LineClearing,
                    GameStatus.ScreenClearing -> {
                        ""
                    }
                }
            }
        }
        launch {
            snapshotFlow {
                hintText
            }.collectLatest {
                if (it.isNotBlank()) {
                    textAlphaAnimate.snapTo(targetValue = 0.85f)
                    var targetValue = 0f
                    while (true) {
                        textAlphaAnimate.animateTo(
                            targetValue = targetValue,
                            animationSpec = tween(
                                durationMillis = 1600,
                                easing = LinearEasing
                            )
                        )
                        targetValue = if (targetValue == 0f) {
                            0.85f
                        } else {
                            0f
                        }
                    }
                }
            }
        }
    }
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
            if (hintText.isNotBlank()) {
                val fontSize = getFontSize(gameStatus = tetrisViewState.gameStatus)
                val textLayoutResult = textMeasurer.measure(
                    text = hintText,
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
                    alpha = textAlphaAnimate.value,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = textAlphaAnimate.value),
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
    val leftBottom = Offset(x = 0f, y = height)
    val rightTop = Offset(x = width, y = 0f)
    val rightBottom = Offset(x = width, y = height)
    val path = Path().apply {
        lineTo(x = borderWidth, y = borderWidth)
        lineTo(x = rightTop.x - borderWidth, y = borderWidth)
        lineTo(x = rightTop.x, y = rightTop.y)
        close()
    }
    drawPath(
        path = path,
        color = Color.Black.copy(alpha = 0.7f)
    )
    path.apply {
        reset()
        lineTo(x = borderWidth, y = borderWidth)
        lineTo(x = borderWidth, y = leftBottom.y - borderWidth)
        lineTo(x = leftBottom.x, y = leftBottom.y)
        close()
    }
    drawPath(
        path = path,
        color = Color.Black.copy(alpha = 0.5f)
    )
    path.apply {
        reset()
        moveTo(x = leftBottom.x, y = leftBottom.y)
        relativeLineTo(dx = borderWidth, dy = -borderWidth)
        lineTo(x = rightBottom.x - borderWidth, y = rightBottom.y - borderWidth)
        lineTo(x = rightBottom.x, y = rightBottom.y)
        close()
    }
    drawPath(
        path = path,
        color = Color.Black.copy(alpha = 0.7f)
    )
    path.apply {
        reset()
        moveTo(x = rightTop.x, y = rightTop.y)
        relativeLineTo(dx = -borderWidth, dy = borderWidth)
        lineTo(x = rightBottom.x - borderWidth, y = rightBottom.y - borderWidth)
        lineTo(x = rightBottom.x, y = rightBottom.y)
        close()
    }
    drawPath(
        path = path,
        color = Color.Black.copy(alpha = 0.5f)
    )
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
    drawRect(
        color = brickColor,
        size = Size(
            width = brickSize,
            height = brickSize
        )
    )
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
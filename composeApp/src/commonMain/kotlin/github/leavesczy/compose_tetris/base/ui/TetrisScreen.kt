package github.leavesczy.compose_tetris.base.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.leavesczy.compose_tetris.base.logic.CellFilled
import github.leavesczy.compose_tetris.base.logic.GameAction
import github.leavesczy.compose_tetris.base.logic.GameState
import github.leavesczy.compose_tetris.base.logic.GameStatus
import github.leavesczy.compose_tetris.base.logic.TetrisViewModel
import github.leavesczy.compose_tetris.base.logic.Tetromino
import github.leavesczy.compose_tetris.base.logic.boardCellAt
import github.leavesczy.compose_tetris.forcePortraitGameLayout
import github.leavesczy.compose_tetris.resources.Res
import github.leavesczy.compose_tetris.resources.game_over
import github.leavesczy.compose_tetris.resources.label_high
import github.leavesczy.compose_tetris.resources.label_hold
import github.leavesczy.compose_tetris.resources.label_level
import github.leavesczy.compose_tetris.resources.label_lines
import github.leavesczy.compose_tetris.resources.label_next
import github.leavesczy.compose_tetris.resources.label_score
import github.leavesczy.compose_tetris.resources.pause_capital_letter
import github.leavesczy.compose_tetris.resources.tetris
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun TetrisPage(
    modifier: Modifier,
    windowSizeClass: WindowSizeClass,
    viewModel: TetrisViewModel
) {
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 200L)
        viewModel.dispatch(action = GameAction.Welcome)
    }
    val useLandscapeLayout = shouldUseLandscapeLayout(windowSizeClass = windowSizeClass)
    val soundEnabled = viewModel.gameState.soundEnabled
    TetrisTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(brush = ShellGradient)
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(insets = WindowInsets.safeDrawing),
                containerColor = Color.Transparent
            ) { innerPadding ->
                val contentModifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize()
                if (useLandscapeLayout) {
                    LandscapeGameLayout(
                        modifier = contentModifier,
                        gameState = viewModel.gameState,
                        soundEnabled = soundEnabled,
                        onAction = viewModel::dispatch
                    )
                } else {
                    PortraitGameLayout(
                        modifier = contentModifier,
                        gameState = viewModel.gameState,
                        soundEnabled = soundEnabled,
                        onAction = viewModel::dispatch
                    )
                }
            }
        }
    }
}

private fun shouldUseLandscapeLayout(windowSizeClass: WindowSizeClass): Boolean {
    if (forcePortraitGameLayout) {
        return false
    }
    val wide = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    val short = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    return wide || short
}

@Composable
private fun PortraitGameLayout(
    modifier: Modifier,
    gameState: GameState,
    soundEnabled: Boolean,
    onAction: (GameAction) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        GameBoard(
            modifier = Modifier
                .weight(weight = 1f)
                .fillMaxWidth()
                .padding(horizontal = PanelHorizontalPadding, vertical = 10.dp),
            gameState = gameState
        )
        GamePad(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PanelHorizontalPadding, vertical = 6.dp),
            landscape = false,
            soundEnabled = soundEnabled,
            onAction = onAction
        )
    }
}

@Composable
private fun LandscapeGameLayout(
    modifier: Modifier,
    gameState: GameState,
    soundEnabled: Boolean,
    onAction: (GameAction) -> Unit
) {
    Row(
        modifier = modifier.padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        GameBoard(
            modifier = Modifier
                .weight(weight = 0.68f)
                .fillMaxHeight(),
            gameState = gameState
        )
        GamePad(
            modifier = Modifier
                .weight(weight = 0.32f)
                .fillMaxHeight()
                .widthIn(min = 200.dp, max = 340.dp),
            landscape = true,
            soundEnabled = soundEnabled,
            onAction = onAction
        )
    }
}

@Composable
private fun GameBoard(
    modifier: Modifier,
    gameState: GameState
) {
    val boardWidth = gameState.width
    val boardHeight = gameState.height
    val boardColor = LcdBoard
    val density = LocalDensity.current
    val gameStatus by rememberUpdatedState(newValue = gameState.gameStatus)
    var overlayText by remember { mutableStateOf(value = "") }
    val overlayAlpha = remember { Animatable(initialValue = 1f) }
    val textMeasurer = rememberTextMeasurer()
    val welcomeText = stringResource(resource = Res.string.tetris)
    val pauseText = stringResource(resource = Res.string.pause_capital_letter)
    val gameOverText = stringResource(resource = Res.string.game_over)
    val holdLabel = stringResource(resource = Res.string.label_hold)
    val nextLabel = stringResource(resource = Res.string.label_next)
    val scoreLabel = stringResource(resource = Res.string.label_score)
    val linesLabel = stringResource(resource = Res.string.label_lines)
    val levelLabel = stringResource(resource = Res.string.label_level)
    val highLabel = stringResource(resource = Res.string.label_high)
    LaunchedEffect(key1 = Unit) {
        launch {
            snapshotFlow { gameStatus }.collect { status ->
                overlayText = when (status) {
                    GameStatus.Welcome -> welcomeText
                    GameStatus.Paused -> pauseText
                    GameStatus.GameOver -> gameOverText
                    GameStatus.Running,
                    GameStatus.LineClearing,
                    GameStatus.ScreenClearing -> ""
                }
            }
        }
        launch {
            snapshotFlow { overlayText }.collectLatest { text ->
                if (text.isNotBlank()) {
                    overlayAlpha.snapTo(targetValue = 0.9f)
                    var targetValue = 0.15f
                    while (true) {
                        overlayAlpha.animateTo(
                            targetValue = targetValue,
                            animationSpec = tween(durationMillis = 1400, easing = LinearEasing)
                        )
                        targetValue = if (targetValue < 0.5f) 0.9f else 0.15f
                    }
                }
            }
        }
    }
    val cornerRadiusPx = with(receiver = density) { PanelCornerRadius.toPx() }
    Box(
        modifier = modifier
            .fillMaxSize()
            .shadow(elevation = 1.dp, shape = PanelShape, clip = false)
            .clip(shape = PanelShape)
            .background(color = boardColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val densityScale = density.density
            val layout = computePlayfieldLayout(
                canvasWidth = size.width,
                canvasHeight = size.height,
                columns = boardWidth,
                rows = boardHeight,
                densityScale = densityScale
            )
            drawRoundedPanelChrome(
                left = layout.panelLeft,
                top = layout.panelTop,
                width = layout.panelWidth,
                height = layout.panelHeight,
                borderWidth = layout.borderWidth,
                cornerRadius = cornerRadiusPx
            )
            // Divider between playfield and HUD.
            val dividerX = layout.playfieldLeft + layout.playfieldWidth +
                    (layout.sidePanelLeft - layout.playfieldLeft - layout.playfieldWidth) / 2f
            val dividerInset = layout.innerMargin + (cornerRadiusPx * 0.15f)
            drawLine(
                color = LcdInk.copy(alpha = 0.18f),
                start = Offset(x = dividerX, y = layout.playfieldTop + dividerInset),
                end = Offset(
                    x = dividerX,
                    y = layout.playfieldTop + layout.playfieldHeight - dividerInset
                ),
                strokeWidth = (1.5f * densityScale).coerceAtLeast(1f)
            )
            val playfieldFrameInset = layout.innerMargin / 2f
            val playfieldCorner = (cornerRadiusPx * 0.45f).coerceAtLeast(6f * densityScale)
            drawRoundRect(
                color = LcdInk.copy(alpha = 0.14f),
                topLeft = Offset(
                    x = layout.playfieldLeft + playfieldFrameInset,
                    y = layout.playfieldTop + playfieldFrameInset
                ),
                size = Size(
                    width = layout.playfieldWidth - 2f * playfieldFrameInset,
                    height = layout.playfieldHeight - 2f * playfieldFrameInset
                ),
                cornerRadius = CornerRadius(x = playfieldCorner, y = playfieldCorner),
                style = Stroke(width = (2f * densityScale).coerceAtLeast(1.2f))
            )
            translate(
                left = layout.playfieldLeft + layout.innerMargin,
                top = layout.playfieldTop + layout.innerMargin
            ) {
                drawPlayfield(
                    gameState = gameState,
                    cellSize = layout.cellSize,
                    cellStride = layout.cellStride,
                    boardColor = boardColor
                )
            }
            if (overlayText.isNotBlank()) {
                val fontSizeSp = with(receiver = density) { layout.overlayFontSizePx.toSp() }
                val textLayoutResult = textMeasurer.measure(
                    text = overlayText,
                    style = TextStyle(
                        fontSize = fontSizeSp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 2.sp
                    )
                )
                drawText(
                    textLayoutResult = textLayoutResult,
                    color = LcdInk,
                    alpha = overlayAlpha.value,
                    shadow = Shadow(
                        color = LcdInk.copy(alpha = overlayAlpha.value * 0.55f),
                        offset = Offset(x = 3f * densityScale, y = 4f * densityScale),
                        blurRadius = 2f * densityScale
                    ),
                    topLeft = Offset(
                        x = layout.playfieldLeft +
                                (layout.playfieldWidth - textLayoutResult.size.width) / 2f,
                        y = layout.playfieldTop +
                                (layout.playfieldHeight - textLayoutResult.size.height) / 2f
                    )
                )
            }
            drawSideHud(
                boardColor = boardColor,
                gameState = gameState,
                left = layout.sidePanelLeft,
                top = layout.sidePanelTop,
                width = layout.sidePanelWidth,
                height = layout.sidePanelHeight,
                density = density,
                textMeasurer = textMeasurer,
                holdLabel = holdLabel,
                nextLabel = nextLabel,
                scoreLabel = scoreLabel,
                linesLabel = linesLabel,
                levelLabel = levelLabel,
                highLabel = highLabel
            )
            if (gameState.scorePopup > 0 && gameState.gameStatus == GameStatus.LineClearing) {
                val popupText = "+${gameState.scorePopup}"
                val popupLayout = textMeasurer.measure(
                    text = popupText,
                    style = TextStyle(
                        fontSize = with(receiver = density) { (layout.overlayFontSizePx * 0.42f).toSp() },
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                )
                drawText(
                    textLayoutResult = popupLayout,
                    color = LcdInk,
                    shadow = Shadow(
                        color = LcdInk.copy(alpha = 0.45f),
                        offset = Offset(x = 2f * densityScale, y = 2f * densityScale),
                        blurRadius = 1.5f * densityScale
                    ),
                    topLeft = Offset(
                        x = layout.playfieldLeft +
                                (layout.playfieldWidth - popupLayout.size.width) / 2f,
                        y = layout.playfieldTop + layout.playfieldHeight * 0.18f
                    )
                )
            }
        }
    }
}

private fun DrawScope.drawPlayfield(
    gameState: GameState,
    cellSize: Float,
    cellStride: Float,
    boardColor: Color
) {
    val width = gameState.width
    val height = gameState.height
    val clearingLines = gameState.clearingLines
    val clearingVisible = gameState.clearingLinesVisible
    for (y in 0 until height) {
        val isClearingLine = y in clearingLines
        for (x in 0 until width) {
            val value = gameState.boardCellAt(x = x, y = y)
            val cellColor = when {
                isClearingLine && clearingVisible -> CellColorClearing
                isClearingLine -> CellColorEmpty
                value == CellFilled -> CellColorFilled
                else -> CellColorEmpty
            }
            drawCell(
                left = x * cellStride,
                top = y * cellStride,
                boardColor = boardColor,
                cellSize = cellSize,
                cellColor = cellColor
            )
        }
    }
    if (!gameState.showActivePiece) {
        return
    }
    val activeOffset = gameState.activePiece.offset
    for (cell in gameState.activePiece.cells) {
        val realX = cell.x + activeOffset.x
        val realY = cell.y + activeOffset.y
        if (realX !in 0 until width || realY !in 0 until height) {
            continue
        }
        drawCell(
            left = realX * cellStride,
            top = realY * cellStride,
            boardColor = boardColor,
            cellSize = cellSize,
            cellColor = CellColorFilled
        )
    }
}

private fun DrawScope.drawRoundedPanelChrome(
    left: Float,
    top: Float,
    width: Float,
    height: Float,
    borderWidth: Float,
    cornerRadius: Float
) {
    val outerCorner = CornerRadius(x = cornerRadius, y = cornerRadius)
    val stroke = (borderWidth * 0.55f).coerceAtLeast(1.2f)
    // Light outer rim — follows the same round corners as the clipped plate.
    drawRoundRect(
        color = PanelRim,
        topLeft = Offset(x = left + stroke / 2f, y = top + stroke / 2f),
        size = Size(width = width - stroke, height = height - stroke),
        cornerRadius = outerCorner,
        style = Stroke(width = stroke)
    )
    val inset = borderWidth
    val innerCorner = (cornerRadius - inset).coerceAtLeast(0f)
    drawRoundRect(
        color = LcdInk.copy(alpha = 0.12f),
        topLeft = Offset(x = left + inset, y = top + inset),
        size = Size(width = width - 2f * inset, height = height - 2f * inset),
        cornerRadius = CornerRadius(x = innerCorner, y = innerCorner),
        style = Stroke(width = (stroke * 0.75f).coerceAtLeast(1f))
    )
}

private fun DrawScope.drawSideHud(
    boardColor: Color,
    gameState: GameState,
    left: Float,
    top: Float,
    width: Float,
    height: Float,
    density: Density,
    textMeasurer: TextMeasurer,
    holdLabel: String,
    nextLabel: String,
    scoreLabel: String,
    linesLabel: String,
    levelLabel: String,
    highLabel: String
) {
    if (!gameState.showSideHud || width <= 0f || height <= 0f) {
        return
    }
    val densityScale = density.density
    val labelStyle = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.SansSerif,
        textAlign = TextAlign.Center,
        letterSpacing = 0.8.sp
    )
    val valueStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        textAlign = TextAlign.Center,
        letterSpacing = 0.4.sp
    )

    fun drawCenteredLabel(text: String, centerY: Float, style: TextStyle, color: Color) {
        val layout = textMeasurer.measure(text = text, style = style)
        drawText(
            textLayoutResult = layout,
            color = color,
            topLeft = Offset(
                x = left + (width - layout.size.width) / 2f,
                y = centerY - layout.size.height / 2f
            )
        )
    }

    val pad = height * 0.04f
    // Three bands: Hold / Stats / Next — evenly stacked inside the side column.
    val band = (height - 2f * pad) / 3f
    val holdTop = top + pad
    val statsTop = holdTop + band
    val nextTop = statsTop + band

    drawCenteredLabel(
        text = holdLabel,
        centerY = holdTop + band * 0.18f,
        style = labelStyle,
        color = LcdInkMuted
    )
    gameState.holdPiece?.let { held ->
        drawMiniPiece(
            boardColor = boardColor,
            piece = held,
            left = left,
            top = holdTop + band * 0.30f,
            width = width,
            areaHeight = band * 0.62f,
            densityScale = densityScale
        )
    }

    val stats = listOf(
        scoreLabel to gameState.displayScore.toString(),
        linesLabel to gameState.displayLines.toString(),
        levelLabel to gameState.displayLevel.toString(),
        highLabel to gameState.displayHighScore.toString()
    )
    val statRow = band / stats.size
    stats.forEachIndexed { index, (label, value) ->
        val rowTop = statsTop + index * statRow
        drawCenteredLabel(
            text = label,
            centerY = rowTop + statRow * 0.32f,
            style = labelStyle,
            color = LcdInkMuted
        )
        drawCenteredLabel(
            text = value,
            centerY = rowTop + statRow * 0.72f,
            style = valueStyle,
            color = LcdInk
        )
    }

    drawCenteredLabel(
        text = nextLabel,
        centerY = nextTop + band * 0.18f,
        style = labelStyle,
        color = LcdInkMuted
    )
    drawMiniPiece(
        boardColor = boardColor,
        piece = gameState.nextPiece,
        left = left,
        top = nextTop + band * 0.30f,
        width = width,
        areaHeight = band * 0.62f,
        densityScale = densityScale
    )
}

private fun DrawScope.drawMiniPiece(
    boardColor: Color,
    piece: Tetromino,
    left: Float,
    top: Float,
    width: Float,
    areaHeight: Float,
    densityScale: Float
) {
    val cells = piece.cells
    val minX = cells.minOf { it.x }
    val maxX = cells.maxOf { it.x }
    val minY = cells.minOf { it.y }
    val maxY = cells.maxOf { it.y }
    val shapeWidth = maxX - minX + 1
    val shapeHeight = maxY - minY + 1
    val cellMargin = 1.5f * densityScale
    val maxCellByWidth = (width * 0.72f - cellMargin * (shapeWidth - 1)) / shapeWidth
    val maxCellByHeight = (areaHeight - cellMargin * (shapeHeight - 1)) / shapeHeight
    val cellSize = minOf(maxCellByWidth, maxCellByHeight, 16f * densityScale).coerceAtLeast(6f)
    val cellStride = cellSize + cellMargin
    val blockWidth = shapeWidth * cellSize + (shapeWidth - 1) * cellMargin
    val blockHeight = shapeHeight * cellSize + (shapeHeight - 1) * cellMargin
    val originX = left + (width - blockWidth) / 2f
    val originY = top + (areaHeight - blockHeight) / 2f
    for (cell in cells) {
        drawCell(
            left = originX + (cell.x - minX) * cellStride,
            top = originY + (cell.y - minY) * cellStride,
            boardColor = boardColor,
            cellSize = cellSize,
            cellColor = CellColorFilled
        )
    }
}

private fun DrawScope.drawCell(
    left: Float,
    top: Float,
    boardColor: Color,
    cellSize: Float,
    cellColor: Color
) {
    // Nested cell: outer fill / board ring / inner fill.
    val strokeWidth = cellSize / 9f
    val innerSize = cellSize / 2f
    val innerOffset = (cellSize - innerSize) / 2f
    drawRect(
        color = cellColor,
        topLeft = Offset(x = left, y = top),
        size = Size(width = cellSize, height = cellSize)
    )
    drawRect(
        color = boardColor,
        topLeft = Offset(x = left + strokeWidth, y = top + strokeWidth),
        size = Size(width = cellSize - 2f * strokeWidth, height = cellSize - 2f * strokeWidth)
    )
    drawRect(
        color = cellColor,
        topLeft = Offset(x = left + innerOffset, y = top + innerOffset),
        size = Size(width = innerSize, height = innerSize)
    )
}
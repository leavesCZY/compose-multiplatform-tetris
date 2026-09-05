package github.leavesczy.compose_tetris.base.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

internal data class PlayfieldLayout(
    val cellSize: Float,
    val cellStride: Float,
    val cellMargin: Float,
    val borderWidth: Float,
    val innerMargin: Float,
    val panelLeft: Float,
    val panelTop: Float,
    val panelWidth: Float,
    val panelHeight: Float,
    val playfieldLeft: Float,
    val playfieldTop: Float,
    val playfieldWidth: Float,
    val playfieldHeight: Float,
    val sidePanelLeft: Float,
    val sidePanelTop: Float,
    val sidePanelWidth: Float,
    val sidePanelHeight: Float,
    val overlayFontSizePx: Float
)

internal fun computePlayfieldLayout(
    canvasWidth: Float,
    canvasHeight: Float,
    columns: Int,
    rows: Int,
    densityScale: Float
): PlayfieldLayout {
    val borderWidth = min(7f * densityScale, min(canvasWidth, canvasHeight) * 0.022f)
    val innerMargin = min(5f * densityScale, min(canvasWidth, canvasHeight) * 0.014f)
    val cellMargin = min(2f * densityScale, min(canvasWidth, canvasHeight) * 0.005f)
    val gap = min(10f * densityScale, canvasWidth * 0.024f)
    // Padding between the LCD plate edge and the game/HUD content.
    val contentPad = min(14f * densityScale, min(canvasWidth, canvasHeight) * 0.04f)
        .coerceAtLeast(10f * densityScale)
    val sidePanelWidth = (72f * densityScale).coerceIn(
        minimumValue = 64f * densityScale,
        maximumValue = 96f * densityScale
    )

    // Outer plate fills the board area (aligned with the control pad width).
    val panelLeft = 0f
    val panelTop = 0f
    val panelWidth = canvasWidth
    val panelHeight = canvasHeight

    val contentLeft = panelLeft + borderWidth + contentPad
    val contentTop = panelTop + borderWidth + contentPad
    val contentWidth = (panelWidth - 2f * (borderWidth + contentPad)).coerceAtLeast(1f)
    val contentHeight = (panelHeight - 2f * (borderWidth + contentPad)).coerceAtLeast(1f)

    val playfieldBudgetWidth = (contentWidth - gap - sidePanelWidth).coerceAtLeast(1f)
    val playfieldBudgetHeight = contentHeight

    val cellByWidth =
        (playfieldBudgetWidth - 2f * innerMargin - cellMargin * (columns - 1)) / columns
    val cellByHeight =
        (playfieldBudgetHeight - 2f * innerMargin - cellMargin * (rows - 1)) / rows
    val cellSize = min(cellByWidth, cellByHeight).coerceAtLeast(4f)
    val cellStride = cellSize + cellMargin

    val playfieldWidth = columns * cellSize + (columns - 1) * cellMargin + 2f * innerMargin
    val playfieldHeight = rows * cellSize + (rows - 1) * cellMargin + 2f * innerMargin

    val groupWidth = playfieldWidth + gap + sidePanelWidth
    val groupHeight = playfieldHeight
    val groupLeft = contentLeft + (contentWidth - groupWidth).coerceAtLeast(0f) / 2f
    val groupTop = contentTop + (contentHeight - groupHeight).coerceAtLeast(0f) / 2f

    val playfieldLeft = groupLeft
    val playfieldTop = groupTop
    val sidePanelLeft = playfieldLeft + playfieldWidth + gap
    val sidePanelTop = playfieldTop
    val sidePanelHeight = playfieldHeight

    val overlayFontSizePx = (playfieldWidth * 0.18f).coerceIn(
        minimumValue = 28f * densityScale,
        maximumValue = 96f * densityScale
    )
    return PlayfieldLayout(
        cellSize = cellSize,
        cellStride = cellStride,
        cellMargin = cellMargin,
        borderWidth = borderWidth,
        innerMargin = innerMargin,
        panelLeft = panelLeft,
        panelTop = panelTop,
        panelWidth = panelWidth,
        panelHeight = panelHeight,
        playfieldLeft = playfieldLeft,
        playfieldTop = playfieldTop,
        playfieldWidth = playfieldWidth,
        playfieldHeight = playfieldHeight,
        sidePanelLeft = sidePanelLeft,
        sidePanelTop = sidePanelTop,
        sidePanelWidth = sidePanelWidth,
        sidePanelHeight = sidePanelHeight,
        overlayFontSizePx = overlayFontSizePx
    )
}

internal data class GamePadMetrics(
    val controlButtonSize: Dp,
    val moveButtonSize: Dp,
    val iconSize: Dp,
    val rowSpacing: Dp
)

internal fun computeGamePadMetrics(
    maxWidth: Dp,
    maxHeight: Dp,
    landscape: Boolean
): GamePadMetrics {
    val shortest = minOf(a = maxWidth, b = maxHeight)
    val moveButtonSize = when {
        landscape -> (shortest * 0.16f).coerceIn(minimumValue = 44.dp, maximumValue = 72.dp)
        else -> (maxWidth / 4.8f).coerceIn(minimumValue = 52.dp, maximumValue = 92.dp)
    }
    val controlButtonSize = (moveButtonSize * 0.38f).coerceIn(
        minimumValue = 22.dp,
        maximumValue = 36.dp
    )
    val iconSize = (moveButtonSize * 0.44f).coerceIn(
        minimumValue = 20.dp,
        maximumValue = 38.dp
    )
    return GamePadMetrics(
        controlButtonSize = controlButtonSize,
        moveButtonSize = moveButtonSize,
        iconSize = iconSize,
        rowSpacing = 12.dp
    )
}

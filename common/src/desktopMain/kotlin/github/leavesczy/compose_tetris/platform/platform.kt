package github.leavesczy.compose_tetris.platform

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.nativeCanvas
import github.leavesczy.compose_tetris.common.logic.Location
import github.leavesczy.compose_tetris.common.logic.SoundPlayer
import github.leavesczy.compose_tetris.desktop.DesktopSoundPlayer
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.PaintMode

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
actual fun getScreenSize(): Location {
    return Location(x = 38, y = 20)
}

actual fun getSoundPlayer(): SoundPlayer {
    return DesktopSoundPlayer()
}

actual fun Canvas.drawText(
    text: String,
    color: Int,
    fontSize: Float,
    strokeWidth: Float,
    x: Float,
    y: Float,
) {
    val textPaint = Paint().apply {
        this.mode = PaintMode.STROKE_AND_FILL
        this.isAntiAlias = true
        this.isDither = true
        this.color = color
        this.strokeWidth = strokeWidth / 2f
    }
    val font = Font().apply {
        this.size = fontSize * 1.5f
    }
    val measureWidth = font.measureText(s = text).width
    nativeCanvas.drawString(
        s = text,
        x = x / 2f - measureWidth / 2f,
        y = y / 2f,
        font = font,
        paint = textPaint
    )
}
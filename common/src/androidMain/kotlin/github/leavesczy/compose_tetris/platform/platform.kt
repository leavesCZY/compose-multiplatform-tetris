package github.leavesczy.compose_tetris.platform

import android.graphics.Paint
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.nativeCanvas
import github.leavesczy.compose_tetris.android.AndroidSoundPlayer
import github.leavesczy.compose_tetris.common.logic.Location
import github.leavesczy.compose_tetris.common.logic.SoundPlayer

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:03
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
actual fun getScreenSize(): Location {
    return Location(x = 12, y = 25)
}

actual fun getSoundPlayer(): SoundPlayer {
    return AndroidSoundPlayer()
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
        this.color = color
        this.textSize = fontSize
        this.textAlign = Paint.Align.CENTER
        this.style = Paint.Style.FILL_AND_STROKE
        this.strokeWidth = strokeWidth
        this.isAntiAlias = true
        this.isDither = true
    }
    nativeCanvas.drawText(
        text,
        x / 2f,
        y / 3f,
        textPaint
    )
}
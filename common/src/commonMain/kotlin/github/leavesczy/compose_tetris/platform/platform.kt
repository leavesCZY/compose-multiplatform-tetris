package github.leavesczy.compose_tetris.platform

import androidx.compose.ui.graphics.Canvas
import github.leavesczy.compose_tetris.common.logic.Location
import github.leavesczy.compose_tetris.common.logic.SoundPlayer

expect fun getScreenSize(): Location

expect fun getSoundPlayer(): SoundPlayer

expect fun Canvas.drawText(
    text: String,
    color: Int,
    fontSize: Float,
    strokeWidth: Float,
    x: Float,
    y: Float,
)
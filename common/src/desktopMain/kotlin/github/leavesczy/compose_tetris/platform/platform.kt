package github.leavesczy.compose_tetris.platform

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.nativeCanvas
import github.leavesczy.compose_tetris.common.logic.Location
import github.leavesczy.compose_tetris.common.logic.SoundPlayer
import github.leavesczy.compose_tetris.common.logic.SoundType
import javazoom.jl.player.Player
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.PaintMode
import java.io.File
import java.io.FileInputStream

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
private val DesktopSoundPlayer = object : SoundPlayer {

    override fun play(soundType: SoundType) {
        try {
            val audioFile = getAudioFile(soundType)
            val player = Player(FileInputStream(audioFile))
            player.play()
            player.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun release() {

    }

    private fun getAudioFile(soundType: SoundType): File {
        return when (soundType) {
            SoundType.Welcome -> {
                getAudioFile("welcome.mp3")
            }
            SoundType.Transformation -> {
                getAudioFile("transformation.mp3")
            }
            SoundType.Rotate -> {
                getAudioFile("rotate.mp3")
            }
            SoundType.Fall -> {
                getAudioFile("fall.mp3")
            }
            SoundType.Clean -> {
                getAudioFile("clean.mp3")
            }
        }
    }

    private fun getAudioFile(fileName: String): File {
        return File(javaClass.getResource("/raw/$fileName")!!.path)
    }

}

actual fun getScreenSize(): Location {
    return Location(x = 38, y = 20)
}

actual fun getSoundPlayer(): SoundPlayer {
    return DesktopSoundPlayer
}

private val textPaint = Paint().apply {
    mode = PaintMode.STROKE_AND_FILL
    strokeWidth = strokeWidth
    isAntiAlias = true
    isDither = true
}

private val font = Font()

actual fun Canvas.drawText(
    text: String,
    color: Int,
    fontSize: Float,
    strokeWidth: Float,
    x: Float,
    y: Float,
) {
    textPaint.color = color
    textPaint.strokeWidth = strokeWidth / 2f
    font.size = fontSize * 1.5f
    val measureWidth = font.measureText(s = text).width
    nativeCanvas.drawString(
        s = text,
        x = x / 2f - measureWidth / 2f,
        y = y / 2f,
        font = font,
        paint = textPaint
    )
}
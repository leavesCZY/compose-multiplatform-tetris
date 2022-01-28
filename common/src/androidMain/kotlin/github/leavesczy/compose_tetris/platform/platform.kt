package github.leavesczy.compose_tetris.platform

import android.graphics.Paint
import android.media.AudioManager
import android.media.SoundPool
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.nativeCanvas
import github.leavesczy.compose_tetris.android.ContextHolder
import github.leavesczy.compose_tetris.android.R
import github.leavesczy.compose_tetris.common.logic.Location
import github.leavesczy.compose_tetris.common.logic.SoundPlayer
import github.leavesczy.compose_tetris.common.logic.SoundType

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:03
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
private val AndroidSoundPlayer: SoundPlayer = object : SoundPlayer {

    private val soundPool by lazy {
        SoundPool.Builder().setMaxStreams(4).setMaxStreams(AudioManager.STREAM_MUSIC).build()
    }

    private val soundMap = mutableMapOf<SoundType, Int>()

    init {
        for (value in SoundType.values()) {
            soundMap[value] =
                soundPool.load(ContextHolder.context, getAudioId(soundType = value), 1)
        }
    }

    private fun getAudioId(soundType: SoundType): Int {
        return when (soundType) {
            SoundType.Welcome -> {
                R.raw.welcome
            }
            SoundType.Transformation -> {
                R.raw.transformation
            }
            SoundType.Rotate -> {
                R.raw.rotate
            }
            SoundType.Fall -> {
                R.raw.fall
            }
            SoundType.Clean -> {
                R.raw.clean
            }
        }
    }

    override fun play(soundType: SoundType) {
        soundPool.play(requireNotNull(soundMap[soundType]), 1f, 1f, 0, 0, 1f)
    }

    override fun release() {
        soundMap.clear()
        soundPool.release()
    }

}

actual fun getScreenSize(): Location {
    return Location(x = 12, y = 25)
}

actual fun getSoundPlayer(): SoundPlayer {
    return AndroidSoundPlayer
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
package github.leavesczy.compose_tetris.utils

import android.app.Application
import android.media.AudioManager
import android.media.SoundPool
import github.leavesczy.compose_tetris.R

/**
 * @Author: leavesCZY
 * @Date: 2021/6/4 22:26
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */

enum class SoundType(val audioId: Int) {
    Welcome(R.raw.welcome),
    Transformation(R.raw.transformation),
    Rotate(R.raw.rotate),
    Fall(R.raw.fall),
    Clean(R.raw.clean),
}

object SoundUtil {

    private var context: Application? = null

    private val soundPool by lazy {
        SoundPool.Builder().setMaxStreams(4).setMaxStreams(AudioManager.STREAM_MUSIC).build()
    }
    private val map = mutableMapOf<SoundType, Int>()

    fun init(context: Application) {
        require(this.context == null)
        this.context = context
        map.clear()
        for (value in SoundType.values()) {
            map[value] = soundPool.load(this.context, value.audioId, 1)
        }
    }

    fun play(soundType: SoundType) {
        soundPool.play(requireNotNull(map[soundType]), 1f, 1f, 0, 0, 1f)
    }

    fun release() {
        context = null
        map.clear()
        soundPool.release()
    }

}
package github.leavesczy.compose_tetris.utils

import android.app.Application
import android.media.AudioManager
import android.media.SoundPool
import github.leavesczy.compose_tetris.R

/**
 * @Author: leavesC
 * @Date: 2022/1/17 23:35
 * @Desc:
 * @Github：https://github.com/leavesC
 */

lateinit var SoundPlayerInstance: SoundPlayer

class AndroidSoundPlayer(context: Application) : SoundPlayer {

    private val soundPool by lazy {
        SoundPool.Builder().setMaxStreams(4).setMaxStreams(AudioManager.STREAM_MUSIC).build()
    }

    private val soundMap = mutableMapOf<SoundType, Int>()

    init {
        for (value in SoundType.values()) {
            soundMap[value] = soundPool.load(context, getAudioId(soundType = value), 1)
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
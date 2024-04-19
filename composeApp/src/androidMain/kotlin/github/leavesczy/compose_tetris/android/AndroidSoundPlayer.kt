package github.leavesczy.compose_tetris.android

import android.media.AudioManager
import android.media.SoundPool
import github.leavesczy.compose_tetris.R
import github.leavesczy.compose_tetris.common.logic.SoundPlayer
import github.leavesczy.compose_tetris.common.logic.SoundType

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:03
 * @Desc:
 */
class AndroidSoundPlayer : SoundPlayer {

    private val soundPool =
        SoundPool.Builder().setMaxStreams(4).setMaxStreams(AudioManager.STREAM_MUSIC).build()

    private val soundMap = mutableMapOf<SoundType, Int>()

    init {
        for (value in SoundType.entries) {
            soundMap[value] =
                soundPool.load(ContextProvider.context, getAudioId(soundType = value), 1)
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
        soundPool.play(soundMap[soundType]!!, 1f, 1f, 0, 0, 1f)
    }

    override fun release() {
        soundMap.clear()
        soundPool.release()
    }

}
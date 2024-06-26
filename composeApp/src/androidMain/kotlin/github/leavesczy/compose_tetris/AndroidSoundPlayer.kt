package github.leavesczy.compose_tetris

import android.app.Application
import android.media.AudioManager
import android.media.SoundPool
import github.leavesczy.compose_tetris.logic.SoundPlayer
import github.leavesczy.compose_tetris.logic.SoundType

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:03
 * @Desc:
 */
class AndroidSoundPlayer(application: Application) : SoundPlayer {

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setMaxStreams(AudioManager.STREAM_MUSIC)
        .build()

    private val soundMap = mutableMapOf<SoundType, Int>()

    init {
        for (value in SoundType.entries) {
            soundMap[value] = soundPool.load(application, getAudioId(soundType = value), 1)
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
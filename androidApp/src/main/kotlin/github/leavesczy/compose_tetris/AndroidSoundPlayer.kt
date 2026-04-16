package github.leavesczy.compose_tetris

import android.app.Application
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import github.leavesczy.compose_tetris.base.logic.SoundPlayer
import github.leavesczy.compose_tetris.base.logic.SoundType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:02
 * @Desc:
 */
class AndroidSoundPlayer(private val application: Application) : SoundPlayer {

    private val soundPool = run {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        SoundPool.Builder()
            .setMaxStreams(2)
            .setMaxStreams(AudioManager.STREAM_MUSIC)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private val soundMap = mutableMapOf<SoundType, Int>()

    override suspend fun init() {
        withContext(context = Dispatchers.Main.immediate) {
            for (value in SoundType.entries) {
                soundMap[value] = loadSoundId(soundType = value)
            }
        }
    }

    private suspend fun loadSoundId(soundType: SoundType): Int {
        return withContext(context = Dispatchers.IO) {
            val uriString = getMediaFileUri(soundType = soundType)
            val assetPath = uriString.replace("file:///android_asset/", "")
            val assetFileDescriptor = application.assets.openFd(assetPath)
            soundPool.load(assetFileDescriptor, 1)
        }
    }

    override fun play(soundType: SoundType) {
        val soundId = soundMap[soundType] ?: return
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    override fun release() {
        soundMap.clear()
        soundPool.release()
    }

}
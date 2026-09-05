package github.leavesczy.compose_tetris

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.SoundPool
import github.leavesczy.compose_tetris.base.logic.SoundPlayer
import github.leavesczy.compose_tetris.base.logic.SoundType
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidSoundPlayer(private val application: Application) : SoundPlayer {

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val soundIds = mutableMapOf<SoundType, Int>()
    private var released = false

    override suspend fun init() {
        withContext(context = Dispatchers.IO) {
            val loaded = CompletableDeferred<Unit>()
            val total = SoundType.entries.size
            var completed = 0
            soundPool.setOnLoadCompleteListener { _, _, _ ->
                completed++
                if (completed >= total && !loaded.isCompleted) {
                    loaded.complete(value = Unit)
                }
            }
            val openDescriptors = mutableListOf<AssetFileDescriptor>()
            try {
                for (soundType in SoundType.entries) {
                    val uriString = getMediaFileUri(soundType = soundType)
                    val assetPath = uriString.replace(
                        oldValue = "file:///android_asset/",
                        newValue = ""
                    )
                    val assetFileDescriptor = application.assets.openFd(assetPath)
                    openDescriptors.add(assetFileDescriptor)
                    soundIds[soundType] = soundPool.load(assetFileDescriptor, 1)
                }
                loaded.await()
            } finally {
                for (descriptor in openDescriptors) {
                    runCatching { descriptor.close() }
                }
                soundPool.setOnLoadCompleteListener(null)
            }
        }
    }

    override fun play(soundType: SoundType) {
        if (released) {
            return
        }
        val soundId = soundIds[soundType] ?: return
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    override fun pause() {
        if (!released) {
            soundPool.autoPause()
        }
    }

    override fun release() {
        if (released) {
            return
        }
        released = true
        soundIds.clear()
        soundPool.setOnLoadCompleteListener(null)
        soundPool.autoPause()
        soundPool.release()
    }

}
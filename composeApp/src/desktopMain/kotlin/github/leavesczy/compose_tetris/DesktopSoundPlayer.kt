package github.leavesczy.compose_tetris

import github.leavesczy.compose_tetris.base.logic.SoundPlayer
import github.leavesczy.compose_tetris.base.logic.SoundType
import github.leavesczy.compose_tetris.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

class DesktopSoundPlayer : SoundPlayer {

    private val coroutineScope = CoroutineScope(context = SupervisorJob() + Dispatchers.Default)
    private val soundMap = mutableMapOf<SoundType, ByteArray>()
    private val activeClips = ConcurrentHashMap.newKeySet<Clip>()
    private var released = false

    override suspend fun init() {
        withContext(context = Dispatchers.IO) {
            for (value in SoundType.entries) {
                soundMap[value] = loadMediaByteArray(soundType = value)
            }
        }
    }

    override fun play(soundType: SoundType) {
        if (released) {
            return
        }
        coroutineScope.launch {
            if (released) {
                return@launch
            }
            val clip = AudioSystem.getClip() ?: return@launch
            val resourceInputStream = getMediaResourceStream(soundType = soundType)
            val audioInputStream = AudioSystem.getAudioInputStream(resourceInputStream)
            clip.open(audioInputStream)
            clip.microsecondPosition = 0
            clip.loop(0)
            activeClips.add(clip)
            clip.addLineListener(ReleaseLineListener(clip = clip))
            clip.start()
        }
    }

    override fun pause() {
        for (clip in activeClips.toTypedArray()) {
            stopAndClose(clip = clip)
        }
        activeClips.clear()
    }

    private inner class ReleaseLineListener(
        private val clip: Clip
    ) : LineListener {
        override fun update(event: LineEvent) {
            if (event.type == LineEvent.Type.STOP) {
                coroutineScope.launch {
                    stopAndClose(clip = clip)
                }
            }
        }
    }

    private fun stopAndClose(clip: Clip) {
        activeClips.remove(clip)
        runCatching {
            if (clip.isOpen) {
                clip.stop()
                clip.close()
            }
        }
    }

    private suspend fun getMediaResourceStream(soundType: SoundType): InputStream {
        val byteArray = soundMap[soundType] ?: loadMediaByteArray(soundType = soundType)
        return byteArray.inputStream()
    }

    private suspend fun loadMediaByteArray(soundType: SoundType): ByteArray {
        return withContext(context = Dispatchers.IO) {
            val resourcePath = getMediaFilePath(soundType = soundType)
            Res.readBytes(resourcePath)
        }
    }

    override fun release() {
        if (released) {
            return
        }
        released = true
        pause()
        soundMap.clear()
        coroutineScope.cancel()
    }

}
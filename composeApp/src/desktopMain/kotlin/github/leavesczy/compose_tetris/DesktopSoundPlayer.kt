package github.leavesczy.compose_tetris

import github.leavesczy.compose_tetris.base.logic.SoundPlayer
import github.leavesczy.compose_tetris.base.logic.SoundType
import github.leavesczy.compose_tetris.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:03
 * @Desc:
 */
class DesktopSoundPlayer(private val coroutineScope: CoroutineScope) : SoundPlayer {

    private val soundMap = mutableMapOf<SoundType, ByteArray>()

    override suspend fun init() {
        withContext(context = Dispatchers.Main.immediate) {
            for (value in SoundType.entries) {
                soundMap[value] = loadMediaByteArray(soundType = value)
            }
        }
    }

    override fun play(soundType: SoundType) {
        coroutineScope.launch {
            val clip = AudioSystem.getClip() ?: return@launch
            val resourceInputStream = getMediaResourceStream(soundType = soundType)
            val audioInputStream = AudioSystem.getAudioInputStream(resourceInputStream)
            clip.open(audioInputStream)
            clip.microsecondPosition = 0
            clip.loop(0)
            clip.addLineListener(
                ReleaseLineListener(
                    clip = clip,
                    coroutineScope = coroutineScope
                )
            )
            clip.start()
        }
    }

    private class ReleaseLineListener(
        private val clip: Clip,
        private val coroutineScope: CoroutineScope
    ) : LineListener {
        override fun update(event: LineEvent) {
            if (event.type == LineEvent.Type.STOP) {
                coroutineScope.launch {
                    clip.stop()
                    clip.close()
                }
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
        soundMap.clear()
        coroutineScope.cancel()
    }

}
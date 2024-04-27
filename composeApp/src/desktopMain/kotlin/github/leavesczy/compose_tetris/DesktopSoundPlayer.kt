package github.leavesczy.compose_tetris

import github.leavesczy.compose_tetris.logic.SoundPlayer
import github.leavesczy.compose_tetris.logic.SoundType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.InputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Desc:
 */
class DesktopSoundPlayer(private val coroutineScope: CoroutineScope) : SoundPlayer {

    override fun play(soundType: SoundType) {
        coroutineScope.launch {
            val audioResourceInputStream = getAudioResourceStream(soundType = soundType)
            val audioBufferedInputStream = BufferedInputStream(audioResourceInputStream)
            val audioInputStream = AudioSystem.getAudioInputStream(audioBufferedInputStream)
            val clip = AudioSystem.getClip()
            clip?.apply {
                open(audioInputStream)
                microsecondPosition = 0
                loop(0)
                addLineListener(
                    ReleaseLineListener(
                        clip = this,
                        coroutineScope = coroutineScope
                    )
                )
                start()
            }
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

    private fun getAudioResourceStream(soundType: SoundType): InputStream {
        return when (soundType) {
            SoundType.Welcome -> {
                getAudioResourceStream("welcome.wav")
            }

            SoundType.Transformation -> {
                getAudioResourceStream("transformation.wav")
            }

            SoundType.Rotate -> {
                getAudioResourceStream("rotate.wav")
            }

            SoundType.Fall -> {
                getAudioResourceStream("fall.wav")
            }

            SoundType.Clean -> {
                getAudioResourceStream("clean.wav")
            }
        }
    }

    private fun getAudioResourceStream(fileName: String): InputStream {
        return javaClass.getResourceAsStream("/raw/$fileName")!!
    }

    override fun release() {
        coroutineScope.cancel()
    }

}
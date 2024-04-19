package github.leavesczy.compose_tetris.desktop

import github.leavesczy.compose_tetris.common.logic.SoundPlayer
import github.leavesczy.compose_tetris.common.logic.SoundType
import kotlinx.coroutines.Dispatchers
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
class DesktopSoundPlayer : SoundPlayer {

    override fun play(soundType: SoundType) {
        DesktopCoroutineScope.launch(context = Dispatchers.IO) {
            val audioResourceInputStream = getAudioResourceStream(soundType = soundType)
            val audioBufferedInputStream = BufferedInputStream(audioResourceInputStream)
            val audioInputStream = AudioSystem.getAudioInputStream(audioBufferedInputStream)
            val clip = AudioSystem.getClip()
            clip?.apply {
                open(audioInputStream)
                microsecondPosition = 0
                loop(0)
                addLineListener(ReleaseLineListener(this))
                start()
            }
        }
    }

    private class ReleaseLineListener(private val clip: Clip) : LineListener {
        override fun update(event: LineEvent) {
            if (event.type == LineEvent.Type.STOP) {
                DesktopCoroutineScope.launch(context = Dispatchers.IO) {
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

    }

}
package github.leavesczy.compose_tetris.desktop

import github.leavesczy.compose_tetris.common.logic.SoundPlayer
import github.leavesczy.compose_tetris.common.logic.SoundType
import javazoom.jl.player.Player
import java.io.InputStream

/**
 * @Author: leavesCZY
 * @Date: 2022/1/20 15:09
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class DesktopSoundPlayer : SoundPlayer {

    override fun play(soundType: SoundType) {
        try {
            val audioFile = getAudioResourceStream(soundType)
            val player = Player(audioFile)
            player.play()
            player.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun release() {

    }

    private fun getAudioResourceStream(soundType: SoundType): InputStream {
        return when (soundType) {
            SoundType.Welcome -> {
                getAudioResourceStream("welcome.mp3")
            }

            SoundType.Transformation -> {
                getAudioResourceStream("transformation.mp3")
            }

            SoundType.Rotate -> {
                getAudioResourceStream("rotate.mp3")
            }

            SoundType.Fall -> {
                getAudioResourceStream("fall.mp3")
            }

            SoundType.Clean -> {
                getAudioResourceStream("clean.mp3")
            }
        }
    }

    private fun getAudioResourceStream(fileName: String): InputStream {
        return javaClass.getResourceAsStream("/raw/$fileName")!!
    }

}
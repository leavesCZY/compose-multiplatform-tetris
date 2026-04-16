package github.leavesczy.compose_tetris.base.logic

import github.leavesczy.compose_tetris.resources.Res

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:03
 * @Desc:
 */
interface SoundPlayer {

    suspend fun init()

    fun play(soundType: SoundType)

    fun release()

    fun getMediaFileUri(soundType: SoundType): String {
        val resourcePath = getMediaFilePath(soundType = soundType)
        return Res.getUri(resourcePath)
    }

    fun getMediaFilePath(soundType: SoundType): String {
        val resourceName = getMediaFileName(soundType = soundType)
        return "files/$resourceName"
    }

    private fun getMediaFileName(soundType: SoundType): String {
        return when (soundType) {
            SoundType.Welcome -> {
                "welcome.wav"
            }

            SoundType.Transformation -> {
                "transformation.wav"
            }

            SoundType.Rotate -> {
                "rotate.wav"
            }

            SoundType.Fall -> {
                "fall.wav"
            }

            SoundType.Clean -> {
                "clean.wav"
            }
        }
    }

}

enum class SoundType {
    Welcome,
    Transformation,
    Rotate,
    Fall,
    Clean;
}
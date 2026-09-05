package github.leavesczy.compose_tetris.base.logic

import github.leavesczy.compose_tetris.resources.Res

interface SoundPlayer {

    suspend fun init()

    fun play(soundType: SoundType)

    fun pause()

    fun release()

    fun getMediaFileUri(soundType: SoundType): String {
        return Res.getUri(path = getMediaFilePath(soundType = soundType))
    }

    fun getMediaFilePath(soundType: SoundType): String {
        return "files/${soundType.fileName}"
    }

}

enum class SoundType(val fileName: String) {
    Welcome(fileName = "welcome.wav"),
    Transform(fileName = "transformation.wav"),
    Rotate(fileName = "rotate.wav"),
    HardDrop(fileName = "fall.wav"),
    LineClear(fileName = "clean.wav")
}
package github.leavesczy.compose_tetris.utils

/**
 * @Author: leavesC
 * @Date: 2022/1/17 23:34
 * @Desc:
 * @Github：https://github.com/leavesC
 */
enum class SoundType {
    Welcome,
    Transformation,
    Rotate,
    Fall,
    Clean,
}

interface SoundPlayer {

    fun play(soundType: SoundType)

    fun release()

}
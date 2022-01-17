package github.leavesczy.compose_tetris.utils

import github.leavesczy.compose_tetris.R

/**
 * @Author: leavesC
 * @Date: 2022/1/17 23:34
 * @Desc:
 * @Github：https://github.com/leavesC
 */
enum class SoundType(val audioId: Int) {
    Welcome(R.raw.welcome),
    Transformation(R.raw.transformation),
    Rotate(R.raw.rotate),
    Fall(R.raw.fall),
    Clean(R.raw.clean),
}
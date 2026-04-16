package github.leavesczy.compose_tetris

import github.leavesczy.compose_tetris.base.logic.GameStatus
import github.leavesczy.compose_tetris.base.logic.Location

/**
 * @Author: leavesCZY
 * @Date: 2026/4/16 20:03
 * @Desc:
 */
expect fun getScreenSize(): Location

expect fun getFontSize(gameStatus: GameStatus): Int
package github.leavesczy.compose_tetris.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.logic.TetrisLogic
import github.leavesczy.compose_tetris.common.logic.TransformationType
import github.leavesczy.compose_tetris.common.ui.ButtonDisabledColor
import github.leavesczy.compose_tetris.common.ui.ButtonNormalColor
import github.leavesczy.compose_tetris.common.ui.ControlButton
import github.leavesczy.compose_tetris.common.ui.PlayButton

/**
 * @Author: leavesCZY
 * @Date: 2023/9/12 16:52
 * @Desc:
 */
@Composable
fun AndroidTetrisButton(tetrisLogic: TetrisLogic) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val controlButtonSize = 32.dp
        val playButtonSize = 65.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlButton(
                text = "Start",
                size = controlButtonSize,
                onClick = {
                    tetrisLogic.dispatch(action = Action.Start)
                }
            )
            ControlButton(
                text = "Pause",
                size = controlButtonSize,
                onClick = {
                    tetrisLogic.dispatch(action = Action.Pause)
                }
            )
            ControlButton(
                text = "Reset",
                size = controlButtonSize,
                onClick = {
                    tetrisLogic.dispatch(action = Action.Reset)
                }
            )
            ControlButton(
                text = "Sound",
                size = controlButtonSize,
                color = if (tetrisLogic.tetrisViewState.soundEnable) {
                    ButtonNormalColor
                } else {
                    ButtonDisabledColor
                },
                onClick = {
                    tetrisLogic.dispatch(action = Action.Sound)
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlayButton(
                icon = Icons.AutoMirrored.Filled.ArrowLeft,
                size = playButtonSize
            ) {
                tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.Left))
            }
            PlayButton(
                icon = Icons.AutoMirrored.Filled.ArrowRight,
                size = playButtonSize
            ) {
                tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.Right))
            }
            PlayButton(
                icon = Icons.AutoMirrored.Filled.RotateRight,
                size = playButtonSize
            ) {
                tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.Rotate))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 36.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            PlayButton(
                modifier = Modifier,
                icon = Icons.Filled.ArrowDropDown,
                size = playButtonSize
            ) {
                tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.FastDown))
            }
            PlayButton(
                modifier = Modifier
                    .rotate(degrees = 90f),
                icon = Icons.Filled.FastForward,
                size = playButtonSize
            ) {
                tetrisLogic.dispatch(action = Action.Transformation(transformationType = TransformationType.Fall))
            }
        }
    }
}
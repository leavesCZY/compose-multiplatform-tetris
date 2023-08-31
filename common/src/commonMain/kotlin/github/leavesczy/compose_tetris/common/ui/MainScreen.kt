package github.leavesczy.compose_tetris.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.logic.PlayListener
import github.leavesczy.compose_tetris.common.logic.TetrisLogic
import github.leavesczy.compose_tetris.common.ui.theme.ComposeTetrisTheme

@Composable
fun MainScreen(modifier: Modifier, tetrisLogic: TetrisLogic) {
    val playListener = remember {
        PlayListener(
            onStart = {
                tetrisLogic.dispatch(action = Action.Start)
            },
            onPause = {
                tetrisLogic.dispatch(action = Action.Pause)
            },
            onReset = {
                tetrisLogic.dispatch(action = Action.Reset)
            },
            onTransformation = {
                tetrisLogic.dispatch(action = Action.Transformation(it))
            },
            onSound = {
                tetrisLogic.dispatch(action = Action.Sound)
            },
        )
    }
    LaunchedEffect(key1 = Unit) {
        tetrisLogic.dispatch(action = Action.Welcome)
    }
    ComposeTetrisTheme {
        Scaffold(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .then(other = modifier),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f)
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                ) {
                    TetrisScreen(tetrisViewState = tetrisLogic.tetrisViewState)
                }
                Spacer(
                    modifier = Modifier
                        .height(height = 6.dp)
                )
                TetrisButton(
                    tetrisViewState = tetrisLogic.tetrisViewState,
                    playListener = playListener
                )
                Spacer(
                    modifier = Modifier
                        .height(height = 30.dp)
                )
            }
        }
    }
}
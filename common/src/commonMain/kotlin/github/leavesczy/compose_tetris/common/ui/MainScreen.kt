package github.leavesczy.compose_tetris.common.ui

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import github.leavesczy.compose_tetris.common.logic.Action
import github.leavesczy.compose_tetris.common.logic.ITetrisLogic
import github.leavesczy.compose_tetris.common.logic.combinedPlayListener
import github.leavesczy.compose_tetris.common.ui.theme.ComposeTetrisTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier, tetrisLogic: ITetrisLogic) {
    val tetrisState by tetrisLogic.tetrisStateFlow.collectAsState()
    val playListener = remember {
        combinedPlayListener(
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
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
                .then(other = modifier)
        ) {
            TetrisBody(tetrisScreen = {
                TetrisScreen(tetrisState = tetrisState)
            }, tetrisButton = {
                TetrisButton(tetrisState = tetrisState, playListener = playListener)
            })
        }
    }
}
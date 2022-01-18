package github.leavesczy.compose_tetris.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

/**
 * @Author: leavesCZY
 * @Date: 2021/5/28 17:19
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun TetrisBody(
    tetrisScreen: @Composable (() -> Unit),
    tetrisButton: @Composable (() -> Unit),
) {
    ProvideWindowInsets {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f)
                    .padding(horizontal = 30.dp)
                    .align(alignment = Alignment.CenterHorizontally),
            ) {
                tetrisScreen()
            }
            Spacer(modifier = Modifier.height(height = 6.dp))
            tetrisButton()
            Spacer(modifier = Modifier.height(height = 30.dp))
        }
    }
}
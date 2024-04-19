package github.leavesczy.compose_tetris

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import github.leavesczy.compose_tetris.android.AndroidMainScreen

/**
 * @Author: leavesCZY
 * @Date: 2021/6/3 22:06
 * @Desc:
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            AndroidMainScreen()
        }
    }

}
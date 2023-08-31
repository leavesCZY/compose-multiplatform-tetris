package github.leavesczy.compose_tetris

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import github.leavesczy.compose_tetris.android.AndroidMainScreen

/**
 * @Author: leavesCZY
 * @Date: 2021/6/3 22:06
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AndroidMainScreen()
        }
    }

}
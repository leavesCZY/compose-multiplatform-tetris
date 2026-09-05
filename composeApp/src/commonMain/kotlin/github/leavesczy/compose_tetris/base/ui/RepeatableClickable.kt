package github.leavesczy.compose_tetris.base.ui

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

internal fun Modifier.repeatableClickable(
    enabled: Boolean = true,
    initialDelayMs: Long = InputRepeatTiming.DasDelayMs,
    repeatIntervalMs: Long = InputRepeatTiming.ArrIntervalMs,
    onClick: () -> Unit
): Modifier = composed {
    var pressed by remember { mutableStateOf(value = false) }
    LaunchedEffect(key1 = pressed, key2 = enabled) {
        if (!enabled || !pressed) {
            return@LaunchedEffect
        }
        onClick()
        delay(timeMillis = initialDelayMs)
        while (isActive) {
            onClick()
            delay(timeMillis = repeatIntervalMs)
        }
    }
    pointerInput(key1 = enabled) {
        if (!enabled) {
            return@pointerInput
        }
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)
            pressed = true
            try {
                waitForUpOrCancellation()
            } finally {
                pressed = false
            }
        }
    }
}
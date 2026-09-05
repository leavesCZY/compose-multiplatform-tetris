package github.leavesczy.compose_tetris.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_tetris.base.logic.GameAction
import github.leavesczy.compose_tetris.base.logic.PieceMove
import github.leavesczy.compose_tetris.resources.Res
import github.leavesczy.compose_tetris.resources.hold
import github.leavesczy.compose_tetris.resources.ic_arrow_drop_down
import github.leavesczy.compose_tetris.resources.ic_arrow_left
import github.leavesczy.compose_tetris.resources.ic_arrow_right
import github.leavesczy.compose_tetris.resources.ic_fast_forward
import github.leavesczy.compose_tetris.resources.ic_rotate_right
import github.leavesczy.compose_tetris.resources.pause
import github.leavesczy.compose_tetris.resources.reset
import github.leavesczy.compose_tetris.resources.sound
import github.leavesczy.compose_tetris.resources.start
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GamePad(
    modifier: Modifier,
    landscape: Boolean,
    soundEnabled: Boolean,
    onAction: (GameAction) -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val metrics = computeGamePadMetrics(
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            landscape = landscape
        )
        val padModifier = Modifier
            .fillMaxWidth()
            .align(alignment = Alignment.Center)
        if (landscape) {
            LandscapeGamePad(
                modifier = padModifier.fillMaxHeight(),
                metrics = metrics,
                soundEnabled = soundEnabled,
                onAction = onAction
            )
        } else {
            PortraitGamePad(
                modifier = padModifier,
                metrics = metrics,
                soundEnabled = soundEnabled,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun PortraitGamePad(
    modifier: Modifier,
    metrics: GamePadMetrics,
    soundEnabled: Boolean,
    onAction: (GameAction) -> Unit
) {
    Column(
        modifier = modifier.padChrome().padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = metrics.rowSpacing,
            alignment = Alignment.Top
        )
    ) {
        ControlRow(
            metrics = metrics,
            soundEnabled = soundEnabled,
            onAction = onAction
        )
        MoveRow(metrics = metrics, onAction = onAction)
        DropRow(metrics = metrics, onAction = onAction)
    }
}

@Composable
private fun LandscapeGamePad(
    modifier: Modifier,
    metrics: GamePadMetrics,
    soundEnabled: Boolean,
    onAction: (GameAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padChrome()
            .padding(all = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ControlRow(
            metrics = metrics,
            soundEnabled = soundEnabled,
            onAction = onAction
        )
        MoveRow(metrics = metrics, onAction = onAction)
        DropRow(metrics = metrics, onAction = onAction)
    }
}

private fun Modifier.padChrome(): Modifier {
    return clip(shape = PanelShape)
        .background(color = PanelFill)
        .border(width = 1.dp, color = PanelStroke, shape = PanelShape)
}

@Composable
private fun ControlRow(
    metrics: GamePadMetrics,
    soundEnabled: Boolean,
    onAction: (GameAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LabeledRoundButton(
            text = stringResource(resource = Res.string.start),
            size = metrics.controlButtonSize,
            onClick = { onAction(GameAction.Start) }
        )
        LabeledRoundButton(
            text = stringResource(resource = Res.string.pause),
            size = metrics.controlButtonSize,
            onClick = { onAction(GameAction.Pause) }
        )
        LabeledRoundButton(
            text = stringResource(resource = Res.string.reset),
            size = metrics.controlButtonSize,
            onClick = { onAction(GameAction.Reset) }
        )
        LabeledRoundButton(
            text = stringResource(resource = Res.string.sound),
            size = metrics.controlButtonSize,
            highlighted = soundEnabled,
            onClick = { onAction(GameAction.ToggleSound) }
        )
        LabeledRoundButton(
            text = stringResource(resource = Res.string.hold),
            size = metrics.controlButtonSize,
            onClick = { onAction(GameAction.Hold) }
        )
    }
}

@Composable
private fun MoveRow(
    metrics: GamePadMetrics,
    onAction: (GameAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoveButton(
            painter = painterResource(resource = Res.drawable.ic_arrow_left),
            size = metrics.moveButtonSize,
            iconSize = metrics.iconSize,
            repeatable = true,
            contentDescription = "Left",
            onClick = { onAction(GameAction.MovePiece(move = PieceMove.Left)) }
        )
        MoveButton(
            painter = painterResource(resource = Res.drawable.ic_rotate_right),
            size = metrics.moveButtonSize,
            iconSize = metrics.iconSize,
            repeatable = false,
            contentDescription = "Rotate",
            onClick = { onAction(GameAction.MovePiece(move = PieceMove.Rotate)) }
        )
        MoveButton(
            painter = painterResource(resource = Res.drawable.ic_arrow_right),
            size = metrics.moveButtonSize,
            iconSize = metrics.iconSize,
            repeatable = true,
            contentDescription = "Right",
            onClick = { onAction(GameAction.MovePiece(move = PieceMove.Right)) }
        )
    }
}

@Composable
private fun DropRow(
    metrics: GamePadMetrics,
    onAction: (GameAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoveButton(
            painter = painterResource(resource = Res.drawable.ic_arrow_drop_down),
            size = metrics.moveButtonSize,
            iconSize = metrics.iconSize,
            repeatable = true,
            contentDescription = "Soft drop",
            onClick = { onAction(GameAction.MovePiece(move = PieceMove.SoftDrop)) }
        )
        MoveButton(
            modifier = Modifier.rotate(degrees = 90f),
            painter = painterResource(resource = Res.drawable.ic_fast_forward),
            size = metrics.moveButtonSize,
            iconSize = metrics.iconSize,
            repeatable = false,
            contentDescription = "Hard drop",
            onClick = { onAction(GameAction.MovePiece(move = PieceMove.HardDrop)) }
        )
    }
}

@Composable
private fun LabeledRoundButton(
    text: String,
    size: Dp,
    highlighted: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 5.dp, alignment = Alignment.Top)
    ) {
        Text(
            text = text,
            color = LabelOnShell,
            style = MaterialTheme.typography.bodySmall
        )
        Box(
            modifier = Modifier
                .size(size = size)
                .circleBrushBackground(
                    brush = if (highlighted) {
                        ButtonControlBrush
                    } else {
                        ButtonMutedBrush
                    }
                )
                .clickable(onClick = onClick)
        )
    }
}

@Composable
private fun MoveButton(
    painter: Painter,
    size: Dp,
    iconSize: Dp,
    repeatable: Boolean,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size = size)
                .circleBrushBackground(brush = ButtonMoveBrush)
                .then(
                    if (repeatable) {
                        Modifier.repeatableClickable(onClick = onClick)
                    } else {
                        Modifier.clickable(onClick = onClick)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(size = iconSize),
                painter = painter,
                tint = Color.White,
                contentDescription = contentDescription
            )
        }
    }
}

private fun Modifier.circleBrushBackground(brush: Brush): Modifier {
    return shadow(elevation = 1.dp, shape = CircleShape, clip = false)
        .clip(shape = CircleShape)
        .background(brush = brush)
}
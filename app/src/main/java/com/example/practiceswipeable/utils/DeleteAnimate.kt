package com.example.practiceswipeable.utils

import androidx.compose.animation.DpPropKey
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*Initial: means starting configuration
* Idle : means it's ready to start animate
* Expand: means animate to expanded state
* */

enum class DeleteBtnAnimateState {
    EXPAND, IDLE, INITIAL
}

val width = DpPropKey()
val iconPadding = DpPropKey()


@Composable
fun transactionAnimationSetting(
    delBtnState: MutableState<DeleteBtnAnimateState>, minWidth: Dp, maxWidth: Dp
): TransitionState {

    val transDef = transitionDefinition<DeleteBtnAnimateState> {
        state(DeleteBtnAnimateState.INITIAL) {
            this[width] = minWidth
            this[iconPadding] = 0.dp
        }
        state(DeleteBtnAnimateState.EXPAND) {
            this[width] = minWidth
            this[iconPadding] = 0.dp
        }
        state(DeleteBtnAnimateState.IDLE) {
            this[width] = maxWidth
            this[iconPadding] = minWidth.times(2)
        }
        transition(fromState = DeleteBtnAnimateState.EXPAND, toState = DeleteBtnAnimateState.IDLE) {
            width using tween(durationMillis = 500)
            iconPadding using tween(durationMillis = 500)
        }
        transition(DeleteBtnAnimateState.IDLE to DeleteBtnAnimateState.EXPAND) {
            width using tween(durationMillis = 500)
            iconPadding using tween(durationMillis = 500)
        }
    }

    val toState = if (delBtnState.value == DeleteBtnAnimateState.EXPAND) {
        DeleteBtnAnimateState.IDLE
    } else {
        DeleteBtnAnimateState.EXPAND
    }

    return transition(
        definition = transDef,
        initState = delBtnState.value,
        toState = toState
    )
}
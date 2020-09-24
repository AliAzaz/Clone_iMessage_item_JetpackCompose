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

enum class DeleteBtnAnimateState {
    IDLE, EXPAND, INITIAL
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
        state(DeleteBtnAnimateState.IDLE) {
            this[width] = minWidth
            this[iconPadding] = 0.dp
        }
        state(DeleteBtnAnimateState.EXPAND) {
            this[width] = maxWidth
            this[iconPadding] = minWidth.times(2)
        }
        transition(fromState = DeleteBtnAnimateState.IDLE, toState = DeleteBtnAnimateState.EXPAND) {
            width using tween(durationMillis = 1000)
            iconPadding using tween(durationMillis = 1000)
        }
        transition(DeleteBtnAnimateState.EXPAND to DeleteBtnAnimateState.IDLE) {
            width using tween(durationMillis = 1000)
            iconPadding using tween(durationMillis = 1000)
        }
    }

    val toState = if (delBtnState.value == DeleteBtnAnimateState.IDLE) {
        DeleteBtnAnimateState.EXPAND
    } else {
        DeleteBtnAnimateState.IDLE
    }

    return transition(
        definition = transDef,
        initState = delBtnState.value,
        toState = toState
    )
}
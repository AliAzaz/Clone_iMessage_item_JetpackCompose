package com.example.practiceswipeable.module

import androidx.compose.animation.animatedValue
import androidx.compose.animation.core.AnimatedValue
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.practiceswipeable.utils.DeleteBtnAnimateState

data class ItemModel(var delBtnWidthState: MutableState<Dp> = mutableStateOf(0.dp), var subItemHeight: Int = 0)

data class DeleteItemModel(var flagDrawer : MutableState<Boolean> = mutableStateOf(false),
                           var animatedSubItemCollapse: AnimatedValue<Int, AnimationVector1D>,
                           var delBtnState: MutableState<DeleteBtnAnimateState> = mutableStateOf(DeleteBtnAnimateState.INITIAL),
                           var delBtnIconState: MutableState<DeleteBtnAnimateState> = mutableStateOf(DeleteBtnAnimateState.INITIAL))
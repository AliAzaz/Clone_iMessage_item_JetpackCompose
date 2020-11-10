package com.example.practiceswipeable.module

import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.Dp
import com.example.practiceswipeable.utils.DeleteBtnAnimateState

data class ItemModel(var delBtnWidthState: MutableState<Dp>, var subItemHeight: Int)

//data class DeleteItemModel(var delBtnState: MutableState<DeleteBtnAnimateState>, var delBtnIconState: MutableState<DeleteBtnAnimateState>, var animatedSubItemCollapse: Int)
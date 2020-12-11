package com.example.practiceswipeable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.animatedValue
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.material.Icon
import androidx.compose.foundation.animation.FlingConfig
import androidx.compose.foundation.animation.fling
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces.Acescg
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.practiceswipeable.module.DeleteItemModel
import com.example.practiceswipeable.module.ItemModel
import com.example.practiceswipeable.ui.PracticeSwipeableTheme
import com.example.practiceswipeable.utils.DeleteBtnAnimateState
import com.example.practiceswipeable.utils.iconPadding
import com.example.practiceswipeable.utils.transactionAnimationSetting
import com.example.practiceswipeable.utils.width
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeSwipeableTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SwipeableEvent()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PracticeSwipeableTheme {
        SwipeableEvent()
    }
}


@Composable
fun SwipeableEvent() {

    PracticeSwipeableTheme(content = {

        val itemModelState = remember {
            mutableStateOf(
                ItemModel()
            )
        }

        val btmDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
        val delBtnClicked = remember { mutableStateOf(false) }


        // Bottom Static Drawer
        BottomDrawerLayout(
            drawerState = btmDrawerState,
            drawerContent = {
                BottomStaticDrawerContent(
                    btmDrawerState = btmDrawerState,
                    delBtnClicked = delBtnClicked
                )
            },
            bodyContent = {
                val modelItem: MutableList<DeleteItemModel> = mutableListOf(
                    DeleteItemModel(
                        animatedSubItemCollapse = animatedValue(
                            initVal = 0,
                            converter = Int.VectorConverter
                        )
                    ),
                    DeleteItemModel(
                        animatedSubItemCollapse = animatedValue(
                            initVal = 0,
                            converter = Int.VectorConverter
                        )
                    ),
                    DeleteItemModel(
                        animatedSubItemCollapse = animatedValue(
                            initVal = 0,
                            converter = Int.VectorConverter
                        )
                    )
                )
                LazyColumnFor(
                    items = listOf(0),
                    modifier = Modifier.fillMaxHeight(),
                    itemContent = { item ->
                        /*val flagDrawer = remember { mutableStateOf(false) }
                        val delBtnState = remember { mutableStateOf(DeleteBtnAnimateState.INITIAL) }
                        val delBtnIconState =
                            remember { mutableStateOf(DeleteBtnAnimateState.INITIAL) }
                        val animatedSubItemCollapse =
                            animatedValue(initVal = 0, converter = Int.VectorConverter)*/

                        MessageItemView(
                            itemModelState = itemModelState,
                            delBtnState = modelItem[item].delBtnState,
                            delBtnIconState = modelItem[item].delBtnIconState,
                            btmDrawerState = btmDrawerState,
                            flagDrawer = modelItem[item].flagDrawer,
                            delBtnClicked = delBtnClicked,
                            animatedSubItemCollapse = modelItem[item].animatedSubItemCollapse
                        )

                        /*BottomStaticDrawerEvents(
                            btmDrawerState = btmDrawerState,
                            delBtnState = modelItem[item].delBtnState,
                            delBtnIconState = modelItem[item].delBtnIconState,
                            flagDrawer = modelItem[item].flagDrawer,
                            delBtnClicked = delBtnClicked,
                            itemModelState = itemModelState,
                            animatedSubItemCollapse = modelItem[item].animatedSubItemCollapse
                        )*/
                    })
            },
            gesturesEnabled = false,
            modifier = Modifier.background(color = Color.Transparent),
            drawerBackgroundColor = Color.LightGray,
            scrimColor = Color(0f, 0f, 0f, 0.3f, Acescg)
        )

    })

}


@Composable
fun MessageItemView(
    itemModelState: MutableState<ItemModel>,
    delBtnState: MutableState<DeleteBtnAnimateState>,
    delBtnIconState: MutableState<DeleteBtnAnimateState>,
    btmDrawerState: BottomDrawerState,
    flagDrawer: MutableState<Boolean>,
    delBtnClicked: MutableState<Boolean>,
    animatedSubItemCollapse: AnimatedValue<Int, AnimationVector1D>
) {

    val state = transactionAnimationSetting(
        delBtnState = delBtnState,
        minWidth = itemModelState.value.delBtnWidthState.value,
        maxWidth = itemModelState.value.delBtnWidthState.value.times(3)
    )

    if (btmDrawerState.isOpen) {
        flagDrawer.value = true
    } else if (btmDrawerState.isClosed && flagDrawer.value && !delBtnClicked.value) {
        delBtnState.value = DeleteBtnAnimateState.IDLE
        delBtnIconState.value = DeleteBtnAnimateState.IDLE
    } else if (btmDrawerState.isClosed && delBtnClicked.value) {
        animatedSubItemCollapse.animateTo(
            -itemModelState.value.subItemHeight,
            tween(durationMillis = 500)
        )
        delBtnClicked.value = false
    }

    Column(
        modifier = Modifier.preferredHeight(110.dp).layout { measurable, constraints ->
            val itemView = measurable.measure(constraints)
            itemModelState.value = itemModelState.value.copy(subItemHeight = itemView.height)
            layout(itemView.width, itemView.height) {
                itemView.place(0, animatedSubItemCollapse.value)
            }
        }, content = {
            RowView(
                modifier = Modifier.fillMaxHeight(),
                btnWidthState = itemModelState,
                delBtnState = delBtnState,
                delBtnIconState = delBtnIconState,
                flagDrawer = flagDrawer,
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(5.dp, 5.dp, 0.dp, 10.dp),
                        content = {
                            Row(modifier = Modifier, content = {
                                AvatarImage(url = R.drawable.scene_01)
                                Column(
                                    modifier = Modifier.padding(
                                        start = 10.dp,
                                        top = 5.dp,
                                        end = 15.dp
                                    ), content = {
                                        TitleRowContainer {
                                            TitleRow()
                                        }
                                        Text(
                                            text = "ABCD 123456789",
                                            maxLines = 1,
                                            modifier = Modifier.preferredHeight(80.dp)
                                                .padding(0.dp, 5.dp, 0.dp, 0.dp),
                                            overflow = TextOverflow.Ellipsis,
                                            style = MaterialTheme.typography.body2
                                        )
                                    })
                            })
                        })

                    IconButton(
                        onClick = {},
                        modifier = Modifier.background(MaterialTheme.colors.primary)
                            .height(110.dp),
                        content = {
                            Icon(imageVector = Icons.Filled.Edit, tint = Color.White)
                        })


                    IconButton(
                        onClick = {
                            delBtnState.value = DeleteBtnAnimateState.EXPAND
                            delBtnIconState.value = DeleteBtnAnimateState.EXPAND
                            btmDrawerState.open()
                        },
                        modifier = Modifier.background(Color.Red)
                            .size(state[width], 110.dp),
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                tint = Color.White,
                                modifier = Modifier.padding(
                                    0.dp,
                                    0.dp,
                                    state[iconPadding],
                                    0.dp
                                )
                            )
                        })
                },
            )

            Divider(
                color = Color.LightGray,
                modifier = Modifier.fillMaxWidth().height(1.dp)
            )
        })
}


@Composable
fun RowView(
    btnWidthState: MutableState<ItemModel>,
    modifier: Modifier = Modifier,
    delBtnState: MutableState<DeleteBtnAnimateState>,
    delBtnIconState: MutableState<DeleteBtnAnimateState>,
    flagDrawer: MutableState<Boolean>,
    content: @Composable () -> Unit
) {
    val threshold = .8f
    val width = remember { mutableStateOf(0) }
    val positionOffset = animatedFloat(0f)

    val modify = modifier.draggable(
        orientation = Orientation.Horizontal,
        enabled = !flagDrawer.value,
        onDrag = { delta ->
            positionOffset.snapTo((positionOffset.value + delta).coerceAtMost(0f))
            delBtnState.value = DeleteBtnAnimateState.INITIAL
            delBtnIconState.value = DeleteBtnAnimateState.INITIAL
        },
        onDragStopped = { velocity ->
            val config =
                FlingConfig(anchors = listOf(-width.value.toFloat(), width.value.toFloat()))
            if (positionOffset.value.absoluteValue >= threshold) {
                positionOffset.fling(velocity, config) { _, endValue, _ ->
                }
            }

        }
    )

    if (delBtnState.value == DeleteBtnAnimateState.IDLE) {
        positionOffset.animateTo(0f, tween(100))
        flagDrawer.value = false
    }

    Layout(content = content, modifier = modify) { measures, constraints ->
        width.value = constraints.maxWidth.div(3)
        val smallWidth = width.value.div(2)
        val card = measures[0].measure(constraints)
        val box1 = measures[1].measure(
            androidx.compose.ui.unit.Constraints.fixedWidth(smallWidth)
        )
        val box2 = measures[2].measure(
            androidx.compose.ui.unit.Constraints.fixedWidth(smallWidth)
        )

        btnWidthState.value.delBtnWidthState.value = smallWidth.dp
        positionOffset.setBounds(-smallWidth.toFloat(), 0f)
        layout(width = constraints.maxWidth, height = constraints.maxHeight) {
            card.placeRelative(
                positionOffset.value.roundToInt() + positionOffset.value.roundToInt(),
                0
            )
            box1.placeRelative(
                card.width + positionOffset.value.roundToInt() + positionOffset.value.roundToInt(),
                0
            )
            box2.placeRelative(
                card.width + positionOffset.value.roundToInt(),
                0
            )

        }
    }

}


@Composable
fun AvatarImage(url: Int) {
    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center,
        content = {
            CoilImage(
                data = url, modifier = Modifier.border(3.dp, Color.Black, CircleShape)
                    .preferredSize(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        })
}


/*Title and Date Items*/
@Composable
fun TitleRowContainer(content: @Composable () -> Unit) {
    Layout(
        content = content,
        modifier = Modifier.height(30.dp),
        measureBlock =
        { measurables, constraints ->
            val width = constraints.maxWidth.div(5)
            val title = measurables[0].measure(
                androidx.compose.ui.unit.Constraints.fixedWidth(width.times(3))
            )
            val date = measurables[1].measure(
                androidx.compose.ui.unit.Constraints.fixedWidth(width.times(2))
            )
            layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                title.place(x = 0, y = 0)
                date.place(
                    x = title.width,
                    y = (constraints.maxHeight - date.height).div(3)
                )
            }
        })
}

@Composable
fun TitleRow() {
    Text(
        text = "Hello",
        maxLines = 1,
        modifier = Modifier,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.h6
    )
    Row(content = {
        Text(
            text = "20-09-2020",
            maxLines = 1,
            modifier = Modifier.wrapContentWidth().padding(0.dp, 4.dp, 0.dp, 0.dp),
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(fontSize = 12.sp, color = Color.Gray)
        )
        Icon(
            imageVector = Icons.Filled.ArrowRight,
            modifier = Modifier.padding(0.dp),
            tint = Color.Gray
        )
    })
}


/*Bottom Drawer*/
@Composable
fun BottomStaticDrawerContent(
    btmDrawerState: BottomDrawerState,
    delBtnClicked: MutableState<Boolean>
) {
    Column(
        modifier = Modifier.background(color = Color.Transparent)
            .padding(10.dp, 0.dp, 10.dp, 10.dp),
        content = {
            Card(shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Column(
                        modifier = Modifier
                            .background(color = MaterialTheme.colors.surface),
                        content = {
                            Text(
                                text = "Would you like to delete this conversation? This conversation will be deleted from all of your devices.",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(
                                    20.dp,
                                    10.dp,
                                    20.dp,
                                    10.dp
                                ),
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 18.sp,
                                    fontFamily = SansSerif
                                )
                            )
                            Divider(color = Color.LightGray, thickness = 1.dp)
                            Button(
                                onClick = {
                                    btmDrawerState.close()
                                    delBtnClicked.value = true
                                },
                                content = {
                                    Text(
                                        text = "Delete",
                                        style = TextStyle(
                                            color = Color.Red,
                                            fontSize = 24.sp,
                                            fontFamily = SansSerif
                                        )
                                    )
                                },
                                colors = ButtonConstants.defaultButtonColors(
                                    backgroundColor = MaterialTheme.colors.surface
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(0)
                            )
                        })
                })

            Divider(thickness = 20.dp, color = Color.Transparent)

            Button(
                onClick = { btmDrawerState.close() },
                content = {
                    Text(
                        text = "Cancel",
                        style = TextStyle(
                            color = Color.Blue,
                            fontSize = 24.sp,
                            fontFamily = SansSerif
                        )
                    )
                },
                colors = ButtonConstants.defaultButtonColors(backgroundColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20)
            )
        })

}

@Composable
fun BottomStaticDrawerEvents(
    btmDrawerState: BottomDrawerState,
    delBtnState: MutableState<DeleteBtnAnimateState>,
    delBtnIconState: MutableState<DeleteBtnAnimateState>,
    flagDrawer: MutableState<Boolean>,
    delBtnClicked: MutableState<Boolean>,
    animatedSubItemCollapse: AnimatedValue<Int, AnimationVector1D>,
    itemModelState: MutableState<ItemModel>,
) {
    if (btmDrawerState.isOpen) {
        flagDrawer.value = true
    } else if (btmDrawerState.isClosed && flagDrawer.value && !delBtnClicked.value) {
        delBtnState.value = DeleteBtnAnimateState.IDLE
        delBtnIconState.value = DeleteBtnAnimateState.IDLE
    } else if (btmDrawerState.isClosed && delBtnClicked.value) {
        animatedSubItemCollapse.animateTo(
            -itemModelState.value.subItemHeight,
            tween(durationMillis = 500)
        )
        delBtnClicked.value = false
    }
}
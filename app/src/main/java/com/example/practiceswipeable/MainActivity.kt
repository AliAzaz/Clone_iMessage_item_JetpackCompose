package com.example.practiceswipeable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.animatedValue
import androidx.compose.animation.core.*
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.animation.FlingConfig
import androidx.compose.foundation.animation.fling
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.example.practiceswipeable.ui.PracticeSwipeableTheme
import com.example.practiceswipeable.utils.DeleteBtnAnimateState
import com.example.practiceswipeable.utils.iconPadding
import com.example.practiceswipeable.utils.transactionAnimationSetting
import com.example.practiceswipeable.utils.width
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


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

        val btnWidthState = remember { mutableStateOf(0.dp) }
        val btmDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
        val flagDrawer = remember { mutableStateOf(false) }
        val delBtnState = remember { mutableStateOf(DeleteBtnAnimateState.INITIAL) }
        val delBtnIconState = remember { mutableStateOf(DeleteBtnAnimateState.INITIAL) }
        val subItemWidth = remember { mutableStateOf(0) }
        val animatedSubItemCollapse = animatedValue(initVal = 0, converter = Int.VectorConverter)

        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(), children = {

            // Bottom Static Drawer
            BottomStaticDrawerInit(
                btmDrawerState,
                subItemWidth = subItemWidth,
                animatedSubItemCollapse = animatedSubItemCollapse
            )
            BottomStaticDrawerEvents(
                btmDrawerState = btmDrawerState,
                delBtnState = delBtnState,
                delBtnIconState = delBtnIconState,
                flagDrawer = flagDrawer,
            )

//        LazyColumnFor(items = listOf(0, 1), itemContent = { item ->
            MessageItemView(
                btnWidthState = btnWidthState,
                delBtnState = delBtnState,
                delBtnIconState = delBtnIconState,
                state = transactionAnimationSetting(
                    delBtnState = delBtnState,
                    minWidth = btnWidthState.value,
                    maxWidth = btnWidthState.value.times(3)
                ),
                btmDrawerState = btmDrawerState,
                flagDrawer = flagDrawer,
                subItemWidth = subItemWidth,
                animatedSubItemCollapse = animatedSubItemCollapse
            )
//        })

        })

    })


}


@Composable
fun MessageItemView(
    btnWidthState: MutableState<Dp>,
    delBtnState: MutableState<DeleteBtnAnimateState>,
    delBtnIconState: MutableState<DeleteBtnAnimateState>,
    state: TransitionState,
    btmDrawerState: BottomDrawerState,
    flagDrawer: MutableState<Boolean>,
    subItemWidth: MutableState<Int>,
    animatedSubItemCollapse: AnimatedValue<Int, AnimationVector1D>
) {

    Column(
        modifier = Modifier.preferredHeight(110.dp).layout { measurable, constraints ->
            val child = measurable.measure(constraints)
            subItemWidth.value = child.width
            layout(child.width, child.height) {
                child.place(animatedSubItemCollapse.value, 0)
            }
        }, children = {
            RowView(
                btnWidthState = btnWidthState,
                children = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .height(110.dp)
                            .padding(5.dp, 5.dp, 0.dp, 10.dp),
                        children = {
                            Row(modifier = Modifier, children = {
                                AvatarImage(url = R.drawable.scene_01)
                                Column(
                                    modifier = Modifier.padding(
                                        start = 10.dp,
                                        top = 5.dp,
                                        end = 15.dp
                                    ), children = {
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
                        modifier = Modifier.background(Color.Gray).height(110.dp),
                        icon = {
                            Icon(asset = Icons.Filled.Edit, tint = Color.White)
                        })


                    IconButton(
                        onClick = {
                            delBtnState.value = DeleteBtnAnimateState.EXPAND
                            delBtnIconState.value = DeleteBtnAnimateState.EXPAND
                            btmDrawerState.open()
                        },
                        modifier = Modifier.background(Color.Red).size(state[width], 110.dp),
                        icon = {
                            Icon(
                                asset = Icons.Filled.Delete,
                                tint = Color.White,
                                modifier = Modifier.padding(0.dp, 0.dp, state[iconPadding], 0.dp)
                            )
                        })
                },
                delBtnState = delBtnState,
                delBtnIconState = delBtnIconState,
                flagDrawer = flagDrawer
            )

            Divider(
                color = Color.LightGray,
                modifier = Modifier.fillMaxWidth().height(1.dp)
            )
        })
}


@Composable
fun Modifier.swipe(): Modifier {
    val threshold = .8f

    val width = remember { mutableStateOf(100) }

    val deleted = remember { mutableStateOf(false) }
    val positionOffset = animatedFloat(0f)
    val collapse = remember { mutableStateOf(0) }
    val animatedCollapse = animatedValue(initVal = 0, converter = Int.VectorConverter)

    return this.draggable(orientation = Orientation.Horizontal,
        onDragStarted = {
//            positionOffset.setBounds(-width.toFloat(), width.toFloat())
        },
        onDrag = { delta ->
            positionOffset.snapTo((positionOffset.value + delta).coerceAtMost(0f))
        },
        onDragStopped = { velocity ->
            val config =
                FlingConfig(anchors = listOf(-width.value.toFloat(), 0f, width.value.toFloat()))
            if (positionOffset.value.absoluteValue >= threshold) {
                positionOffset.fling(velocity, config) { _, endValue, _ ->
                    if (endValue != 0f) {
                        animatedCollapse.snapTo(collapse.value)
                        animatedCollapse.animateTo(0, onEnd = { _, _ ->

                        }, anim = tween(500))
                    }
                }
            }

        }
    ).then(
        layout { measures, constraints ->
            width.value = constraints.maxWidth / 5
            val card = measures.measure(constraints)
            positionOffset.setBounds(-width.value.toFloat(), width.value.toFloat())
            layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                card.placeRelative(positionOffset.value.roundToInt(), 0)
            }
        }
    )
}


@Composable
fun RowView(
    btnWidthState: MutableState<Dp>,
    modifier: Modifier = Modifier,
    children: @Composable () -> Unit,
    delBtnState: MutableState<DeleteBtnAnimateState>,
    delBtnIconState: MutableState<DeleteBtnAnimateState>,
    flagDrawer: MutableState<Boolean>,
) {
    val threshold = .8f
    val width = remember { mutableStateOf(0) }
    val positionOffset = animatedFloat(0f)
    val collapse = remember { mutableStateOf(0) }
    val animatedCollapse = animatedValue(initVal = 0, converter = Int.VectorConverter)

    val modify = modifier.draggable(
        orientation = Orientation.Horizontal,
        onDragStarted = {
        },
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
                    if (endValue != 0f) {
                        animatedCollapse.snapTo(collapse.value)
                        animatedCollapse.animateTo(0, onEnd = { _, _ ->

                        }, anim = tween(500))
                    }
                }
            }

        }
    )

    if (delBtnState.value == DeleteBtnAnimateState.IDLE) {
        positionOffset.animateTo(0f, tween(100))
        flagDrawer.value = false
    }

    Layout(children = children, modifier = modify) { measures, constraints ->
        width.value = constraints.maxWidth.div(3)
        val smallWidth = width.value.div(2)
        val card = measures[0].measure(constraints)
        val box1 = measures[1].measure(
            androidx.compose.ui.unit.Constraints.fixedWidth(smallWidth)
        )
        val box2 = measures[2].measure(
            androidx.compose.ui.unit.Constraints.fixedWidth(smallWidth)
        )

        btnWidthState.value = smallWidth.toDp()
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
        alignment = Alignment.Center,
        children = {
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
fun TitleRowContainer(children: @Composable () -> Unit) {
    Layout(children = children, modifier = Modifier.height(30.dp)) { measurables, constraints ->
        val width = constraints.maxWidth.div(5)
        val title = measurables[0].measure(
            androidx.compose.ui.unit.Constraints.fixedWidth(width.times(4))
        )
        val date = measurables[1].measure(
            androidx.compose.ui.unit.Constraints.fixedWidth(width)
        )
        layout(width = constraints.maxWidth, height = constraints.maxHeight) {
            title.place(x = 0, y = 0)
            date.place(
                x = title.width,
                y = (constraints.maxHeight - date.height).div(3)
            )
        }
    }
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
    Row(children = {
        Text(
            text = "20-09-2020",
            maxLines = 1,
            modifier = Modifier.wrapContentWidth().padding(0.dp, 4.dp, 0.dp, 0.dp),
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(fontSize = 12.sp, color = Color.Gray)
        )
        Icon(
            asset = Icons.Filled.ArrowRight,
            tint = Color.Gray,
            modifier = Modifier.padding(0.dp)
        )
    })
}


/*Bottom Drawer*/
@Composable
fun BottomStaticDrawerInit(
    btmDrawerState: BottomDrawerState,
    subItemWidth: MutableState<Int>,
    animatedSubItemCollapse: AnimatedValue<Int, AnimationVector1D>
) {
    BottomDrawerLayout(
        drawerState = btmDrawerState,
        drawerContent = {
            Column(
                modifier = Modifier.background(color = Color.Transparent)
                    .padding(10.dp, 0.dp, 10.dp, 10.dp),
                children = {
                    Card(shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Column(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colors.surface),
                                children = {
                                    Text(
                                        text = "Would you like to delete this conversation? This conversation will be deleted from all of your devices.",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(20.dp, 10.dp, 20.dp, 10.dp),
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
                                            animatedSubItemCollapse.animateTo(
                                                -subItemWidth.value,
                                                tween(durationMillis = 500)
                                            )
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
                                        backgroundColor = MaterialTheme.colors.surface,
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
                        backgroundColor = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20)
                    )
                })
        },
        bodyContent = {},
        gesturesEnabled = false,
        modifier = Modifier,
        drawerBackgroundColor = Color.LightGray,
        scrimColor = Color.LightGray
    )

}

@Composable
fun BottomStaticDrawerEvents(
    btmDrawerState: BottomDrawerState,
    delBtnState: MutableState<DeleteBtnAnimateState>,
    delBtnIconState: MutableState<DeleteBtnAnimateState>,
    flagDrawer: MutableState<Boolean>,
) {
    if (btmDrawerState.isOpen) {
        flagDrawer.value = true
    } else if (btmDrawerState.isClosed && flagDrawer.value) {
        delBtnState.value = DeleteBtnAnimateState.IDLE
        delBtnIconState.value = DeleteBtnAnimateState.IDLE
    }
}
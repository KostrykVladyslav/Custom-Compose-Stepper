package com.kostryk.customcomposestepper.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.DropShadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kostryk.customcomposestepper.ui.theme.PurplePrimary
import kotlin.math.roundToInt

@Composable
fun StepperBox(
    count: Int,
    offsetX: Float,
    scaleX: Float,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "pressScale"
    )

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .graphicsLayer(
                scaleX = scaleX * pressScale,
                scaleY = pressScale
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { interactionSource.tryEmit(PressInteraction.Press(it)) },
                    onDragEnd = {
                        interactionSource.tryEmit(
                            PressInteraction.Release(
                                PressInteraction.Press(
                                    Offset.Zero
                                )
                            )
                        )
                        onDragEnd()
                    },
                    onDragCancel = {
                        interactionSource.tryEmit(
                            PressInteraction.Cancel(
                                PressInteraction.Press(
                                    Offset.Zero
                                )
                            )
                        )
                        onDragEnd()
                    },
                    onDrag = { _, dragAmount -> onDrag(dragAmount) }
                )
            }
            .size(120.dp)
            .dropShadow(
                shape = RoundedCornerShape(32.dp),
                dropShadow = DropShadow(15.dp, PurplePrimary, 0.dp, alpha = 0.5f),
                offset = DpOffset(10.dp, 10.dp)
            )
            .clip(RoundedCornerShape(32.dp))
            .background(PurplePrimary),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(250),
                    initialOffsetX = { it / 2 }
                ) + fadeIn(tween(250)) togetherWith
                        slideOutHorizontally(
                            animationSpec = tween(250),
                            targetOffsetX = { -it / 2 }
                        ) + fadeOut(tween(250))
            },
            label = "StepperSlide"
        ) { targetCount ->
            Text(
                text = targetCount.toString(),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
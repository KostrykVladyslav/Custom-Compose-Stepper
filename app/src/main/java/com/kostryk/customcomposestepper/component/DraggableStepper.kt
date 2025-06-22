package com.kostryk.customcomposestepper.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kostryk.customcomposestepper.ui.theme.BackgroundLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun DraggableStepper() {
    val dragLimit = 150f
    val scope = rememberCoroutineScope()

    var rawOffsetX by remember { mutableFloatStateOf(0f) }
    val offsetX = remember { Animatable(0f) }
    var count by remember { mutableIntStateOf(0) }
    val leftIconAlpha = remember { Animatable(1f) }
    val rightIconAlpha = remember { Animatable(1f) }

    val direction: Direction by remember {
        derivedStateOf {
            when {
                rawOffsetX >= dragLimit -> Direction.Increase
                rawOffsetX <= -dragLimit -> Direction.Decrease
                else -> Direction.None
            }
        }
    }

    val currentDirection by rememberUpdatedState(newValue = direction)

    LaunchedEffect(currentDirection) {
        if (currentDirection != Direction.None) {
            var delayTime = 500L
            while (isActive) {
                count += when (currentDirection) {
                    Direction.Increase -> 1
                    Direction.Decrease -> -1
                    else -> 0
                }
                delay(delayTime)
                delayTime = (delayTime * 0.9f).toLong().coerceAtLeast(30L)
            }
        }
    }

    val scaleX by remember {
        derivedStateOf {
            1f + (offsetX.value / dragLimit) * 0.05f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FadeIcon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                alpha = leftIconAlpha.value
            )

            Spacer(modifier = Modifier.width(24.dp))

            StepperBox(
                count = count,
                offsetX = offsetX.value,
                scaleX = scaleX,
                onDrag = { delta ->
                    val newOffset = (rawOffsetX + delta.x).coerceIn(-dragLimit, dragLimit)
                    rawOffsetX = newOffset

                    scope.launch {
                        offsetX.snapTo(newOffset)
                        if (newOffset > 0) {
                            rightIconAlpha.animateTo(
                                targetValue = 1f - (newOffset / dragLimit).coerceIn(0f, 1f),
                                animationSpec = tween(100)
                            )
                        }
                        if (newOffset < 0) {
                            leftIconAlpha.animateTo(
                                targetValue = 1f - (-newOffset / dragLimit).coerceIn(0f, 1f),
                                animationSpec = tween(100)
                            )
                        }
                        if (newOffset.absoluteValue < dragLimit / 2) {
                            rightIconAlpha.animateTo(1f, tween(150))
                            leftIconAlpha.animateTo(1f, tween(150))
                        }
                    }
                },
                onDragEnd = {
                    rawOffsetX = 0f
                    scope.launch {
                        offsetX.animateTo(
                            0f,
                            spring(
                                dampingRatio = Spring.DampingRatioHighBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        leftIconAlpha.animateTo(1f, tween(300))
                        rightIconAlpha.animateTo(1f, tween(300))
                    }
                }
            )

            Spacer(modifier = Modifier.width(24.dp))

            FadeIcon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                alpha = rightIconAlpha.value
            )
        }
    }
}

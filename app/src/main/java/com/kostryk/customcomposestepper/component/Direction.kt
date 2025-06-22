package com.kostryk.customcomposestepper.component

sealed class Direction {
    object None : Direction()
    object Increase : Direction()
    object Decrease : Direction()
}
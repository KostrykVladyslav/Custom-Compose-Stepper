package com.kostryk.customcomposestepper

import com.kostryk.customcomposestepper.component.Direction
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StepperUnitTest {

    @Test
    fun `direction should be Increase when offsetX exceeds limit`() {
        val result = determineDirection(160f, 150f)
        assertEquals(Direction.Increase, result)
    }

    @Test
    fun `direction should be Decrease when offsetX is below -limit`() {
        val result = determineDirection(-151f, 150f)
        assertEquals(Direction.Decrease, result)
    }

    @Test
    fun `direction should be None when offsetX is in bounds`() {
        val result = determineDirection(0f, 150f)
        assertEquals(Direction.None, result)
    }

    @Test
    fun `scale should increase linearly based on offsetX`() {
        val scale = calculateScale(75f, 150f)
        assertEquals(1.025f, scale, 0.001f)
    }

    @Test
    fun `glow alpha should be 0 when direction is None`() {
        val alpha = calculateGlowAlpha(3, Direction.None)
        assertEquals(0f, alpha, 0.001f)
    }

    @Test
    fun `glow alpha should increase with count modulo 10 when direction is active`() {
        val alpha = calculateGlowAlpha(5, Direction.Increase)
        assertEquals(0.55f, alpha, 0.001f)
    }

    fun determineDirection(offsetX: Float, limit: Float): Direction =
        when {
            offsetX >= limit -> Direction.Increase
            offsetX <= -limit -> Direction.Decrease
            else -> Direction.None
        }

    fun calculateScale(offsetX: Float, limit: Float): Float =
        1f + (offsetX / limit) * 0.05f

    fun calculateGlowAlpha(count: Int, direction: Direction): Float =
        if (direction != Direction.None) 0.3f + (count % 10) * 0.05f else 0f
}
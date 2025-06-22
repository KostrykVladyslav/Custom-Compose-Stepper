package com.kostryk.customcomposestepper.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kostryk.customcomposestepper.ui.theme.PurpleIcon

@Composable
fun FadeIcon(
    imageVector: ImageVector,
    alpha: Float
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = PurpleIcon.copy(alpha = alpha),
        modifier = Modifier.size(40.dp)
    )
}
package com.kostryk.customcomposestepper

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.kostryk.customcomposestepper.component.Direction
import com.kostryk.customcomposestepper.component.DraggableStepper
import com.kostryk.customcomposestepper.ui.theme.CustomComposeStepperTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomComposeStepperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DraggableStepper()
                }
            }
        }
    }
}
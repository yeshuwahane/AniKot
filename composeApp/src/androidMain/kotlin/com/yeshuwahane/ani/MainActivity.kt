package com.yeshuwahane.ani

import App
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(
                    Color.TRANSPARENT, Color.WHITE
                ),
                navigationBarStyle = SystemBarStyle.light(
                    Color.TRANSPARENT, Color.TRANSPARENT
                )
            )
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
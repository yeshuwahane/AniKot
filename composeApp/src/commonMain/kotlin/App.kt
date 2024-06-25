import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import ani.composeapp.generated.resources.Res
import ani.composeapp.generated.resources.compose_multiplatform
import cafe.adriel.voyager.navigator.Navigator
import di.appModule
import org.koin.compose.KoinApplication
import presentation.MainScreen
import presentation.navigation.TabView

@Composable
@Preview
fun App() {

    KoinApplication(
        application = {
            modules(appModule())
        }
    ){
        MaterialTheme {
            Navigator(TabView())
        }
    }
}
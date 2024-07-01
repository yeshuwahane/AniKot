import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import cafe.adriel.voyager.navigator.Navigator
import di.appModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
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
            Scaffold { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color.Black)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .windowInsetsPadding(WindowInsets.systemBars)
                ){
                    Navigator(TabView())

                }

            }
        }
    }
}
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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
            Navigator(TabView())
        }
    }
}
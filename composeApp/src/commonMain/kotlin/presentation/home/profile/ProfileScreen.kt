package presentation.home.profile

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class ProfileScreen:Screen {
    @Composable
    override fun Content() {
        Text(text = "Profile")
    }
}
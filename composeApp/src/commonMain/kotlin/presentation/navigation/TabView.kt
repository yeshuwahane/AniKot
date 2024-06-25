package presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import presentation.home.dashboard.DashboardTab
import presentation.home.profile.ProfileTab

class TabView(): Screen {
    @Composable
    override fun Content() {
        TabViewSetup()
    }

}


@Composable
fun TabViewSetup(modifier: Modifier = Modifier) {
    TabNavigator(DashboardTab){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigation(

                ){
                    tabItems(DashboardTab)
                    tabItems(ProfileTab)
                }
            }
        ) {paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CurrentTab()
            }
        }
    }
}

@Composable
fun RowScope.tabItems(tab: Tab) {

    val navigator = LocalTabNavigator.current
    BottomNavigationItem(
        selected = navigator.current == tab,
        onClick = {
            navigator.current = tab
        },
        icon = {
            tab.options.icon?.let { painter ->
                Icon(painter, contentDescription = tab.options.title)
            }
        },
        modifier = Modifier
            .statusBarsPadding()
            .background(Color.Black),
        selectedContentColor = Color.Green,
        unselectedContentColor = Color.White
    )
}
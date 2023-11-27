package alex.eros.pokeappdex.navigation

import alex.eros.pokeappdex.home.HomeScreen
import alex.eros.pokeappdex.home.HomeViewModel
import alex.eros.pokeappdex.splash.ui.SplashScreen
import alex.eros.pokeappdex.splash.viewModels.SplashViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppHomeNavigation(homeViewModel: HomeViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HomeScreen.route){
        composable(Routes.HomeScreen.route){ HomeScreen(homeViewModel) }
    }
}
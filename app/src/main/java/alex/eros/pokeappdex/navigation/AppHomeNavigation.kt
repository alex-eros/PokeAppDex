package alex.eros.pokeappdex.navigation

import alex.eros.pokeappdex.splash.ui.SplashScreen
import alex.eros.pokeappdex.splash.viewModels.SplashViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppHomeNavigation(splashViewModel:SplashViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.SplashScreen.route){
        composable(Routes.SplashScreen.route){ SplashScreen(navController, splashViewModel = splashViewModel ) }
    }
}
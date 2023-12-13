package alex.eros.pokeappdex.navigation

import alex.eros.pokeappdex.home.HomeScreen
import alex.eros.pokeappdex.home.HomeViewModel
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppHomeNavigation(homeViewModel: HomeViewModel,intent: Intent?){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HomeScreen.route){
        composable(Routes.HomeScreen.route){ HomeScreen(homeViewModel,intent) }
    }
}
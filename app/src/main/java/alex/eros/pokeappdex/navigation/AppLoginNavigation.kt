package alex.eros.pokeappdex.navigation

import alex.eros.pokeappdex.login.ui.LoginScreen
import alex.eros.pokeappdex.login.ui.RecoveryPassScreen
import alex.eros.pokeappdex.login.ui.RegisterScreen
import alex.eros.pokeappdex.login.viewModels.LoginViewModel
import alex.eros.pokeappdex.login.viewModels.RecoveryPassViewModel
import alex.eros.pokeappdex.login.viewModels.RegisterViewModel
import alex.eros.pokeappdex.splash.ui.SplashScreen
import alex.eros.pokeappdex.splash.viewModels.SplashViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppLoginNavigation(loginViewModel:LoginViewModel,registerViewModel: RegisterViewModel, splashViewModel: SplashViewModel,recoveryPassViewModel: RecoveryPassViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.SplashScreen.route){
        composable(Routes.SplashScreen.route){ SplashScreen(navController, splashViewModel = splashViewModel ) }
        composable(Routes.LoginScreen.route){ LoginScreen(navController = navController, loginViewModel = loginViewModel) }
        composable(Routes.RegisterScreen.route){ RegisterScreen(navController = navController, registerViewModel = registerViewModel) }
        composable(Routes.RecoveryPassScreen.route){ RecoveryPassScreen(navController = navController, recoveryPassViewModel = recoveryPassViewModel) }
    }
}
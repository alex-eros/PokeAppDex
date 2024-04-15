package alex.eros.pokeappdex.navigation

import alex.eros.pokeappdex.login.ui.LoginScreen
import alex.eros.pokeappdex.login.ui.RecoveryPassScreen
import alex.eros.pokeappdex.login.ui.RegisterScreen
import alex.eros.pokeappdex.login.viewModels.LoginViewModel
import alex.eros.pokeappdex.login.viewModels.RecoveryPassViewModel
import alex.eros.pokeappdex.login.viewModels.RegisterViewModel
import alex.eros.pokeappdex.splash.ui.SplashScreen
import alex.eros.pokeappdex.splash.viewModels.SplashViewModel
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppLoginNavigation(loginViewModel:LoginViewModel,registerViewModel: RegisterViewModel, splashViewModel: SplashViewModel,recoveryPassViewModel: RecoveryPassViewModel,initialScreen:String){
    val TAG = "AppLoginNavigation"
    var previusRoute by rememberSaveable { mutableStateOf("") }
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination =initialScreen){
        composable(Routes.SplashScreen.route){ SplashScreen(navController, splashViewModel = splashViewModel ) }
        composable(Routes.LoginScreen.route){ LoginScreen(navController = navController, loginViewModel = loginViewModel) }
        composable(Routes.RegisterScreen.route,
            enterTransition = {
                scaleIn(
                    animationSpec = tween(220, delayMillis = 90),
                    initialScale = 0f
                ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
            },
            popExitTransition =  {
                scaleOut(
                    animationSpec = tween(
                        durationMillis = 220,
                        delayMillis = 90
                    ), targetScale = 0f
                ) + fadeOut(tween(delayMillis = 90))
            }
        ){ RegisterScreen(navController = navController, registerViewModel = registerViewModel) }
        composable(Routes.RecoveryPassScreen.route){ RecoveryPassScreen(navController = navController, recoveryPassViewModel = recoveryPassViewModel) }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (val currentRoute = navBackStackEntry?.destination?.route) {
        Routes.LoginScreen.route->{
            when{
                previusRoute.contains(Routes.RecoveryPassScreen.route) ->{
                    Log.d(TAG,"Limpiando valores de recovery")
                    recoveryPassViewModel.setOriginalValues()
                }
                previusRoute.contains(Routes.RegisterScreen.route )->{
                    Log.d(TAG,"Limpiando valores de Register")
                    registerViewModel.setOriginalValues()
                }
            }
        }
        Routes.RecoveryPassScreen.route->{
            loginViewModel.setOriginalValues()
            previusRoute =  Routes.RecoveryPassScreen.route
            Log.d(TAG,"Limpiando valores de login")
        }
        Routes.RegisterScreen.route->{
            loginViewModel.setOriginalValues()
            previusRoute =  Routes.RegisterScreen.route
            Log.d(TAG,"Limpiando valores de login")
        }
    }


}
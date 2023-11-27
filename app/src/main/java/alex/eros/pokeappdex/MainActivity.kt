package alex.eros.pokeappdex

import alex.eros.pokeappdex.login.viewModels.LoginViewModel
import alex.eros.pokeappdex.login.viewModels.RecoveryPassViewModel
import alex.eros.pokeappdex.login.viewModels.RegisterViewModel
import alex.eros.pokeappdex.navigation.AppLoginNavigation
import alex.eros.pokeappdex.navigation.Routes
import alex.eros.pokeappdex.splash.viewModels.SplashViewModel
import alex.eros.pokeappdex.ui.HomeActivity
import alex.eros.pokeappdex.ui.theme.PokeAppDexTheme
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object{
      var  isFirstLogin = true
    }

    val TAG = MainActivity::class.java.simpleName
    val loginViewModel: LoginViewModel by viewModels()
    val registerViewModel:RegisterViewModel by viewModels()
    val splashViewModel:SplashViewModel by viewModels ()
    val recoveryPassViewModel:RecoveryPassViewModel by viewModels()
    lateinit var initialScreen:String

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        init()
        if (MainActivity.isFirstLogin) initialScreen = Routes.SplashScreen.route else initialScreen = Routes.LoginScreen.route
        setContent {
            PokeAppDexTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    // set transparent color behind the status bar
                    systemUiController.setStatusBarColor(color = Color.Transparent)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   AppLoginNavigation(
                       loginViewModel = loginViewModel,
                       registerViewModel = registerViewModel,
                       splashViewModel = splashViewModel,
                       recoveryPassViewModel = recoveryPassViewModel,
                       initialScreen = initialScreen
                   )
                }
            }
        }
    }

    fun init(){
        splashViewModel.doLogin.observe(this, Observer {doLogin ->
            if (doLogin){
                launchHome()
            }
        })

        loginViewModel.doLogin.observe(this, Observer {doLogin ->
            if (doLogin){
              launchHome()
            }
        })

        registerViewModel.doLogin.observe(this, Observer {doLogin ->
            if (doLogin){
                launchHome()
            }
        })

    }

    fun launchHome(){
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


}

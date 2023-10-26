package alex.eros.pokeappdex

import alex.eros.pokeappdex.login.viewModels.LoginViewModel
import alex.eros.pokeappdex.login.viewModels.RecoveryPassViewModel
import alex.eros.pokeappdex.login.viewModels.RegisterViewModel
import alex.eros.pokeappdex.navigation.AppLoginNavigation
import alex.eros.pokeappdex.splash.viewModels.SplashViewModel
import alex.eros.pokeappdex.ui.HomeActivity
import alex.eros.pokeappdex.ui.theme.PokeAppDexTheme
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val TAG = MainActivity::class.java.simpleName
    private lateinit var window: Window
    val loginViewModel: LoginViewModel by viewModels()
    val registerViewModel:RegisterViewModel by viewModels()
    val splashViewModel:SplashViewModel by viewModels ()
    val recoveryPassViewModel:RecoveryPassViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContent {
            PokeAppDexTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   AppLoginNavigation(
                       loginViewModel = loginViewModel,
                       registerViewModel = registerViewModel,
                       splashViewModel = splashViewModel,
                       recoveryPassViewModel = recoveryPassViewModel,
                   )
                }
            }
        }
    }

    fun init(){
        window = this.getWindow()
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

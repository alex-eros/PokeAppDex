package alex.eros.pokeappdex.ui

import alex.eros.pokeappdex.navigation.AppHomeNavigation
import alex.eros.pokeappdex.splash.viewModels.SplashViewModel
import alex.eros.pokeappdex.ui.theme.PokeAppDexTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    val splashViewModel: SplashViewModel by viewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeAppDexTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppHomeNavigation(splashViewModel)
                }
            }
        }

    }


}
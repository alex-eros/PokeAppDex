package alex.eros.pokeappdex.ui

import alex.eros.pokeappdex.MainActivity
import alex.eros.pokeappdex.home.HomeViewModel
import alex.eros.pokeappdex.navigation.AppHomeNavigation
import alex.eros.pokeappdex.ui.theme.PokeAppDexTheme
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    val TAG = HomeActivity::class.java.simpleName
    val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        initObservers()
        homeViewModel.getUserInfo()
        setContent {
            PokeAppDexTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    // set transparent color behind the status bar
                    systemUiController.setStatusBarColor(color = Color.Transparent)
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppHomeNavigation(homeViewModel)
                }
            }
        }

    }

    private fun initObservers() {
       homeViewModel.launchHome.observe(this){ launch ->
           if (launch) launchLogin()
       }
    }

    private fun launchLogin(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}
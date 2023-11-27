package alex.eros.pokeappdex.ui

import alex.eros.pokeappdex.MainActivity
import alex.eros.pokeappdex.home.HomeViewModel
import alex.eros.pokeappdex.navigation.AppHomeNavigation
import alex.eros.pokeappdex.ui.theme.PokeAppDexTheme
import alex.eros.pokeappdex.utils.Cons
import alex.eros.pokeappdex.utils.SharedPrefs
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    val TAG = HomeActivity::class.java.simpleName
    val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        homeViewModel.getUserInfo()
        setContent {
            PokeAppDexTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
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
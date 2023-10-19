package alex.eros.pokeappdex.splash.ui

import alex.eros.pokeappdex.R
import alex.eros.pokeappdex.navigation.Routes
import alex.eros.pokeappdex.splash.viewModels.SplashViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController:NavController,splashViewModel:SplashViewModel){
    LaunchedEffect(key1 = true){
      if (!splashViewModel.validateSession()){
          delay(2000)
          navController.popBackStack()
          navController.navigate(Routes.LoginScreen.route)
      }else{
          //TODO:Descomentar para funcionamiento correcto
          //splashViewModel.doLogin()
          delay(2000)
          navController.popBackStack()
          navController.navigate(Routes.LoginScreen.route)
      }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF141414), Color(0xFF383838)),
                )
            ),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier.size(200.dp,200.dp)
            )
            Text(
                text = "PokeAppDex",
                color = Color(0xFFcfcfcf),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
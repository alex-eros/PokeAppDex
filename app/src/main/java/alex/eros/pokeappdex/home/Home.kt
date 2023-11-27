package alex.eros.pokeappdex.home

import alex.eros.pokeappdex.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun HomeScreen(homeViewModel: HomeViewModel){
    ConstraintLayout(Modifier.fillMaxSize()) {

        val (screen,errorMessage) = createRefs()

        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF383838))
                .constrainAs(screen) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
             Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(60.dp)
                     .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
                     .background(
                         Color.LightGray
                     ),
                 horizontalArrangement = Arrangement.Center
             ) {
                 Icon(
                     imageVector = ImageVector.vectorResource(id = R.drawable.ic_logout) ,
                     contentDescription = "logout",
                     modifier = Modifier.clickable {
                         homeViewModel.logOut()
                     }
                 )
             }   
            }
        }
        Box(modifier = Modifier
            .constrainAs(errorMessage) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(bottom = 20.dp, start = 40.dp, end = 40.dp)
            .shadow(8.dp)
        ) {

        }

    }
}
package alex.eros.pokeappdex.login.ui

import alex.eros.pokeappdex.R
import alex.eros.pokeappdex.dialogs.ErroMsgDialog
import alex.eros.pokeappdex.login.viewModels.RecoveryPassViewModel
import alex.eros.pokeappdex.utils.ScreenMetrics
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*

@Composable
fun RecoveryPassScreen(navController: NavController, recoveryPassViewModel:RecoveryPassViewModel) {

    val email:String by recoveryPassViewModel.email.observeAsState(initial = "")
    val buttonState:Boolean by recoveryPassViewModel.buttonState.observeAsState(initial = true)
    val loadingAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_lma3v0kz))
    val showAnimation:Boolean by recoveryPassViewModel.showAnimation.observeAsState(false)
    val isInvalidDataEmail:Boolean by recoveryPassViewModel.isInvalidDataEmail.observeAsState(false)
    val showDialog:Boolean by recoveryPassViewModel.showDialog.observeAsState(false)
    val isErrorMessage:Boolean by recoveryPassViewModel.isErrorMessage.observeAsState(false)
    val dialogMessage:String by recoveryPassViewModel.dialogMessage.observeAsState("Mensaje")
    val showErrorInvalidEmail:String by recoveryPassViewModel.showErrorInvalidEMail.observeAsState("Enter your registered email")
    val progress by animateLottieCompositionAsState(composition = loadingAnimation, isPlaying = showAnimation, iterations = LottieConstants.IterateForever)
    val marginVertical = ScreenMetrics.convertPercentageInDPforScreenHeight(3)

    ConstraintLayout(Modifier.fillMaxSize()){

        val (screen,errorMessage) = createRefs()

        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF383838))
                .constrainAs(screen) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = marginVertical.dp)
            ) {
                Row(modifier = Modifier
                    .weight(3f)
                    .padding(top = 12.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.pikachu_pass),
                        contentDescription = "welcome image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(400.dp)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .weight(.1f)
                        .fillMaxWidth()
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .weight(5f),
                ) {
                    Column(Modifier.align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.passport),
                            contentDescription = "header login section",
                            modifier = Modifier
                                .size(84.dp)
                        )
                    }
                    Column(Modifier.fillMaxSize()) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(top = 14.dp, bottom = 24.dp)
                                .fillMaxWidth(),
                        )
                        {
                            Text(
                                text = "Recovery PassPort",
                                fontSize = 16.sp,
                                color = Color(0xFF595959),
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily(Font(R.font.press_start2p))
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        {
                            OutlinedTextField(
                                value = email,
                                onValueChange = {recoveryPassViewModel.onEmailChanged(it)},
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = Color(0xFFbdbdbd),
                                    focusedBorderColor = Color(0xFF595959),
                                    cursorColor = Color(0xFF595959),
                                    textColor = Color(0xFF595959),
                                    errorBorderColor = Color(0xFFFF4757),
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                label = { Text(text = "Email", color = if(!isInvalidDataEmail) Color(0xFFbdbdbd) else Color(0xFFFF4757) , fontFamily = FontFamily(
                                    Font(R.font.press_start2p)
                                ),fontSize = 10.sp) },
                                placeholder = { Text(text = "pokefan@gmail.com", color = Color(0xFFbdbdbd)) },
                                maxLines = 1,
                                singleLine = true,
                                isError = isInvalidDataEmail
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )
                        {
                            Text(
                                text = showErrorInvalidEmail,
                                color = Color(0xFF595959),
                                modifier = Modifier
                                    .padding(horizontal = 28.dp)
                                    .fillMaxWidth(),
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(  horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {recoveryPassViewModel.evaluateEmail()},
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF595959), disabledBackgroundColor =  Color(0xA0595959)),
                                enabled = buttonState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp)
                            ) {
                                if (showAnimation) LottieAnimation(composition = loadingAnimation, modifier = Modifier.size(34.dp),progress={progress})
                                else Text(text = "SEND", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier
            .constrainAs(errorMessage) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(bottom = 48.dp, start = 40.dp, end = 40.dp)
            .shadow(8.dp)
        ) {
            AnimatedVisibility(
                visible = showDialog,
                enter = fadeIn() + expandHorizontally()
            ) {
                ErroMsgDialog(dialogMessage,isErrorMessage)
            }
        }
    }


}
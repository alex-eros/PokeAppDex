package alex.eros.pokeappdex.login.ui

import alex.eros.pokeappdex.R
import alex.eros.pokeappdex.dialogs.ErroMsgDialog
import alex.eros.pokeappdex.navigation.Routes
import alex.eros.pokeappdex.login.viewModels.LoginViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*

//TODO: ajustar el tamaño del lottie al del botón, ajustar animacion para repetirse indefinidamente
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {

    val email:String by loginViewModel.email.observeAsState(initial = "")
    val passWord:String by loginViewModel.password.observeAsState(initial = "")
    val buttonState:Boolean by loginViewModel.buttonState.observeAsState(initial = true)
    val loadingAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_lma3v0kz))
    val showAnimation:Boolean by loginViewModel.showAnimation.observeAsState(false)
    val showDialog:Boolean by loginViewModel.showDialog.observeAsState(false)
    val isInvalidDataEmail:Boolean by loginViewModel.isInvalidDataEmail.observeAsState(false)
    val isInvalidDataPassWord:Boolean by loginViewModel.isInvalidDataPassWord.observeAsState(false)
    val isErrorMessage:Boolean by loginViewModel.isErrorMessage.observeAsState(false)
    val dialogMessage:String by loginViewModel.dialogMessage.observeAsState("Mensaje")
    val showErrorInvalidEmail:String by loginViewModel.showErrorInvalidEMail.observeAsState("Enter your registered email")
    val showErrorInvalidPassWord:String by loginViewModel.showErrorInvalidPassWord.observeAsState("Enter your registered password")
    var showPassWord by rememberSaveable { mutableStateOf(false) }
    val progress by animateLottieCompositionAsState(composition = loadingAnimation, isPlaying = showAnimation, iterations = LottieConstants.IterateForever)
    val keepSessionActive:Boolean by loginViewModel.keepSessionActive.observeAsState(initial = false)

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
                modifier = Modifier.padding(top = 34.dp, bottom = 38.dp)
            ) {
                Row(modifier = Modifier
                    .weight(3f)
                    .padding(top = 12.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.profesor_oak),
                        contentDescription = "welcome image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(250.dp)
                            .width(150.dp)
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
                            painter = painterResource(id = R.drawable.poke_ball),
                            contentDescription = "header login section",
                            modifier = Modifier
                                .size(74.dp)
                        )
                        Row(modifier = Modifier
                            .padding(bottom = 14.dp)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "New Trainer?", color = Color(0xFFbdbdbd), modifier = Modifier.padding(end = 8.dp), fontWeight = FontWeight.Bold)
                            Text(text = "SIGN UP", color = Color(0xFF383838),fontFamily = FontFamily(Font(R.font.press_start2p)), fontSize = 12.sp, modifier = Modifier.clickable {
                                navController.navigate(Routes.RegisterScreen.route)
                            })
                        }
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
                                text = "Trainer Credentials",
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
                                onValueChange = {loginViewModel.onLoginChanged(email = it, password = passWord )},
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
                                label = { Text(text = "Email", color = if(!isInvalidDataEmail) Color(0xFFbdbdbd) else Color(0xFFFF4757) , fontFamily = FontFamily(Font(R.font.press_start2p)),fontSize = 10.sp) },
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
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = passWord,
                                onValueChange = {loginViewModel.onLoginChanged(email = email,it)},
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
                                label = { Text(text = "Password", color = if(!isInvalidDataPassWord) Color(0xFFbdbdbd) else Color(0xFFFF4757), fontFamily = FontFamily(Font(R.font.press_start2p)), fontSize = 12.sp) },
                                placeholder = { Text(text = "*********", color = Color(0xFFbdbdbd)) },
                                maxLines = 1,
                                singleLine = true,
                                visualTransformation = if (!showPassWord) PasswordVisualTransformation() else VisualTransformation.None,
                                trailingIcon = {
                                    IconButton(onClick = { showPassWord = !showPassWord }) {
                                        var currentIcon = R.drawable.ic_visibility_off
                                        var currentIconColor = Color.Black
                                        if (!showPassWord){
                                            currentIcon= R.drawable.ic_visibility_off
                                            if(isInvalidDataPassWord) currentIconColor = Color(0xFFFF4757)
                                             else currentIconColor = Color(0xA0595959)
                                        }else{
                                            currentIcon= R.drawable.ic_visibility
                                            if(isInvalidDataPassWord) currentIconColor = Color(0xFFFF4757)
                                            else currentIconColor = Color(0xFF383838)
                                        }
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = currentIcon),
                                            contentDescription = "",
                                            tint = currentIconColor
                                        )
                                    }
                                },
                                isError = isInvalidDataPassWord
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
                                text = showErrorInvalidPassWord,
                                color = Color(0xFF595959),
                                modifier = Modifier
                                    .padding(horizontal = 28.dp)
                                    .fillMaxWidth(),
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            CheckBoxTextReverse(tittle = "Remember me?", checkValue = keepSessionActive) { isActive ->
                                loginViewModel.rememberSession(isActive)
                            }
                            Text(
                                text = "Forgot your password?",
                                color = Color(0xFF595959),
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(end = 28.dp)
                                    .clickable { navController.navigate(Routes.RecoveryPassScreen.route) }
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(  horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {loginViewModel.verifyInfo()},
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF595959), disabledBackgroundColor =  Color(0xA0595959)),
                                enabled = buttonState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp)
                            ) {
                                if (showAnimation) LottieAnimation(composition = loadingAnimation, modifier = Modifier.size(34.dp),progress={progress})
                                else Text(text = "LOGIN", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
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
            .padding(bottom = 20.dp, start = 40.dp, end = 40.dp)
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

@Composable
fun CheckBoxTextReverse(tittle: String, checkValue: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp)
    ) {
        Checkbox(
            checked = checkValue, onCheckedChange = { onChange(it) },
            colors = CheckboxDefaults.colors(
                uncheckedColor = Color.Black,
                checkedColor = Color.LightGray
            ),
            modifier = Modifier.padding(end = 0.dp)
        )
        Text(
            text = tittle, fontSize = 14.sp,
            color = Color(0xFF595959),
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(start = 0.dp)
        )
    }
}
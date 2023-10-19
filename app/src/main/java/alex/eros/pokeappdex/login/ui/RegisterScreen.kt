package alex.eros.pokeappdex.login.ui

import alex.eros.pokeappdex.R
import alex.eros.pokeappdex.dialogs.ErroMsgDialog
import alex.eros.pokeappdex.login.viewModels.RegisterViewModel
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

//TODO: ajustar el tamaño del lottie al del botón, ajustar animacion para repetirse indefinidamente
//TODO: Agregar helperText para campo de ingreso de datos
@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel) {

    val checkMaleGenderState: Boolean by registerViewModel.maleGenderCheck.observeAsState(false)
    val checkGirlGenderState: Boolean by registerViewModel.girlGenderCheck.observeAsState(false)
    val nickname:String by registerViewModel.nickName.observeAsState("")
    val registeredEmail:String by registerViewModel.registeredEmail.observeAsState("")
    val registeredPassWord:String by registerViewModel.registeredPassword.observeAsState("")
    val buttonRegisterState:Boolean by registerViewModel.buttonRegisterState.observeAsState(false)
    val loadingAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_lma3v0kz))
    val showAnimation:Boolean by registerViewModel.showAnimation.observeAsState(false)
    val showErrorMessage:Boolean by registerViewModel.showErrorMessage.observeAsState(false)
    val exceptionMessage:String by registerViewModel.exceptionMessage.observeAsState("Ha ocurrido un error")

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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .weight(3f), horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pkm_trainer_boy),
                        contentDescription = "welcome image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(250.dp)
                            .width(150.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.pkm_trainer_girl),
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
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(top = 14.dp)
                                .fillMaxWidth(),
                        )
                        {
                            Text(
                                text = "Are You?",
                                fontSize = 16.sp,
                                color = Color(0xFF595959),
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily(Font(R.font.press_start2p))
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                                .fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CheckBoxText(tittle = "A boy", checkValue = checkMaleGenderState) {
                                registerViewModel.onCheckChanged(it, 0)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            CheckBoxText(tittle = "A girl", checkValue = checkGirlGenderState) {
                                registerViewModel.onCheckChanged(it, 1)
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                                .fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = nickname,
                                onValueChange = { registerViewModel.onRegisterChange(nickName = it, registeredEmail = registeredEmail, registeredPassWord = registeredPassWord ) },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = Color(0xFFbdbdbd),
                                    focusedBorderColor = Color(0xFF595959),
                                    cursorColor = Color(0xFF595959),
                                    textColor = Color(0xFF595959)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                label = {
                                    Text(
                                        text = "NickName",
                                        color = Color(0xFFbdbdbd),
                                        fontFamily = FontFamily(Font(R.font.press_start2p)),
                                        fontSize = 10.sp
                                    )
                                },
                                placeholder = { Text(text = "PokeManiac", color = Color(0xFFbdbdbd)) },
                                maxLines = 1,
                                singleLine = true
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )
                        {
                            Text(
                                text = "Enter ten characteres",
                                color = Color(0xFF595959),
                                modifier = Modifier.padding(horizontal = 28.dp).fillMaxWidth(),
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                                .fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = registeredEmail,
                                onValueChange = { registerViewModel.onRegisterChange(nickName = nickname, registeredEmail = it, registeredPassWord = registeredPassWord) },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = Color(0xFFbdbdbd),
                                    focusedBorderColor = Color(0xFF595959),
                                    cursorColor = Color(0xFF595959),
                                    textColor = Color(0xFF595959),
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                label = {
                                    Text(
                                        text = "Your Email",
                                        color = Color(0xFFbdbdbd),
                                        fontFamily = FontFamily(Font(R.font.press_start2p)),
                                        fontSize = 10.sp
                                    )
                                },
                                placeholder = { Text(text = "paletTown@hotmail.com", color = Color(0xFFbdbdbd)) },
                                maxLines = 1,
                                singleLine = true
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )
                        {
                            Text(
                                text = "Enter a valid email",
                                color = Color(0xFF595959),
                                modifier = Modifier.padding(horizontal = 28.dp).fillMaxWidth(),
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                                .fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = registeredPassWord,
                                onValueChange = { registerViewModel.onRegisterChange(nickName = nickname, registeredEmail = registeredEmail, registeredPassWord = it) },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = Color(0xFFbdbdbd),
                                    focusedBorderColor = Color(0xFF595959),
                                    cursorColor = Color(0xFF595959),
                                    textColor = Color(0xFF595959)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                label = {
                                    Text(
                                        text = "Your Password",
                                        color = Color(0xFFbdbdbd),
                                        fontFamily = FontFamily(Font(R.font.press_start2p)),
                                        fontSize = 10.sp
                                    )
                                },
                                placeholder = { Text(text = "*********", color = Color(0xFFbdbdbd)) },
                                maxLines = 1,
                                singleLine = true
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )
                        {
                            Text(
                                text = "Enter ten characteres",
                                color = Color(0xFF595959),
                                modifier = Modifier.padding(horizontal = 28.dp).fillMaxWidth(),
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                                .fillMaxWidth()
                        )
                        Row(  horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { if (showAnimation)return@Button else registerViewModel.registerTrainer() },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF595959)),
                                enabled = buttonRegisterState,
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(100.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    if (showAnimation)  LottieAnimation(composition = loadingAnimation, modifier = Modifier.size(26.dp))
                                    else Text(text = "LOGIN", fontSize = 12.sp,fontFamily = FontFamily(Font(R.font.press_start2p)))
                                }
                            }
                        }

                    }
                }
            }
        }
        if (showErrorMessage){
            Box(modifier = Modifier.constrainAs(errorMessage){
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }.padding(bottom = 20.dp, start = 40.dp, end = 40.dp)) {
                ErroMsgDialog(exceptionMessage)
            }
        }
    }
}

@Composable
fun CheckBoxText(tittle: String, checkValue: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tittle, fontSize = 12.sp,
            color = Color(0xFF595959),
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily(Font(R.font.press_start2p))
        )
        Checkbox(
            checked = checkValue, onCheckedChange = { onChange(it) },
            colors = CheckboxDefaults.colors(
                uncheckedColor = Color.Black,
                checkedColor = Color.LightGray
            )
        )
    }
}







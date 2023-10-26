package alex.eros.pokeappdex.login.ui

import alex.eros.pokeappdex.R
import alex.eros.pokeappdex.dialogs.ErroMsgDialog
import alex.eros.pokeappdex.login.viewModels.RegisterViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    val buttonRegisterState:Boolean by registerViewModel.buttonRegisterState.observeAsState(true)
    val loadingAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_lma3v0kz))
    val showAnimation:Boolean by registerViewModel.showAnimation.observeAsState(false)
    val showDialog:Boolean by registerViewModel.showDialog.observeAsState(false)
    val isErrorMessage:Boolean by registerViewModel.isErrorMessage.observeAsState(false)
    val dialogMessage:String by registerViewModel.dialogMessage.observeAsState("Mensaje")
    val isInvalidDataEmail:Boolean by registerViewModel.isInvalidDataEmail.observeAsState(false)
    val isInvalidDataPassWord:Boolean by registerViewModel.isInvalidDataPassWord.observeAsState(false)
    val isInvalidDataNickName:Boolean by registerViewModel.isInvalidDataNickName.observeAsState(false)
    val showErrorInvalidEmail:String by registerViewModel.showErrorInvalidEMail.observeAsState("Enter a valid email")
    val showErrorInvalidPassWord:String by registerViewModel.showErrorInvalidPassWord.observeAsState("Enter twelve characteres, at least one capital letter and one number")
    val showErrorInvalidNickName:String by registerViewModel.showErrorInvalidNickname.observeAsState("At most twelve characteres")
    var showPassWord by rememberSaveable { mutableStateOf(false) }

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
                                fontSize = 14.sp,
                                color = Color(0xFF595959),
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily(Font(R.font.press_start2p))
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(4.dp)
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
                                .height(4.dp)
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
                                    textColor = Color(0xFF595959),
                                    errorBorderColor = Color(0xFFFF4757)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                label = {
                                    Text(
                                        text = "NickName",
                                        color = if(!isInvalidDataNickName) Color(0xFFbdbdbd) else Color(0xFFFF4757),
                                        fontFamily = FontFamily(Font(R.font.press_start2p)),
                                        fontSize = 10.sp
                                    )
                                },
                                placeholder = { Text(text = "PokeManiac", color = Color(0xFFbdbdbd)) },
                                maxLines = 1,
                                singleLine = true,
                                isError = isInvalidDataNickName
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
                                text = showErrorInvalidNickName,
                                color = Color(0xFF595959),
                                modifier = Modifier
                                    .padding(horizontal = 28.dp)
                                    .fillMaxWidth(),
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
                                    errorBorderColor = Color(0xFFFF4757)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                label = {
                                    Text(
                                        text = "Your Email",
                                        color = if(!isInvalidDataEmail) Color(0xFFbdbdbd) else Color(0xFFFF4757),
                                        fontFamily = FontFamily(Font(R.font.press_start2p)),
                                        fontSize = 10.sp
                                    )
                                },
                                placeholder = { Text(text = "paletTown@hotmail.com", color = Color(0xFFbdbdbd)) },
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
                                    textColor = Color(0xFF595959),
                                    errorBorderColor = Color(0xFFFF4757)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                label = {
                                    Text(
                                        text = "Your Password",
                                        color = if(!isInvalidDataPassWord) Color(0xFFbdbdbd) else Color(0xFFFF4757),
                                        fontFamily = FontFamily(Font(R.font.press_start2p)),
                                        fontSize = 10.sp
                                    )
                                },
                                placeholder = { Text(text = "*********", color = Color(0xFFbdbdbd)) },
                                maxLines = 1,
                                singleLine = true,
                                isError = isInvalidDataPassWord,
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
                                }
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
                        Spacer(
                            modifier = Modifier
                                .height(28.dp)
                                .fillMaxWidth()
                        )
                        Row(  horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {registerViewModel.verifyInfo()},
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF595959), disabledBackgroundColor =  Color(0xA0595959)),
                                enabled = buttonRegisterState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp)
                            ) {
                                if (showAnimation) LottieAnimation(composition = loadingAnimation, modifier = Modifier.size(34.dp))
                                else Text(text = "SIGN UP", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
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
fun CheckBoxText(tittle: String, checkValue: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tittle, fontSize = 10.sp,
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







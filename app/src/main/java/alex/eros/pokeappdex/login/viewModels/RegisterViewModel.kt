package alex.eros.pokeappdex.login.viewModels

import alex.eros.pokeappdex.utils.Cons
import alex.eros.pokeappdex.utils.SharedPrefs
import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val application:Application
):ViewModel() {
    /*Gender
    0: Male
    1: Female
    * */

    private lateinit var timerError: CountDownTimer
    private val errorTimerCountDownValue:Long = 10000
    private var _gender:Int? = null

    private val _maleGenderCheck = MutableLiveData<Boolean>()
    val  maleGenderCheck: LiveData<Boolean> = _maleGenderCheck

    private val _girlGenderCheck = MutableLiveData<Boolean>()
    val  girlGenderCheck:LiveData<Boolean> = _girlGenderCheck

    private val _nickName = MutableLiveData<String>()
    val nickName:LiveData<String> = _nickName

    private val _registeredEmail = MutableLiveData<String>()
    val registeredEmail:LiveData<String> = _registeredEmail

    private val _registeredPassword = MutableLiveData<String>()
    val registeredPassword:LiveData<String> = _registeredPassword

    private val _buttonRegisterState = MutableLiveData<Boolean>()
    val buttonRegisterState:LiveData<Boolean> = _buttonRegisterState

    private val _showAnimation = MutableLiveData<Boolean>()
    val showAnimation:LiveData<Boolean> = _showAnimation

    private val _showErrorMessage = MutableLiveData<Boolean>()
    val showErrorMessage:LiveData<Boolean> = _showErrorMessage

    private val _exceptionMessage = MutableLiveData<String>()
    val exceptionMessage:LiveData<String> = _exceptionMessage

    private val _doLogin = MutableLiveData(false)
    val  doLogin: LiveData<Boolean> = _doLogin

    fun onRegisterChange(nickName:String, registeredEmail:String, registeredPassWord:String ){
        if (nickName.length <= 10) _nickName.value = nickName
        if (registeredEmail.length <= 20) _registeredEmail.value = registeredEmail
        if (registeredPassWord.length <= 12) _registeredPassword.value = registeredPassWord
        enableButton(registeredEmail,registeredPassWord)
    }

    fun onCheckChanged(value:Boolean,gender:Int){
        _gender = gender
        when(gender){
            1 -> {
                _maleGenderCheck.value = !value
                _girlGenderCheck.value = value
            }
            0 -> {
                _maleGenderCheck.value = value
                _girlGenderCheck.value = !value
            }
        }
    }

    fun registerTrainer(){
        if (_gender != null && !_nickName.value.isNullOrEmpty()){
            _showAnimation.postValue(true)
            firebaseAuth.createUserWithEmailAndPassword(registeredEmail.value!!,registeredPassword.value!!)
                .addOnCompleteListener { registerResult->
                    if (registerResult.isSuccessful){
                        setAnimationState(false)
                        with(SharedPrefs){
                            saveData(application.applicationContext,Cons.TRAINER_GENDER,_gender!!)
                            saveData(application.applicationContext,Cons.TRAINER_NICKNAME,_nickName.value!!)
                        }
                        doLogin()
                    }else{
                        setAnimationState(false)
                        _exceptionMessage.postValue(registerResult.exception?.message)
                        _showErrorMessage.value = true
                        startTimerCountDown()
                    }
                }
        }else{
            //TODO: Mandar mensaje de introduce tu genero o ingresa un nickmane valido
        }
    }

    private fun setAnimationState(state:Boolean){
        _showAnimation.postValue(state)
    }

    fun doLogin(){
        _doLogin.postValue(true)
    }

    fun startTimerCountDown(){

        viewModelScope.launch(Dispatchers.Main){
            timerError = object : CountDownTimer(errorTimerCountDownValue,1000){
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    _showErrorMessage.postValue(false)
                }
            }
            timerError.start()
        }

    }

    private fun enableButton(email: String, password: String) {
        _buttonRegisterState.value = !email.isNullOrBlank() && !password.isNullOrBlank() && password.length >10 && isValidEmailFormat(email)
    }

    private fun isValidEmailFormat(email: String?): Boolean =
        email?.let { Patterns.EMAIL_ADDRESS.matcher(email).matches() } ?: false



}
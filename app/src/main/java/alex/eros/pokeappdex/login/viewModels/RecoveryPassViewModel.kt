package alex.eros.pokeappdex.login.viewModels

import android.os.CountDownTimer
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryPassViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
):ViewModel() {

    private val TAG = RecoveryPassViewModel::class.java.simpleName

    private lateinit var timerError: CountDownTimer
    private val errorTimerCountDownValue:Long = 3000

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _buttonState = MutableLiveData<Boolean>()
    val buttonState: LiveData<Boolean> = _buttonState

    private val _showAnimation = MutableLiveData<Boolean>()
    val showAnimation:LiveData<Boolean> = _showAnimation

    private val _isInvalidDataEmail = MutableLiveData<Boolean>()
    val isInvalidDataEmail:LiveData<Boolean> = _isInvalidDataEmail

    private val _showErrorInvalidEmail = MutableLiveData<String>()
    val showErrorInvalidEMail:LiveData<String> = _showErrorInvalidEmail

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog:LiveData<Boolean> = _showDialog

    private val _isErrorMessage = MutableLiveData <Boolean>()
    val isErrorMessage:LiveData<Boolean> = _isErrorMessage

    private val _dialogMessage = MutableLiveData<String>()
    val dialogMessage:LiveData<String> = _dialogMessage


    fun onEmailChanged(email: String) {
        _email.value = email
    }

    private fun startTimerCountDown(){
        viewModelScope.launch(Dispatchers.Main){
            timerError = object : CountDownTimer(errorTimerCountDownValue,1000){
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    _showDialog.postValue(false)
                }
            }
            timerError.start()
        }
    }

    fun evaluateEmail(){
        _showAnimation.value = true
        _buttonState.value = false
        cleanValues()
        if (_email.value.isNullOrBlank()){
            setErrorEmail("Can't be empty")
        }else if (!isValidEmailFormat(_email.value)){
            setErrorEmail("Invalid format")
        }
        if (_isInvalidDataEmail.value!!) {
            _showAnimation.value = false
            _buttonState.value = true
        }else sendRecoveryEmail()
    }

    private fun sendRecoveryEmail() {
        firebaseAuth.sendPasswordResetEmail(_email.value!!)
            .addOnCompleteListener { sendEmailResult ->
                if (sendEmailResult.isSuccessful){
                    _showAnimation.value = false
                    _buttonState.value = true
                    _showDialog.value = true
                    _isErrorMessage.value = false
                    _dialogMessage.postValue("Email sent succesfully")
                    startTimerCountDown()
                }else{
                    _showAnimation.value = false
                    Log.e(TAG,"[loginTrainer] Ex:${sendEmailResult.exception?.message}")
                    _dialogMessage.postValue(sendEmailResult.exception?.message)
                    //TODO: Agregar mensajes cortos y descriptivos, para mostrar al usuario
                    _showDialog.value = true
                    _isErrorMessage.value = true
                    startTimerCountDown()
                   _buttonState.value = true
                }
            }
    }

    private fun isValidEmailFormat(email: String?): Boolean =
        email?.let { Patterns.EMAIL_ADDRESS.matcher(email).matches() } ?: false

    private fun setErrorEmail(message:String){
        _isInvalidDataEmail.value = true
        _showErrorInvalidEmail.value = message
    }

    private fun cleanValues() {
        _isInvalidDataEmail.value = false
        _showErrorInvalidEmail.value = ""
    }

    fun setOriginalValues(){
        if(::timerError.isInitialized) timerError.cancel()
        _email.value = ""
        _buttonState.value = true
        _showAnimation.value = false
        _showDialog.value = false
        _isInvalidDataEmail.value = false
        _showErrorInvalidEmail.value = "Enter your registered email"
    }

}
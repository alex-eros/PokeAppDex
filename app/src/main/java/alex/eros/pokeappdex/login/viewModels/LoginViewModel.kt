package alex.eros.pokeappdex.login.viewModels

import alex.eros.pokeappdex.splash.models.response.UserData
import alex.eros.pokeappdex.utils.Cons
import alex.eros.pokeappdex.utils.SharedPrefs
import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val application:Application
) : ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    private lateinit var timerError: CountDownTimer
    private val errorTimerCountDownValue:Long = 3000

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _buttonState = MutableLiveData<Boolean>()
    val buttonState: LiveData<Boolean> = _buttonState

    private val _showAnimation = MutableLiveData<Boolean>()
    val showAnimation:LiveData<Boolean> = _showAnimation

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog:LiveData<Boolean> = _showDialog

    private val _isErrorMessage = MutableLiveData <Boolean>()
    val isErrorMessage:LiveData<Boolean> = _isErrorMessage

    private val _dialogMessage = MutableLiveData<String>()
    val dialogMessage:LiveData<String> = _dialogMessage

    private val _doLogin = MutableLiveData<UserData>()
    val  doLogin: LiveData<UserData> = _doLogin

    private val _isInvalidDataEmail = MutableLiveData<Boolean>()
    val isInvalidDataEmail:LiveData<Boolean> = _isInvalidDataEmail

    private val _isInvalidDataPassWord = MutableLiveData<Boolean>()
    val isInvalidDataPassWord:LiveData<Boolean> = _isInvalidDataPassWord

    private val _showErrorInvalidEmail = MutableLiveData<String>()
    val showErrorInvalidEMail:LiveData<String> = _showErrorInvalidEmail

    private val _showErrorInvalidPassWord = MutableLiveData<String>()
    val showErrorInvalidPassWord:LiveData<String> = _showErrorInvalidPassWord

    private val _keepSesionActive = MutableLiveData(false)
    val keepSessionActive:LiveData<Boolean> = _keepSesionActive


    fun onLoginChanged(email: String, password: String) {
        _email.value = email
       if (password.length <= 12) _password.value = password
    }

    fun rememberSession(isActive:Boolean){
        _keepSesionActive.value = isActive
    }


    private fun loginTrainer() {
        viewModelScope.launch(Dispatchers.IO) {
            val deferredUserData = async { getUserData() }.await()
            if (deferredUserData.success){
                withContext(Dispatchers.Main){
                    SharedPrefs.saveData(application,Cons.REMEMBER_SESSION,_keepSesionActive.value!!)
                    setAnimationState(false)
                    enableButton(true)
                    doLogin(deferredUserData)
                }
            }else{
                withContext(Dispatchers.Main){
                    //TODO: Agregar mensajes cortos y descriptivos, para mostrar al usuario
                    Log.e(TAG,"[loginTrainer] Ex:${deferredUserData.message}")
                    setAnimationState(false)
                    _dialogMessage.postValue(deferredUserData.message)
                    _isErrorMessage.value = true
                    _showDialog.value = true
                    startTimerCountDown()
                    enableButton(true)
                }
            }
        }
    }

    private suspend fun getUserData():UserData{
        val userData = UserData(null,null,false,null)
        try {
            firebaseAuth.signInWithEmailAndPassword(_email.value!!, _password.value!!)
                .addOnCompleteListener { loginResult ->
                    if (loginResult.isSuccessful){
                        userData.success = true
                    }
                }.await()
        }catch (ex:Exception){
            with(userData){
                success = false
                message = ex.message
            }
        }
        if (userData.success){
            db.collection("Users").document(firebaseAuth.currentUser!!.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.data != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        with(document.data!!) {
                            userData.userNickName = get("nickname") as String?
                            Log.d(TAG, "Trainer Nickname: ${userData.userNickName}")
                            userData.gender = get("gender").toString().toInt()
                            Log.d(TAG, "Trainer Gender: ${userData.gender}")
                        }
                    }
                }.await()
        }
        return userData
    }
    private fun doLogin(userData: UserData){
        _doLogin.postValue(userData)
    }

    private fun setAnimationState(state:Boolean){
        _showAnimation.postValue(state)
    }

    fun startTimerCountDown(){

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

    private fun isValidEmailFormat(email: String?): Boolean =
        email?.let { Patterns.EMAIL_ADDRESS.matcher(email).matches() } ?: false



    private fun enableButton(state:Boolean) {
        _buttonState.value = state
    }

    fun verifyInfo(){
        enableButton(false)
        setAnimationState(true)
        cleanValues()
        viewModelScope.launch {
            delay(5000)
            if (_email.value.isNullOrBlank()){
                setErrorEmail("Can't be empty")
            }else if (!isValidEmailFormat(_email.value)){
                setErrorEmail("Invalid format")
            }

            if (_password.value.isNullOrBlank()){
                setErrorPassWord("Can't be empty")
            }else if (_password.value!!.length < 12){
                setErrorPassWord("Short password")
            }

            if (_isInvalidDataEmail.value!! || _isInvalidDataPassWord.value!!) {
                setAnimationState(false)
                enableButton(true)
            }
            else loginTrainer()
        }
    }

    private fun cleanValues() {
        _isInvalidDataEmail.value = false
        _isInvalidDataPassWord.value = false
        _showErrorInvalidEmail.value = ""
        _showErrorInvalidPassWord.value = ""
    }

    private fun setErrorEmail(message:String){
        _isInvalidDataEmail.value = true
        _showErrorInvalidEmail.value = message
    }

    private fun setErrorPassWord(message: String){
        _isInvalidDataPassWord.value = true
        _showErrorInvalidPassWord.value = message
    }

    fun setOriginalValues() {
        if (::timerError.isInitialized) timerError.cancel()
        _email.value = ""
        _password.value = ""
        _showAnimation.value = false
        _isInvalidDataPassWord.value = false
        _isInvalidDataEmail.value = false
        _showErrorInvalidPassWord.value = "Enter your registered password"
        _showErrorInvalidEmail.value = "Enter your regitered email"
        _showDialog.value = false
    }


}
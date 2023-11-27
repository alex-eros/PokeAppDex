package alex.eros.pokeappdex.login.viewModels

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val application:Application,
    private val db:FirebaseFirestore
):ViewModel() {
    /*Gender
    0: Male
    1: Female
    * */

    private val TAG = RegisterViewModel::class.java.simpleName
    private lateinit var timerError: CountDownTimer
    private val errorTimerCountDownValue:Long = 3000
    private var _gender:Int? = null
    private var passWordErrorMessage = ""

    private val _maleGenderCheck = MutableLiveData(false)
    val  maleGenderCheck: LiveData<Boolean> = _maleGenderCheck

    private val _girlGenderCheck = MutableLiveData(false)
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

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog:LiveData<Boolean> = _showDialog

    private val _isErrorMessage = MutableLiveData <Boolean>()
    val isErrorMessage:LiveData<Boolean> = _isErrorMessage

    private val _dialogMessage = MutableLiveData<String>()
    val dialogMessage:LiveData<String> = _dialogMessage

    private val _isInvalidDataEmail = MutableLiveData<Boolean>()
    val isInvalidDataEmail:LiveData<Boolean> = _isInvalidDataEmail

    private val _isInvalidDataPassWord = MutableLiveData<Boolean>()
    val isInvalidDataPassWord:LiveData<Boolean> = _isInvalidDataPassWord

    private val _isInvalidDataNickName = MutableLiveData<Boolean>()
    val isInvalidDataNickName:LiveData<Boolean> = _isInvalidDataNickName

    private val _showErrorInvalidEmail = MutableLiveData<String>()
    val showErrorInvalidEMail:LiveData<String> = _showErrorInvalidEmail

    private val _showErrorInvalidPassWord = MutableLiveData<String>()
    val showErrorInvalidPassWord:LiveData<String> = _showErrorInvalidPassWord

    private val _showErrorInvalidNickName = MutableLiveData<String>()
    val showErrorInvalidNickname:LiveData<String> = _showErrorInvalidNickName

    private val _doLogin = MutableLiveData(false)
    val  doLogin: LiveData<Boolean> = _doLogin

    fun onRegisterChange(nickName:String, registeredEmail:String, registeredPassWord:String ){
        if (nickName.length <= 10) _nickName.value = nickName
        _registeredEmail.value = registeredEmail
        if (registeredPassWord.length <= 12) _registeredPassword.value = registeredPassWord
    }

    fun onCheckChanged(value:Boolean,gender:Int){
        _gender = gender
        when(gender){
            1 -> {
                if (_girlGenderCheck.value == true) _girlGenderCheck.value = false
                else{
                    _maleGenderCheck.value = !value
                    _girlGenderCheck.value = value
                }
            }
            0 -> {
                if (_maleGenderCheck.value == true) _maleGenderCheck.value = false
                else{
                    _maleGenderCheck.value = value
                    _girlGenderCheck.value = !value
                }
            }
        }
    }

    fun registerTrainer(){
            firebaseAuth.createUserWithEmailAndPassword(registeredEmail.value!!,registeredPassword.value!!)
                .addOnCompleteListener { registerResult->
                    if (registerResult.isSuccessful){
                        setAnimationState(false)
                        saveUserDataShared()
                        saveUserDataRemoteDB()
                        doLogin()
                    }else{
                        setAnimationState(false)
                        _dialogMessage.postValue(registerResult.exception?.message)
                        _isErrorMessage.postValue(true)
                        _showDialog.value = true
                        startTimerCountDown()
                    }
                }
    }

    private fun saveUserDataRemoteDB() {
        val currentUserUID = firebaseAuth.currentUser?.uid
        if (currentUserUID != null){
            val userData = hashMapOf(
                "nickname" to _nickName.value,
                "gender" to _gender
            )
            val users = db.collection("Users")
            users.document(currentUserUID).set(userData)
        }
        else {
            Log.d(TAG,"[saveUserDataRemoteDB] there isn´t a user")
            return
        }
    }

    private fun setAnimationState(state:Boolean){
        _showAnimation.postValue(state)
    }

     private fun saveUserDataShared(){
        try {
            with(SharedPrefs){
                saveData(application.applicationContext,Cons.CURRENT_TRAINER_GENDER,_gender!!)
                saveData(application.applicationContext,Cons.CURRENT_TRAINER_NICKNAME,_nickName.value!!)
            }
        }catch (ex:Exception){
            Log.d(TAG,"[saveUserDataShared] Error:${ex.message} ")
        }
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
                    _showDialog.postValue(false)
                }
            }
            timerError.start()
        }

    }

    fun verifyInfo(){
        cleanValues()
        enableButton(false)
        setAnimationState(true)
        val isGenderSelected = _maleGenderCheck.value!! || _girlGenderCheck.value!!
        if (!isGenderSelected){
            _dialogMessage.postValue("Choose a gender")
            _showDialog.value = true
            _isErrorMessage.value = true
            startTimerCountDown()
        }
        if (_nickName.value.isNullOrEmpty()) setErrorNickName("Can´t be empty")

        if (_registeredEmail.value.isNullOrBlank()){
            setErrorEmail("Can't be empty")
        }else if (!isValidEmailFormat(_registeredEmail.value)){
            setErrorEmail("Invalid format")
        }

        if (_registeredPassword.value.isNullOrBlank()){
            setErrorPassWord("Can't be empty")
        }else if (!isValidPassWordFormat()){
            setErrorPassWord(passWordErrorMessage)
        }

        if (_isInvalidDataEmail.value!! || _isInvalidDataPassWord.value!! || isInvalidDataNickName.value!! || !isGenderSelected){
            setAnimationState(false)
            enableButton(true)
        }else registerTrainer()
    }

    private fun cleanValues() {
        _isInvalidDataEmail.value = false
        _isInvalidDataPassWord.value = false
        _showErrorInvalidEmail.value = ""
        _showErrorInvalidPassWord.value = ""
        _isInvalidDataNickName.value = false
        _showErrorInvalidNickName.value = ""
    }

    private fun setErrorEmail(message:String){
        _isInvalidDataEmail.value = true
        _showErrorInvalidEmail.value = message
    }

    private fun setErrorPassWord(message: String){
        _isInvalidDataPassWord.value = true
        _showErrorInvalidPassWord.value = message
    }

    private fun setErrorNickName(message: String){
        _isInvalidDataNickName.value = true
        _showErrorInvalidNickName.value = message
    }

    private fun enableButton(enable:Boolean) {
        _buttonRegisterState.value = enable
    }

    private fun isValidEmailFormat(email: String?): Boolean =
        email?.let { Patterns.EMAIL_ADDRESS.matcher(email).matches() } ?: false

    private fun isValidPassWordFormat():Boolean{
        var hasCaptialLetter = false
        var hasNumber = false
        val isShortPassWord = _registeredPassword.value!!.length < 12

        for( character in _registeredPassword.value!!){
            if (character.isUpperCase()) hasCaptialLetter = true
            if (character.isDigit()) hasNumber = true
        }

        passWordErrorMessage = when{
            !hasCaptialLetter && !hasNumber && isShortPassWord -> "Short password, missing capital letter, missing number"
            hasCaptialLetter && !hasNumber && !isShortPassWord  ->  "Missing Number"
            !hasCaptialLetter && hasNumber && !isShortPassWord -> "Missing capital letter"
            hasCaptialLetter && hasNumber && isShortPassWord -> "Short password"
            !hasCaptialLetter && hasNumber && isShortPassWord -> "Short password, missing capital letter"
            hasCaptialLetter && !hasNumber && isShortPassWord -> "Short password, missing number"
            !hasCaptialLetter && !hasNumber && !isShortPassWord -> "Missing capital letter, missing number"
            else -> ""
        }

        return (hasCaptialLetter && hasNumber && !isShortPassWord)
    }

    fun setOriginalValues(){
        if (::timerError.isInitialized) timerError.cancel()
        _maleGenderCheck.value = false
        _girlGenderCheck.value = false
        _registeredEmail.value = ""
        _registeredPassword.value = ""
        _nickName.value = ""
        _buttonRegisterState.value = true
        _showAnimation.value = false
        _showDialog.value = false
        _isInvalidDataPassWord.value = false
        _isInvalidDataEmail.value = false
        _isInvalidDataNickName.value = false
        _showErrorInvalidEmail.value = "Enter a valid Email"
        _showErrorInvalidPassWord.value = "Enter twelve characteres, at least one capital letter and one number"
        _showErrorInvalidNickName.value = "At most twelve characteres"
    }


}
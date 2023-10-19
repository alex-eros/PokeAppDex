package alex.eros.pokeappdex.splash.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
):ViewModel() {

    private val TAG = SplashViewModel::class.simpleName

    private val _doLogin = MutableLiveData(false)
    val  doLogin: LiveData<Boolean> = _doLogin

    fun validateSession():Boolean{
        try {
            val currentUser = firebaseAuth.currentUser
            return currentUser != null
        } catch (ex: Exception) {
            Log.e(TAG, "[validateSession] Ex:${ex.message} ")
        }
        return false
    }

    fun doLogin(){
        _doLogin.postValue(true)
    }

}
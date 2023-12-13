package alex.eros.pokeappdex.home

import alex.eros.pokeappdex.MainActivity
import alex.eros.pokeappdex.utils.Cons
import alex.eros.pokeappdex.utils.SharedPrefs
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val application: Application
) : ViewModel() {

    val TAG = HomeViewModel::class.java.simpleName

    private val _launchHome = MutableLiveData(false)
    val launchHome: LiveData<Boolean> = _launchHome

    private val _trainerNickName = MutableLiveData("Trainer")
    val  trainerNickName: LiveData<String> = _trainerNickName

    fun logOut() {
        if (firebaseAuth.currentUser != null) firebaseAuth.signOut()
        MainActivity.isFirstLogin = false
        _launchHome.value = true
    }

    //Mover
    /* private fun checkSavedUserNickName(){
         val nickName = SharedPrefs.getStringData(application, Cons.CURRENT_TRAINER_NICKNAME,"")
         if (nickName.isNotEmpty()) _trainerNickName.value = nickName
     }*/

    fun keepSessionActive(){
        if (!SharedPrefs.getBooleanData(application, Cons.REMEMBER_SESSION,false)) firebaseAuth.signOut()
    }





}
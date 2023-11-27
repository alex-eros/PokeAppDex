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

    fun getUserInfo(){
        val currentUserUID = firebaseAuth.currentUser?.uid
        if (currentUserUID != null){
            val docRef = db.collection("Users").document(currentUserUID)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        document.data?.get("nickname").let {
                            _trainerNickName.value = it as String?
                            Log.d(TAG, "Trainer Nickname: ${trainerNickName.value}")
                        }
                    } else {
                        checkSavedUserNickName()
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }else{
            Log.d(TAG,"[init] there isn´t a user")
            checkSavedUserNickName()
            return
        }
       if (!keepSessionActive()) firebaseAuth.signOut()
    }

    private fun keepSessionActive():Boolean{
        return SharedPrefs.getBooleanData(application, Cons.REMEMBER_SESSION,false)
    }

    private fun checkSavedUserNickName(){
      val nickName = SharedPrefs.getStringData(application,Cons.CURRENT_TRAINER_NICKNAME,"")
      if (nickName.isNotEmpty()) _trainerNickName.value = nickName
    }



}
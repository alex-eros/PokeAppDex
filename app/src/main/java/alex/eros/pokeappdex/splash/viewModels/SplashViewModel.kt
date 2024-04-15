package alex.eros.pokeappdex.splash.viewModels

import alex.eros.pokeappdex.splash.models.response.UserData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val TAG = "SplashViewModel"

    private val _doLogin = MutableLiveData<UserData>()
    val doLogin: LiveData<UserData> = _doLogin

    suspend fun getUserInfo(): UserData {
        val currentUserUID = firebaseAuth.currentUser?.uid
        var userData = UserData(null,null,false,null)
        if (currentUserUID != null) {
            db.collection("Users").document(currentUserUID).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.data != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        with(document.data!!) {
                            val userNickName = get("nickname") as String?
                            Log.d(TAG, "Trainer Nickname: $userNickName")
                            val gender = get("gender").toString()
                            Log.d(TAG, "Trainer Gender: $gender")
                            userData = UserData(userNickName, gender.toInt(), true,null)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                    userData = UserData(null, null, false,null)
                }.await()
        }else  Log.d(TAG, "There is not a user")
        return userData
    }

    fun doLogin(userData: UserData) {
        _doLogin.postValue(userData)
    }

}
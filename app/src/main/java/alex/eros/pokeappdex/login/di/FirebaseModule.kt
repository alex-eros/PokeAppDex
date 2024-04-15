package alex.eros.pokeappdex.login.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    fun provideFirebaseAuthInstance():FirebaseAuth{
        return Firebase.auth
    }

    @Provides
    fun provideFirebaseDataBaseInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

}
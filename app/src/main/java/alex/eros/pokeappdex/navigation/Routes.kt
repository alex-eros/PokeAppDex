package alex.eros.pokeappdex.navigation

/*TODO: Agregar envío de parametros entre pantallas*/
sealed class Routes(val route:String){
    object SplashScreen: Routes("Splash")
    object LoginScreen: Routes("Login")
    object RegisterScreen: Routes("Register")
    object RecoveryPassScreen: Routes("RecoveryPass")
}

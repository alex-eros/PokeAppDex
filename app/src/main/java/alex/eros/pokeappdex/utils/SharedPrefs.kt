package alex.eros.pokeappdex.utils

import android.content.Context

class SharedPrefs {

    companion object{

        fun getBooleanData(context:Context,key:String,defValue:Boolean):Boolean{
            return  context.getSharedPreferences(Cons.PREFENRENCES_NAME,Context.MODE_PRIVATE).getBoolean(key,defValue)
        }

        fun getStringData(context:Context,key:String,defValue:String):String{
            return context.getSharedPreferences(Cons.PREFENRENCES_NAME,Context.MODE_PRIVATE).getString(key,defValue)!!
        }

        fun getIntData(context:Context,key:String,defValue:Int):Int{
            return  context.getSharedPreferences(Cons.PREFENRENCES_NAME,Context.MODE_PRIVATE).getInt(key,defValue)
        }

        fun getLongData(context:Context,key:String,defValue:Long):Long{
            return  context.getSharedPreferences(Cons.PREFENRENCES_NAME,Context.MODE_PRIVATE).getLong(key,defValue)
        }

        fun saveData(context: Context,key:String,value:String){
            context.getSharedPreferences(Cons.PREFENRENCES_NAME,Context.MODE_PRIVATE).edit().putString(key,value).apply()
        }

        fun saveData(context: Context,key:String,value:Int){
            context.getSharedPreferences(Cons.PREFENRENCES_NAME,Context.MODE_PRIVATE).edit().putInt(key,value).apply()
        }

        fun saveData(context: Context,key:String,value:Boolean){
            context.getSharedPreferences(Cons.PREFENRENCES_NAME,Context.MODE_PRIVATE).edit().putBoolean(key,value).apply()
        }

        fun saveData(context: Context,key:String,value:Long){
            context.getSharedPreferences(Cons.PREFENRENCES_NAME,Context.MODE_PRIVATE).edit().putLong(key,value).apply()
        }

        fun deleteData(context: Context,key:String){
            context.getSharedPreferences(Cons.PREFENRENCES_NAME,Context.MODE_PRIVATE).edit().remove(key).apply()
        }
    }

}
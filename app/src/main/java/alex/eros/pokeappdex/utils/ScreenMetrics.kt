package alex.eros.pokeappdex.utils

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log

object ScreenMetrics {
    private val TAG = ScreenMetrics::class.java.simpleName
    private val displayMetrics = DisplayMetrics()
    private var deviceScreenHeight = 0
    private var deviceScreenWidth = 0
    private var density = 0
    private const val height = "HEIGHT"
    private const val width = "WIDTH"

    fun getScreenCharacteristics(activity: Activity){
        getScreenDimensions(activity)
        getScreenDensity(activity)
    }

    private fun getScreenDimensions(activity: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = activity.windowManager.currentWindowMetrics
                deviceScreenWidth = windowMetrics.bounds.width()
                deviceScreenHeight = windowMetrics.bounds.height()
                Log.d(TAG,"[getScreenDimensions] Height:$deviceScreenHeight , Width:$deviceScreenWidth ")
            } else {
                deviceScreenHeight = displayMetrics.heightPixels
                deviceScreenWidth = displayMetrics.widthPixels
                Log.d(TAG,"[getScreenDimensions] Height:$deviceScreenHeight , Width:$deviceScreenWidth ")
            }
        }catch (ex:Exception){
           Log.e(TAG,ex.message.toString())
        }
    }

    @Suppress("DEPRECATION")
    private fun getScreenDensity(activity: Activity){
        try {
            density = if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)){
                val windowMetrics = activity.windowManager.currentWindowMetrics
                windowMetrics.density.toInt()
            }else{
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.density.toInt()
            }
            Log.d(TAG,"[getScreenDensity] density:$density")
        }catch (ex:Exception){
            Log.e(TAG,ex.message.toString())
        }

    }

    fun convertPercentageInDPforScreenHeight(percentage:Int):Int{
        var dp = 0
        if (deviceScreenHeight > 0){
            val pixels = getPixelsAccordingPercentage(percentage, height)
            dp = (pixels / density)
            Log.d(TAG,"[convertPercentageInPixelsforScreenHeight] dp: $dp")
        }else{
            Log.d(TAG,"It couldnt get screen height size")
        }
        return dp
    }

    fun convertPercentageInDPforScreenWidth(percentage:Int):Int{
        var dp = 0
        if (deviceScreenWidth > 0){
            val pixels = getPixelsAccordingPercentage(percentage, width)
            dp = (pixels / density)
        }else{
            Log.d(TAG,"It couldnt get screen width size")
        }
        return dp
    }

    private fun getPixelsAccordingPercentage(percentage:Int,dimension:String):Int =
        if (dimension == height) (percentage * deviceScreenHeight) / 100 else (percentage * deviceScreenWidth) /100


    /*Test function to convert dip to pixels
    fun convertDipToPixels(){
        val dip = 34f
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip, displayMetrics)
        Log.d(TAG,"DP conversion to px = $px")

    }
    */

}
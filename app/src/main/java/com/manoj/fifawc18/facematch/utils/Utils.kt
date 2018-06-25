package com.manoj.fifawc18.facematch.utils

import android.content.Context
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager



class Utils {

    companion object {
        @JvmStatic
        fun formatPlayerName(originalName: String): String {
            val words = originalName.split(" ")
            var result = ""
            for(word in words) {
                result = result + word.toLowerCase().capitalize() + " "
            }
            result = result.removeSuffix(" ")
            return result
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

}
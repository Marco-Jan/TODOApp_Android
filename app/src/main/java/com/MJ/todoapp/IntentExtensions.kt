package com.MJ.todoapp


import android.content.Intent
import android.os.Build
import android.os.Parcelable

//hiermit wird auch bei geräten mit neueren versionen altes Intent korrekt ausgeführt

@Suppress("DEPRECATION")
inline fun <reified T: Parcelable> Intent.getParcelableExtraProvider(identifierParameter: String): T?{
    return if (Build.VERSION.SDK_INT >=33){
        this.getParcelableExtra(identifierParameter, T::class.java)
    }else{
        this.getParcelableExtra(identifierParameter)
    }
}
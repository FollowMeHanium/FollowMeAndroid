package com.ghdev.followme.data

import android.util.Base64
import android.util.Log
import java.io.UnsupportedEncodingException


class JWTDecode {
   fun DecodeToken(acckey : String)  : String{
        lateinit var userJWT : List<String>
        try{
            val split : List<String> = acckey.split(".")
            val tempJWT : List<String> = getJson(split[1]).split(",")
            userJWT = tempJWT[1].split(":")

        }catch(e : Exception){
            Log.d("JWT_DECODED", "split error: " + e)
        }
        return userJWT[1]
    }

     fun getJson(strEncoded : String) : String{
        lateinit var decodeBytes : ByteArray
        try{
            var decodeBytes : ByteArray = Base64.decode(strEncoded, Base64.URL_SAFE)
            return decodeBytes.toString(Charsets.UTF_8)
        }catch(e : UnsupportedEncodingException){
            Log.d("JWT_DECODED", "getJson error: " + e)
        }
        return decodeBytes.toString(Charsets.UTF_8)
    }
}
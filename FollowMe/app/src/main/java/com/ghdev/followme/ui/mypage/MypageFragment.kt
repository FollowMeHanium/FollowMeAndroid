package com.ghdev.followme.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ghdev.followme.R
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.ui.LoginActivity
import com.ghdev.followme.ui.PlaceDetailActivity
import kotlinx.android.synthetic.main.fragment_mypage.*
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.nio.charset.CharsetEncoder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MypageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MypageFragment : Fragment() {

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0") !== "0"){
            DecodeToken()
        }


        btn_mypage_goto_mypick.setOnClickListener {
            activity?.let {
               val intent = Intent(context, MypageMypickActivity::class.java)
                startActivity(intent)

               /* val intent = Intent(context, PlaceDetailActivity::class.java)
                startActivity(intent)*/
            }
        }

        btn_mypage_goto_myset.setOnClickListener {
            activity?.let {
                val intent = Intent(context, MypageMysettingActivity::class.java)
                startActivity(intent)
            }
        }

        btn_sign_mypage.setOnClickListener{
            activity?.let{
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    /********************************JWTtoken decode**********************************/
    private fun DecodeToken(){
        //sharedprefs에 token값이 들어있다면
            try{
            val split : List<String>  = sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0").split(".")
                Log.d("JWT_DECODED", "Header: "+ getJson(split[0]))
                Log.d("JWT_DECODED", "Body: "+ getJson(split[1]))
                val tempJWT : List<String> = getJson(split[1]).split(",")
                val userJWT : List<String> = tempJWT[1].split(":")
                changeUserText(userJWT[1])
                Log.d("JWT_DECODED", "nickname: " + userJWT[1])
        }catch (e : Exception){
                Log.d("JWT_DECODED", "split error: " + e)
            }
    }

    private fun getJson(strEncoded : String) : String{
        lateinit var decodeBytes : ByteArray
        try{
            var decodeBytes :ByteArray = Base64.decode(strEncoded, Base64.URL_SAFE)
            return decodeBytes.toString(Charsets.UTF_8)
        }catch (e : UnsupportedEncodingException){
            Log.d("JWT_DECODED", "getJson error: " + e)
        }
        return decodeBytes.toString(Charsets.UTF_8)
    }

    /*******************************change Maypage User Text**********************************/

    private fun changeUserText(nickname : String){
        tv_mypage_login_and_sign_title.setText("환영합니다! " + nickname + "님")
        iv_mypage_login_and_sign.visibility = View.INVISIBLE
        tv_mypage_login_and_sign_text.setText(R.string.already_sign_text_mypage)
        ll_mypage_login_and_sign.isClickable = false
    }
}


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
import com.ghdev.followme.data.JWTDecode
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
            val get_nickname : String = JWTDecode().DecodeToken(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0"))
            changeUserText(get_nickname)
        }else{
            btn_sign_mypage.setOnClickListener{
                activity?.let{
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
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
    }



    /*******************************change Maypage User Text**********************************/

    private fun changeUserText(nickname : String){
        tv_mypage_login_and_sign_title.setText("환영합니다! " + nickname + "님")
        iv_mypage_login_and_sign.visibility = View.INVISIBLE
        tv_mypage_login_and_sign_text.setText(R.string.already_sign_text_mypage)
        ll_mypage_login_and_sign.isClickable = false
    }
}


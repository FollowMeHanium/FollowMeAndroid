package com.ghdev.followme.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.ghdev.followme.data.PostLoginResponse
import com.ghdev.followme.model.LoginUserModel
import com.ghdev.followme.network.LoginUserRepo
import com.ghdev.followme.ui.LoginListener

/*
모든 View와 관련된 비즈니스 로직이 해당.
data를 잘 가공해서 View에서 뿌리기 쉬운 Model로 바꾸는 역할.
Databinding을 이용해서 정보 전달 및 변경을 알림.
*/


class LoginViewModel(private val listener: PostLoginResponse) : ViewModel(){

    private val user : LoginUserModel = LoginUserModel("", "")

    var loginListener: LoginListener? = null

    var email: String? = null
    var password: String? = null


    //로그인 버튼 클릭시
    //btn_login_act와 databinding
    fun onLoginClicked(v: View){
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            loginListener?.onFailure("Invalid email or password")
        }

        val loginResponse = LoginUserRepo().postLoginResponse(email!!, password!!)
        loginListener?.onSuccess(loginResponse)
    }

}
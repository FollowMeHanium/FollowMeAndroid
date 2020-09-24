package com.ghdev.followme.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.ghdev.followme.R
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.ui.LoginActivity
import kotlinx.android.synthetic.main.activity_mypage_mysetting.*
import org.jetbrains.anko.startActivity

class MypageMysettingActivity : AppCompatActivity(), View.OnClickListener {

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_mysetting)

        init()
    }

    fun init(){
        btn_myset_logout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btn_myset_logout -> {
                LogoutDialogCreate()
            }
        }

    }

    fun LogoutDialogCreate(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그아웃")
            .setMessage("로그아웃 하시겠습니까?")
            .setPositiveButton("확인"){dialog, which ->
                sharedPrefs.setString(PreferenceHelper.PREFS_KEY_ACCESS, "0")
                sharedPrefs.setString(PreferenceHelper.PREFS_KEY_REF, "0")

                startActivity<LoginActivity>()
                finish()
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }
}

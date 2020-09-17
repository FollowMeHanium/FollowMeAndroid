package com.ghdev.followme.ui

import android.content.Context
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.ghdev.followme.R
import com.ghdev.followme.ui.base.BasePagerAdapter
import com.ghdev.followme.ui.coursereco.CourseRecommendFragment
import com.ghdev.followme.ui.home.HomeFragment
import com.ghdev.followme.ui.mycourse.MyCourseFragment
import com.ghdev.followme.ui.mypage.MypageFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var lastTimeBackPressed : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ViewPager
        viewpager.run {
            adapter = BasePagerAdapter(supportFragmentManager).apply {
                addFragment(HomeFragment())
                addFragment(MyCourseFragment())
                addFragment(CourseRecommendFragment())
                addFragment(MypageFragment())
            }
            offscreenPageLimit = 3
        }

        // TabLayout
        tablayout.run {
            val navigationLayout: View =
                LayoutInflater.from(this@MainActivity).inflate(R.layout.activity_main_navi, null, false)

            setupWithViewPager(viewpager)
            getTabAt(0)!!.customView =
                navigationLayout.findViewById(R.id.img_home_main_navi_act) as ImageView
            getTabAt(1)!!.customView =
                navigationLayout.findViewById(R.id.img_course_main_navi_act) as ImageView
            getTabAt(2)!!.customView =
                navigationLayout.findViewById(R.id.img_course_recommend_main_navi_act) as ImageView
            getTabAt(3)!!.customView =
                navigationLayout.findViewById(R.id.img_mypage_main_navi_act) as ImageView
        }
    }

    //MainActivity에서 뒤로 버튼 클릭 시 (LoginActivity로 넘어가지 않음)
   override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish()
            return
        }
        lastTimeBackPressed = System.currentTimeMillis()
        toast("'뒤로' 버튼을 한 번 더 누르면 종료합니다.")
    }

}

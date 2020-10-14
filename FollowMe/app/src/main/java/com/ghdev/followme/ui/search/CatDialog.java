package com.ghdev.followme.ui.search;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ghdev.followme.R;

public class CatDialog extends Dialog {
    Button btn_course_category_lovers;
    Button btn_course_category_act;
    Button btn_course_category_family;
    Button btn_course_category_friends;
    Button btn_course_category_pet;
    Button btn_course_category_tv;
    Button btn_course_dialog_close;

    public CatDialog(Context context) {
        super(context);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.

        //다이얼로그에서 사용할 레이아웃입니다.
        setContentView(R.layout.dialog_course_category);

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button btn_course_category_lovers = (Button) findViewById(R.id.btn_course_category_lovers);
        final Button btn_course_category_act = (Button) findViewById(R.id.btn_course_category_act);
        final Button btn_course_category_family = (Button) findViewById(R.id.btn_course_category_family);
        final Button btn_course_category_friends = (Button) findViewById(R.id.btn_course_category_friends);
        final Button btn_course_category_pet = (Button) findViewById(R.id.btn_course_category_pet);
        final Button btn_course_category_tv = (Button) findViewById(R.id.btn_course_category_tv);
        final Button btn_course_dialog_close = (Button) findViewById(R.id.btn_course_dialog_close);

//        btn_course_category_lovers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
//                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
//
//                // 커스텀 다이얼로그를 종료한다.
//                dlg.dismiss();
//            }
//        });

        btn_course_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
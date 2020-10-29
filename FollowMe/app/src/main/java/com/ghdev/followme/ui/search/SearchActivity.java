package com.ghdev.followme.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ghdev.followme.R;
import com.ghdev.followme.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {
    ListView listview;
    Button btn_search_cat;
    CatDialog cd;
    SearchHistoryAdapter adapter;

    // 검색 값 전달
    ImageView SearchBtn;
    EditText sendData;

    // 검색 값 저장
    String shared = "file";
    ImageView btn_delete_history;

    // 뒤로가기
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Adapter 생성
        adapter = new SearchHistoryAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.rv_search_history);
        listview.setAdapter(adapter);

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                SearchHistoryItem item = (SearchHistoryItem) parent.getItemAtPosition(position);

                String titleStr = item.getTitle();
                String dateStr = item.getDate();
                Drawable iconDrawable = item.getIcon();

                // TODO : use item data.
            }
        });

        // 카테고리 호출
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이

        btn_search_cat = (Button) findViewById(R.id.btn_search_cat);
        cd = new CatDialog(this);

        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(cd.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width;  //화면 너비의 절반
        wm.height = height;  //화면 높이의 절반

        btn_search_cat.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼 클릭시 Custom Dialog 호출
                cd.show();
            }
        });

        SearchBtn = (ImageView) findViewById(R.id.btn_search_search);

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("sendData",sendData.getText().toString());// 이 메서드를 통해 데이터를 전달합니다.
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼
        btn_back = (ImageView) findViewById(R.id.btn_search_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        // 검색 값 전달
        sendData = (EditText) findViewById(R.id.et_search_place);

        // 현재 날짜 출력
        Date time = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat ( "YY.MM.dd");
        String time1 = format1.format(time);

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String value = sharedPreferences.getString("searchtext", "");
//        sendData.setText(value);

        // 아이템 추가.
        if (value.length() != 0) {
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.btn_delete_history),
                    value, time1);
        }
    }

    // 뒤로가기, 다른 액티비티 등 액티비티 벗어날 때 작동
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = sendData.getText().toString();
        editor.putString("searchtext", value);
        editor.commit();
    }
}



//    private Context mContext;
//    private Button btn_search_cat;
//    private CatDialog mCatDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        btn_search_cat = (Button) findViewById(R.id.btn_search_cat);
//        btn_search_cat.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_search_cat:
//                mCatDialog = new CatDialog(this);
//                mCatDialog.setCancelable(false);
//                mCatDialog.show();
//                break;
//        }
//    }
//}

//        btn_cat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder ad = new AlertDialog.Builder(SearchActivity.this);
//
//                ad.setTitle("카테고리");
//                ad.setMessage("하나만 고르시오.");
//
//                //닫기 버튼 설정
//                ad.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                ad.show();
//            }
//        });
//    }
package com.example.singsingmart;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnInLogin, btnKeyView;
    EditText etPw, etid;
    LinearLayout lyKeyNum;
    int keyCnt = 0;// 5가 차야 관리자키 화면창이 보임

    SQLiteDatabase db;//디비 관련 변수선언
    MySQLiteOpenHelper helper;
    String id, pw;

    String TAG = "로그인화면예제";

    EditText EtKeyNum;//관리자 키넘버.
    Button btnMaster;//관리자 레이아웃 이동 버튼.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //데이베이스 생성.
        helper = new MySQLiteOpenHelper(
                LoginActivity.this, // 현재 화면의 context
                "member.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호
        //관리자 인텐트 부분
        EtKeyNum=(EditText)findViewById(R.id.EtKeyNum);
        btnMaster=(Button)findViewById(R.id.btnMaster);

        btnMaster.setOnClickListener(mClickListener);
        //로그인 인텐트 부분
        etPw=(EditText)findViewById(R.id.etPw);
        etid=(EditText)findViewById(R.id.etid);

        btnInLogin=(Button)findViewById(R.id.btnInLogin);
        btnInLogin.setOnClickListener(mClickListener);
        //관리자 로그인 레이아웃 온오프.
        btnKeyView=(Button)findViewById(R.id.btnKeyView);
        btnKeyView.setOnClickListener(mClickListener);

        lyKeyNum=(LinearLayout)findViewById(R.id.lyKeyNum);


    }
    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnKeyView://관리자 버튼
                    keyCnt++;

                    if(keyCnt==5)
                    {
                        lyKeyNum.setVisibility(View.VISIBLE);
                    }

                    break;

                case R.id.btnMaster://관리자 버튼

                        if(EtKeyNum.getText().toString().equals(""))
                        {
                            Toast.makeText(LoginActivity.this, "keyNumber 입력하세요.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else{

                            if(EtKeyNum.getText().toString().equals("1004"))
                            {
                                EtKeyNum.setText("");
                                Intent intentLogin = new Intent(LoginActivity.this, MasterActivity.class);
                                startActivity(intentLogin);
                                finish();
                            }
                            else{
                                EtKeyNum.setText("");
                                Toast.makeText(LoginActivity.this, "keyNumber가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    lyKeyNum.setVisibility(View.GONE);//관리자 페이지 넘김 동시에 키 넘버창 종료
                    keyCnt =0;
                    break;

                //로그인 인텐트 부분======================================================
                case R.id.btnInLogin:

                    if (etid.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //공백없이 입력잘되었을경우 변수에 저장
                    id = etid.getText().toString();

                    if (etPw.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "비번을 입력하세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //공백없이 입력잘되었을경우 변수에 저장
                    pw = etPw.getText().toString();

                    //디비에서 비교 검색

                    boolean LoginOk = selectAll(id, pw);

                    if (LoginOk == false) {
                        Toast.makeText(LoginActivity.this, "로그인실패", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //최종로그인처리
                    Intent intentLogin = new Intent(LoginActivity.this,IntroActivity.class);
                    startActivity(intentLogin);
                    finish();
                    keyCnt=0;//로그인 되었을때 관리자키창의 화면 넘김 변수 초기화.
                    break;

            }
        }
    };

    public boolean selectAll(String LoginId, String LoginPw) {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.rawQuery("SELECT * FROM member", null);

        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            String name = c.getString(3);
            String hp = c.getString(4);

            Log.d("회원검색및 비교", "idx: " + idx + ", id : " + id + ", name : " + name
                    + ", hp : " + hp);
            if (LoginId.equals(id)) {
                if (LoginPw.equals(pw)) {
                    Log.d("로그인 성공", "idx: " + idx + ", id : " + id + ",\", pw : \" + pw + \" ,name : " + name
                            + ", hp : " + hp);
                    c.close();
                    db.close();
                    return true;
                }
            }
        }
        c.close();
        db.close();

        return false;
    }
}
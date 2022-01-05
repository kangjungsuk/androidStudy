package com.example.singsingmart;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MasterActivity extends AppCompatActivity {

    //SQLITE 데이타베이스 관련변수
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;//클래스
    String TAG ="회원가입 예제";
    String id,pw,name,hp;


    //회원가입 레이아웃 관련 변수들
    Button btnJoin, btnSel, btnLayUpdate, btnLayDel,btnJoinPass,btnBack; //하단 레이아웃 변환 버튼
    LinearLayout lySelect, lyJoin, lyUpdate, lyDel;// 대표 메인 레이아웃
    EditText joinEtId, joinEtPw, joinEtName, joinEtHp;

    //조회 레이아웃 관련 변수들
    EditText selEtId;
    TextView tvSelView;
    Button btnSelPass, btnAllSel;

    //업데이트 레이아웃 관련 변수들
    EditText EtUpdateId; //수정할아이디 입력
    EditText EtUpName, EtUpHp; //수정내용 입력
    TextView tvUpNowId,tvUpNowName, tvUpNowHp;  //현재 정보보여주기
    TextView tvUpName, tvUpHp; //변경된 정보 보여주기
    Button btnUpIdSel, btnUpPass; //아이디 찾기, 업데이트수행

    //삭제 레이아웃 관련 변수들
    EditText  EtDelId;
    Button btnDelPass;
    TextView tvDelView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

                //데이베이스 생성.
        helper = new MySQLiteOpenHelper(
                MasterActivity.this, // 현재 화면의 context
                "member.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호


        lySelect=(LinearLayout)  findViewById(R.id.lySelect);//대표 메인 레이아웃.
        lyJoin=(LinearLayout)  findViewById(R.id.lyJoin);
        lyUpdate=(LinearLayout)  findViewById(R.id.lyUpdate);
        lyDel=(LinearLayout)  findViewById(R.id.lyDel);

        btnJoin =(Button) findViewById(R.id.btnJoin);//하단 레이아웃 변환 버튼.
        btnSel =(Button) findViewById(R.id.btnSel);
        btnLayUpdate =(Button) findViewById(R.id.btnLayUpdate);
        btnLayDel =(Button) findViewById(R.id.btnLayDel);
        btnJoinPass=(Button) findViewById(R.id.btnJoinPass);
        btnBack=(Button) findViewById(R.id.btnBack);

        btnJoin.setOnClickListener(mChecklists);//하단 레이아웃 변환 버튼.
        btnSel.setOnClickListener(mChecklists);
        btnLayUpdate.setOnClickListener(mChecklists);
        btnLayDel.setOnClickListener(mChecklists);
        btnBack.setOnClickListener(mChecklists);


        //회원가입 레이아웃 변수 연결=========================
        joinEtId=(EditText)findViewById(R.id.joinEtId);
        joinEtPw=(EditText)findViewById(R.id.joinEtPw);
        joinEtName=(EditText)findViewById(R.id.joinEtName);
        joinEtHp=(EditText)findViewById(R.id.joinEtHp);

        btnJoin =(Button) findViewById(R.id.btnJoin);//하단 레이아웃 변환 버튼.
        btnJoinPass.setOnClickListener(mChecklists);//가입 완료 버튼
        //조회 레이아웃 변수 연결============================
        selEtId=(EditText)findViewById(R.id.selEtId);
        tvSelView=(TextView)findViewById(R.id.tvSelView);

        btnSelPass=(Button) findViewById(R.id.btnSelPass);
        btnAllSel=(Button) findViewById(R.id.btnAllSel);

        btnSelPass.setOnClickListener(mChecklists);
        btnAllSel.setOnClickListener(mChecklists);
        //업데이트 레이아웃 관련 변수들========================
        EtUpdateId=(EditText)findViewById(R.id.EtUpdateId);
        EtUpName=(EditText)findViewById(R.id.EtUpName);
        EtUpHp=(EditText)findViewById(R.id.EtUpHp);

        tvUpNowId=(TextView)findViewById(R.id.tvUpNowId);
        tvUpNowName=(TextView)findViewById(R.id.tvUpNowName);
        tvUpNowHp=(TextView)findViewById(R.id.tvUpNowHp);
        tvUpName=(TextView)findViewById(R.id.tvUpName);
        tvUpHp=(TextView)findViewById(R.id.tvUpHp);

        btnUpIdSel=(Button)findViewById(R.id.btnUpIdSel);
        btnUpPass=(Button)findViewById(R.id.btnUpPass);

        btnUpIdSel.setOnClickListener(mChecklists);
        btnUpPass.setOnClickListener(mChecklists);

        //삭제 레이아웃 관련 변수들.
        EtDelId=(EditText)findViewById(R.id.EtDelId);
        btnDelPass=(Button)findViewById(R.id.btnDelPass);
        tvDelView=(TextView)findViewById(R.id.tvDelView);

        btnDelPass.setOnClickListener(mChecklists);

    }
    Button.OnClickListener mChecklists = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnJoin:
                    layoutView();
                    lyJoin.setVisibility(View.VISIBLE);//회원가입 화면 먼저 띄워줌.
                    break;
                case R.id.btnSel:
                    layoutView();
                    lySelect.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnLayUpdate:
                    layoutView();
                    lyUpdate.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnLayDel:
                    layoutView();
                    lyDel.setVisibility(View.VISIBLE);
                    break;

                    //회원 가입완료 버튼 실행===========================
                case R.id.btnJoinPass:

                    if(joinEtId.getText().toString().equals(""))
                    {
                        Toast.makeText(MasterActivity.this, "아이디를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //공백없이 입력잘되었을경우 변수에 저장
                    id = joinEtId.getText().toString();

                    if(joinEtPw.getText().toString().equals(""))
                    {
                        Toast.makeText(MasterActivity.this, "비번을 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //공백없이 입력잘되었을경우 변수에 저장
                    pw = joinEtPw.getText().toString();

                    if(joinEtName.getText().toString().equals(""))
                    {
                        Toast.makeText(MasterActivity.this, "이름을 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //공백없이 입력잘되었을경우 변수에 저장
                    name = joinEtName.getText().toString();

                    if(joinEtHp.getText().toString().equals(""))
                    {
                        Toast.makeText(MasterActivity.this, "연락처를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //공백없이 입력잘되었을경우 변수에 저장
                    hp = joinEtHp.getText().toString();
                    //회원정보를 다 입력하였을경우 데이타베이스에 insert를 호출
                    insert(id,pw,name,hp);
                    break;

                //조회 레이아웃 버튼 =================================================
                case R.id.btnSelPass:
                    String id = selEtId.getText().toString();
                    if(id.equals(""))
                    {
                        break;
                    }
                    select(id);
                    selEtId.setText("");
                    break;
                case R.id.btnAllSel:
                    selectAll();
                    break;

                // 업데이트(수정)레이아웃 관련 부분================================================
                case R.id.btnUpIdSel:
                    String idUpdateFind = EtUpdateId.getText().toString();
                    if(idUpdateFind.equals(""))
                    {
                        break;
                    }

                    selectUpdate(idUpdateFind);
                    break;
                case R.id.btnUpPass:

                    String inputUpdateName = EtUpName.getText().toString();
                    String inputUpdateHp = EtUpHp.getText().toString();

                    String inputUpdateId = EtUpdateId.getText().toString();
                    if(inputUpdateId.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "수정대상 아이디를 입력하세요.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if(inputUpdateName.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "수정대상 이름을 입력하세요.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if(inputUpdateHp.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "수정대상 연락처를 입력하세요.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    update(inputUpdateId,inputUpdateName, inputUpdateHp);
                    break;

                case R.id.btnDelPass:

                    String deleteId = EtDelId.getText().toString();
                    if(deleteId.equals(""))
                    {
                        break;
                    }
                    delete(deleteId);
                    EtDelId.setText("");
                    selectAllDel(); //현재 디비조회 및 삭제후 조회
                    break;

                case R.id.btnBack:
                    Intent intent = new Intent (MasterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }

        }
    };

    //메인 4개 레이아웃 인비지블 메소드
    public void layoutView(){
        lySelect.setVisibility(View.INVISIBLE);
        lyJoin.setVisibility(View.INVISIBLE);
        lyUpdate.setVisibility(View.INVISIBLE);
        lyDel.setVisibility(View.INVISIBLE);
    }

    //메소드 부분 ======================================================
    public void insert(String id, String pw, String name, String hp) {

        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        //값들을 컨트롤 하려고 클래스 생성
        ContentValues values = new ContentValues();

        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("id", id);
        values.put("pw", pw);
        values.put("name", name);
        values.put("hp", hp);
        db.insert("member", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)

        // tip : 마우스를 db.insert에 올려보면 매개변수가 어떤 것이 와야 하는지 알 수 있다.
        db.close();
        Toast.makeText(getApplicationContext(), id+"로 회원 가입 완료.", Toast.LENGTH_LONG).show();

        Log.d(TAG, id+"/"+pw+"/"+name+"/"+hp+" 의 정보로 디비저장완료.");
    }

    public void select(String searchId) {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        //Cursor c = db.query("member", null, null, null, null, null, null);
        Cursor c = db.rawQuery("SELECT * FROM member where id ='" + searchId + "'", null);
        /*
         * 위 결과는 select * from member 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */

        String result = "";
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            String name = c.getString(3);
            String hp = c.getString(4);

            Log.d("member테이블", "idx: " + idx + ", id : " + id + ", name : " + name
                    + ", hp : " + hp);
            result += "idx: " + idx + ", id : " + id + ", name : " + name + ", hp : " + hp + "\n";

        }
        c.close();
        db.close();

        tvSelView.setText(result);
    }

    public void selectAll() {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        //Cursor c = db.query("member", null, null, null, null, null, null);
        Cursor c = db.rawQuery("SELECT * FROM member", null);
        /*
         * 위 결과는 select * from member 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */

        String result = "";
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            String name = c.getString(3);
            String hp = c.getString(4);

            Log.d("member테이블", "idx: " + idx + ", id : " + id + ", name : " + name
                    + ", hp : " + hp);
            result += "idx: " + idx + ", id : " + id + ", name : " + name + ", hp : " + hp + "\n";


        }
        c.close();
        db.close();

        tvSelView.setText(result);
    }

    //Update 관련 메소드
    public void selectUpdate(String searchId) {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        //Cursor c = db.query("member", null, null, null, null, null, null);
        Cursor c = db.rawQuery("SELECT * FROM member where id ='" + searchId + "'", null);
        /*
         * 위 결과는 select * from member 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */
        int idx;
        String id="", pw="", name="", hp="";
        String result="";
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            idx = c.getInt(0);
            id = c.getString(1);
            pw = c.getString(2);
            name = c.getString(3);
            hp = c.getString(4);

            Log.d("수정대상정보", "idx: " + idx + ", id : " + id + ", name : " + name
                    + ", hp : " + hp);
            result += "idx: " + idx + ", id : " + id + ", name : " + name + ", hp : " + hp + "\n";


        }
        c.close();
        db.close();

        //현재 정보를 보여주기
        tvUpNowId.setText(id);
        tvUpNowName.setText(name);
        tvUpNowHp.setText(hp);
    }
    // update
    public void update(String id, String name, String hp) {
        db = helper.getWritableDatabase(); //db 객체를 얻어온다. 쓰기가능

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("hp", hp);
        db.update("member", values, "id='" + id + "'", null);
        db.close();
        Toast.makeText(getApplicationContext(), id + "의 정보가 수정되었습니다.", Toast.LENGTH_LONG).show();

        //현재 정보를 보여주기
        tvUpName.setText(name);
        tvUpHp.setText(hp);
    }
    //Del
    public void delete(String id) {
        db = helper.getWritableDatabase();
        db.delete("member", "id='"+id+"'", null);
        Log.i("정보삭제", id + "정상적으로 삭제 되었습니다.");
        Toast.makeText(getApplicationContext(), id+"의 정보가 삭제되었습니다.", Toast.LENGTH_LONG).show();
        db.close();
    }

    public void selectAllDel() {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        //Cursor c = db.query("member", null, null, null, null, null, null);
        Cursor c = db.rawQuery("SELECT * FROM member", null);
        /*
         * 위 결과는 select * from member 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */

        String result = "";
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            String name = c.getString(3);
            String hp = c.getString(4);

            Log.d("member테이블", "idx: " + idx + ", id : " + id + ", pw : " + pw + ", name : " + name
                    + ", hp : " + hp);
            result += "idx: " + idx + ", id : " + id + ", name : " + name + ", hp : " + hp + "\n";


        }
        c.close();
        db.close();

        tvDelView.setText(result);

    }

}
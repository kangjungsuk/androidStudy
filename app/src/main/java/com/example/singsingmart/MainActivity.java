package com.example.singsingmart;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    Animation tvUp, tvDown;//버튼 클릭시 업다운 애니메이션 변수

    //SQLITE 데이타베이스 관련변수
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;//클래스
    String id,pw,name,hp;


    //개인정보 수정 관련 변수
    EditText edFind,edName, edHp;
    Button btnFind, btnUpdate;
    TextView tvUpName1, tvUpHp1;


   //=======shop 레이아웃 관련 변수 선언==============================================
    Button btnShop,btnBuy,btnBot,btnMy,btnP1,btnP2,btnP3,btnP4,
            btnM1,btnM2,btnM3,btnM4,
            btnInten1,btnInten2,btnInten3,btnInten4;
    TextView tvOnionAni,tvCarrotAni,tvTomatoAni,tvPotatoAni;//야채 이름 상하 움직임tv
    EditText edCnt4,edCnt1,edCnt2,edCnt3;//야채 갯수가 표시되는 에디트 텍스트
    LinearLayout shopLayout, reviewLayout, bottleLayout, myLayout;
    ImageView imgOnion;

    //과일 갯수표시 변수
    int OnionEa = 0;
    int CarrotEa = 0;
    int TomatoEa = 0;
    int potatoEa = 0;
    //과일 총가격
    int onionTotal = 0;
    int carrotTotal = 0;
    int tomatoTotal = 0;
    int potatoTotal = 0;


    //=======장바구니 레이아웃 관련 변수선언===============================================
    LinearLayout onionLayout,carrotLayout,tomatoLayout,potatoLayout,fyBot1,fyBot2,
            fyBotWait,fyInfoWait;
    TextView tvStore1,tvStore2,tvStore3,tvStore4,
            tvVag1,tvVag2,tvVag3,tvVag4,
            tvEa1,tvEa2,tvEa3,tvEa4,
            tvCoinTol1,tvCoinTol2,tvCoinTol3,tvCoinTol4;

    ImageView imgBot1,imgBot2,imgBot3,imgBot4;
    Button btnPay, btnCancel1, btnCancel2, btnCancel3, btnCancel4;

    // 장바구니 안 결제하기 관련 변수 선언
    Button btn50000,btn10000,btn5000,btn1000,btnCoinRe,btnPayment;
    TextView tvInCoin, tvTolMoney, tvInfo,
            tvReviewVa1,tvReviewVa2,tvReviewVa3,tvReviewVa4,
            tvReviewCo1,tvReviewCo2,tvReviewCo3,tvReviewCo4,
            tvWon1,tvWon2,tvWon3,tvWon4;


    int fillMoeny =0;//충전금액
    int vageTotalMoney =0;//야채총금액
    int stayMoney = 0;//남은금액(나의정보하단에 뿌려줄 예정)

    // 나의정보 관련 변수================================================================
    LinearLayout myInfoVa1,myInfoVa2,myInfoVa3, myInfoVa4,myBuyListLay,myInfoLay;//각 야채 gone되어 있는 부분
    ImageView myInfoImg1, myInfoImg2, myInfoImg3,myInfoImg4;//gone 부분의 이미지
    Button btnInfo;//개인정보넘어가기버튼
    TextView tvMyInfoEa1, tvMyInfoEa2, tvMyInfoEa3, tvMyInfoEa4,
            tvMyinfoStore1, tvMyinfoStore2, tvMyinfoStore3, tvMyinfoStore4,
            tvMyInfoVa1, tvMyInfoVa2, tvMyInfoVa3, tvMyInfoVa4,
            myInfoMoney1;// 줄 순서 야채 누적갯수 ,야채가게,야채명

    int onionTotalEa = 0;//구매내역에 누적되는 갯수
    int carrotTotalEa = 0;
    int tomatoTotalEa = 0;
    int potatoTotalEa = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버튼 클릭시 야채이름 움직이는 애니메이션=====================================
        tvUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.tvup);
        tvDown= AnimationUtils.loadAnimation(MainActivity.this, R.anim.tvdown);

        helper = new MySQLiteOpenHelper(
                MainActivity.this, // 현재 화면의 context
                "member.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

        //배너 그리드
        ImageView banner1 = (ImageView)findViewById(R.id.imgBanner);
        Glide.with(this).load(R.drawable.banner).into(banner1);

        //개인정보 수정 관련 변수
        edFind =(EditText)findViewById(R.id.edFind);
        edName =(EditText)findViewById(R.id.edName);
        edHp =(EditText)findViewById(R.id.edHp);

        tvUpName1=(TextView)findViewById(R.id.tvUpName1);
        tvUpHp1=(TextView)findViewById(R.id.tvUpHp1);

        btnFind=(Button)findViewById(R.id.btnFind);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);

        btnFind.setOnClickListener(mClickListener);
        btnUpdate.setOnClickListener(mClickListener);

        //하단 4개의 메인 레이아웃
        shopLayout =(LinearLayout) findViewById(R.id.shopLayout);
        reviewLayout =(LinearLayout) findViewById(R.id.reviewLayout);
        bottleLayout =(LinearLayout) findViewById(R.id.bottleLayout);
        myLayout =(LinearLayout) findViewById(R.id.myLayout);


        //======= shop 레이아웃 부분========================================================
        imgOnion = (ImageView) findViewById(R.id.imgOnion);

        //하단 레이아웃 이동 버튼들
        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnBot = (Button) findViewById(R.id.btnBot);
        btnMy = (Button) findViewById(R.id.btnMy);
        btnShop = (Button) findViewById(R.id.btnShop);

        //화면 제어 버튼들
        btnP1 = (Button) findViewById(R.id.btnP1);
        btnP2 = (Button) findViewById(R.id.btnP2);
        btnP3 = (Button) findViewById(R.id.btnP3);
        btnP4 = (Button) findViewById(R.id.btnP4);
        btnM1 = (Button) findViewById(R.id.btnM1);
        btnM2 = (Button) findViewById(R.id.btnM2);
        btnM3 = (Button) findViewById(R.id.btnM4);
        btnM4 = (Button) findViewById(R.id.btnM3);
        btnInten1 = (Button) findViewById(R.id.btnInten1);
        btnInten2 = (Button) findViewById(R.id.btnInten2);
        btnInten3 = (Button) findViewById(R.id.btnInten3);
        btnInten4 = (Button) findViewById(R.id.btnInten4);

        //수량 표시 에디트 텍스트
        edCnt1= (EditText) findViewById(R.id.edCnt1);//수량표시 텍스트 tv>>ed
        edCnt2= (EditText) findViewById(R.id.edCnt2);//수량표시 텍스트 tv>>ed
        edCnt3= (EditText) findViewById(R.id.edCnt3);//수량표시 텍스트 tv>>ed
        edCnt4 = (EditText) findViewById(R.id.edCnt4);//수량표시 텍스트 tv>>ed

        //shop 레이아웃의 야채 이름 텍스트뷰 상하 애니메이션 변수
        tvOnionAni= (TextView) findViewById(R.id.tvOnionAni);
        tvCarrotAni= (TextView) findViewById(R.id.tvCarrotAni);
        tvTomatoAni= (TextView) findViewById(R.id.tvTomatoAni);
        tvPotatoAni= (TextView) findViewById(R.id.tvPotatoAni);


        btnBuy.setOnClickListener(mClickListener);
        btnBot.setOnClickListener(mClickListener);
        btnMy.setOnClickListener(mClickListener);
        btnShop.setOnClickListener(mClickListener);

        btnP1.setOnClickListener(mClickListener);
        btnP2.setOnClickListener(mClickListener);
        btnP3.setOnClickListener(mClickListener);
        btnP4.setOnClickListener(mClickListener);
        btnM1.setOnClickListener(mClickListener);
        btnM2.setOnClickListener(mClickListener);
        btnM3.setOnClickListener(mClickListener);
        btnM4.setOnClickListener(mClickListener);
        btnInten1.setOnClickListener(mClickListener);
        btnInten2.setOnClickListener(mClickListener);
        btnInten3.setOnClickListener(mClickListener);
        btnInten4.setOnClickListener(mClickListener);



        //=======장바구니 레이아웃 부분=========================================
        onionLayout=(LinearLayout) findViewById(R.id.onionLayout);
        carrotLayout=(LinearLayout) findViewById(R.id.carrotLayout);
        tomatoLayout=(LinearLayout) findViewById(R.id.tomatoLayout);
        potatoLayout=(LinearLayout) findViewById(R.id.potatoLayout);
        fyBot1=(LinearLayout) findViewById(R.id.fyBot1);
        fyBot2=(LinearLayout) findViewById(R.id.fyBot2);
        fyBotWait=(LinearLayout) findViewById(R.id.fyBotWait);
        fyInfoWait=(LinearLayout) findViewById(R.id.fyInfoWait);

        tvStore1= (TextView) findViewById(R.id.tvStore1);
        tvStore2= (TextView) findViewById(R.id.tvStore2);
        tvStore3= (TextView) findViewById(R.id.tvStore3);
        tvStore4= (TextView) findViewById(R.id.tvStore4);
        tvVag1= (TextView) findViewById(R.id.tvVag1);
        tvVag2= (TextView) findViewById(R.id.tvVag2);
        tvVag3= (TextView) findViewById(R.id.tvVag3);
        tvVag4= (TextView) findViewById(R.id.tvVag4);
        tvEa1= (TextView) findViewById(R.id.tvEa1);
        tvEa2= (TextView) findViewById(R.id.tvEa2);
        tvEa3= (TextView) findViewById(R.id.tvEa3);
        tvEa4= (TextView) findViewById(R.id.tvEa4);
        tvCoinTol1= (TextView) findViewById(R.id.tvCoinTol1);
        tvCoinTol2= (TextView) findViewById(R.id.tvCoinTol2);
        tvCoinTol3= (TextView) findViewById(R.id.tvCoinTol3);
        tvCoinTol4= (TextView) findViewById(R.id.tvCoinTol4);

        btnCancel1=(Button)findViewById(R.id.btnCancel1);// 장바구니 담긴목록 취소 버튼
        btnCancel2=(Button)findViewById(R.id.btnCancel2);
        btnCancel3=(Button)findViewById(R.id.btnCancel3);
        btnCancel4=(Button)findViewById(R.id.btnCancel4);

        btnCancel1.setOnClickListener(mClickListener);
        btnCancel2.setOnClickListener(mClickListener);
        btnCancel3.setOnClickListener(mClickListener);
        btnCancel4.setOnClickListener(mClickListener);



        //장바구니 안 결제관련 부분.
        tvInCoin= (TextView) findViewById(R.id.tvInCoin);
        tvTolMoney= (TextView) findViewById(R.id.tvTolMoney);
        tvInfo= (TextView) findViewById(R.id.tvInfo);

        tvReviewVa1= (TextView) findViewById(R.id.tvReviewVa1);
        tvReviewVa2= (TextView) findViewById(R.id.tvReviewVa2);
        tvReviewVa3= (TextView) findViewById(R.id.tvReviewVa3);
        tvReviewVa4= (TextView) findViewById(R.id.tvReviewVa4);
        tvReviewCo1= (TextView) findViewById(R.id.tvReviewCo1);
        tvReviewCo2= (TextView) findViewById(R.id.tvReviewCo2);
        tvReviewCo3= (TextView) findViewById(R.id.tvReviewCo3);
        tvReviewCo4= (TextView) findViewById(R.id.tvReviewCo4);

        tvWon1=(TextView)findViewById(R.id.tvWon1);
        tvWon2=(TextView)findViewById(R.id.tvWon2);
        tvWon3=(TextView)findViewById(R.id.tvWon3);
        tvWon4=(TextView)findViewById(R.id.tvWon4);

        imgBot1= (ImageView) findViewById(R.id.imgBot1);
        imgBot2= (ImageView) findViewById(R.id.imgBot2);
        imgBot3= (ImageView) findViewById(R.id.imgBot3);
        imgBot4= (ImageView) findViewById(R.id.imgBot4);

        btnPay= (Button) findViewById(R.id.btnPay);
        btn50000= (Button) findViewById(R.id.btn50000);
        btn10000= (Button) findViewById(R.id.btn10000);
        btn5000= (Button) findViewById(R.id.btn5000);
        btn1000= (Button) findViewById(R.id.btn1000);
        btnCoinRe= (Button) findViewById(R.id.btnCoinRe);
        btnPayment= (Button) findViewById(R.id.btnPayment);

        btnPay.setOnClickListener(mClickListener);
        btn50000.setOnClickListener(mClickListener);
        btn10000.setOnClickListener(mClickListener);
        btn5000.setOnClickListener(mClickListener);
        btn1000.setOnClickListener(mClickListener);
        btnCoinRe.setOnClickListener(mClickListener);
        btnPayment.setOnClickListener(mClickListener);

        //나의 정보 관련 변수 연결======================================================================
        myInfoVa1=(LinearLayout) findViewById(R.id.myInfoVa1);//나의정보 구매내역 쪽 야채 gone 된 부분
        myInfoVa2=(LinearLayout) findViewById(R.id.myInfoVa2);
        myInfoVa3=(LinearLayout) findViewById(R.id.myInfoVa3);
        myInfoVa4=(LinearLayout) findViewById(R.id.myInfoVa4);
        myBuyListLay=(LinearLayout) findViewById(R.id.myBuyListLay);
        myInfoLay=(LinearLayout) findViewById(R.id.myInfoLay);

        myInfoImg1= (ImageView) findViewById(R.id.myInfoImg1);
        myInfoImg2= (ImageView) findViewById(R.id.myInfoImg2);
        myInfoImg3= (ImageView) findViewById(R.id.myInfoImg3);
        myInfoImg4= (ImageView) findViewById(R.id.myInfoImg4);

        tvMyInfoEa1= (TextView) findViewById(R.id.tvMyInfoEa1);
        tvMyInfoEa2= (TextView) findViewById(R.id.tvMyInfoEa2);
        tvMyInfoEa3= (TextView) findViewById(R.id.tvMyInfoEa3);
        tvMyInfoEa4= (TextView) findViewById(R.id.tvMyInfoEa4);
        tvMyinfoStore1= (TextView) findViewById(R.id.tvMyinfoStore1);
        tvMyinfoStore2= (TextView) findViewById(R.id.tvMyinfoStore2);
        tvMyinfoStore3= (TextView) findViewById(R.id.tvMyinfoStore3);
        tvMyinfoStore4= (TextView) findViewById(R.id.tvMyinfoStore4);
        tvMyInfoVa1= (TextView) findViewById(R.id.tvMyInfoVa1);
        tvMyInfoVa2= (TextView) findViewById(R.id.tvMyInfoVa2);
        tvMyInfoVa3= (TextView) findViewById(R.id.tvMyInfoVa3);
        tvMyInfoVa4= (TextView) findViewById(R.id.tvMyInfoVa4);

        myInfoMoney1= (TextView) findViewById(R.id.myInfoMoney1);
        btnInfo=(Button) findViewById(R.id.btnInfo);

        btnInfo.setOnClickListener(mClickListener);

    }


    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnBuy://최 하단 채소정보 버튼
                    layoutView();
                    reviewLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnBot://최 하단 장바구니 버튼
                    layoutView();
                    bottleLayout.setVisibility(View.VISIBLE);
                    fyBot2.setVisibility(View.INVISIBLE);
                    if (onionTotal > 0 || carrotTotal > 0 || tomatoTotal > 0 || potatoTotal > 0) {
                        fyBotWait.setVisibility(View.GONE);//장바구니 대기화면 \off
                        fyBot1.setVisibility(View.VISIBLE);
                    } else {
                        fyBotWait.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btnMy:
                    layoutView();
                    myLayout.setVisibility(View.VISIBLE);
                    myInfoLay.setVisibility(View.GONE);
                    if (onionTotalEa > 0 || carrotTotalEa > 0 || tomatoTotalEa > 0 || potatoTotalEa > 0) {
                        fyInfoWait.setVisibility(View.GONE);
                        myBuyListLay.setVisibility(View.VISIBLE);
                    } else {
                        fyInfoWait.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btnShop:
                    layoutView();
                    shopLayout.setVisibility(View.VISIBLE);

                    break;
//shop 레이아웃 양파제어 버튼===========================================================================
//shop 레이아웃 양파제어 버튼===========================================================================
//shop 레이아웃 양파제어 버튼===========================================================================
                case R.id.btnP1://양파증감버튼========================================================
                    if ((edCnt1.getText().toString()).equals(""))//에디트 텍스트 창에 공란 입력시 변수화 표시부분을 0으로 초기화
                    {
                        OnionEa = 0;
                        edCnt1.setText((OnionEa + ""));
                    }
                    OnionEa = Integer.parseInt(edCnt1.getText().toString());

                    if ((OnionEa + onionTotalEa) < 100) {

                        if (OnionEa > 100) {
                            Toast.makeText(getApplicationContext(), "1회 최대 100개", Toast.LENGTH_SHORT).show();
                            OnionEa = 100;
                            edCnt1.setText((OnionEa + ""));
                        } else {
                            OnionEa++;
                            edCnt1.setText(OnionEa + "");
                            tvOnionAni.startAnimation(tvUp);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "상품 최대구매 개수는 1000개 입니다. ", Toast.LENGTH_SHORT).show();
                    }
                    break;

//                    OnionEa++;
//                    edCnt1.setText(OnionEa+"");
//                    tvOnionAni.startAnimation(tvUp);
//                    break;
                case R.id.btnM1://양파감소버튼========================================================
                    OnionEa--;
                    if (OnionEa <= 0) {
                        OnionEa = 0;
                        edCnt1.setText(OnionEa + "");
                    } else {
                        edCnt1.setText(OnionEa + "");
                        tvOnionAni.startAnimation(tvDown);
                    }
                    break;

                case R.id.btnInten1://양파 담기 버튼==================================================
                    if ((edCnt1.getText().toString()).equals(""))//에디트 텍스트 창에 공란 입력시 변수화 표시부분을 0으로 초기화
                    {
                        OnionEa = 0;
                        edCnt1.setText((OnionEa + ""));
                    }
                    OnionEa = Integer.parseInt(edCnt1.getText().toString());
                    if ((OnionEa + onionTotalEa) <= 100) {
                        int su1 = Integer.parseInt(edCnt1.getText().toString());//에디트 텍스트 창의 값을 받아온다
                        OnionEa = su1;

                        if (OnionEa == 0 || su1 == 0) {
                            Toast.makeText(getApplicationContext(), "채소를 담아 주세요.", Toast.LENGTH_SHORT).show();
                        } else if (OnionEa > 0) {
                            if (OnionEa > 100) {
                                OnionEa = 100;
                                edCnt1.setText(OnionEa + "");
                                Toast.makeText(getApplicationContext(), "1회 최대 100개", Toast.LENGTH_SHORT).show();
                            } else {
                                onionTotal = OnionEa * 1500;
                                onionLayout.setVisibility(View.VISIBLE);
                                tvStore1.setText("업체: 믿음채소");
                                tvVag1.setText("양파");
                                tvEa1.setText(OnionEa + "");
                                tvCoinTol1.setText((onionTotal + "") + "원");
                                imgBot1.setImageResource(R.drawable.yangpa);
                                tvReviewVa1.setText("양파");//장바구니에 내가 담은목록
                                tvReviewCo1.setText(onionTotal + "");//장바구니에 내가 담은목록
                                tvWon1.setText("원");
                                Toast.makeText(getApplicationContext(), "양파가 장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show();
                                fyBotWait.setVisibility(View.GONE);//장바구니 대기화면 \off
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "상품 최대구매 개수는 1000개 입니다. ", Toast.LENGTH_SHORT).show();
                    }
                    break;

//                    if(OnionEa == 0){
//                        Toast.makeText(getApplicationContext(), "채소를 담아 주세요.",  Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        onionTotal =OnionEa*1500;
//                        onionLayout.setVisibility(View.VISIBLE);
//                        tvStore1.setText("업체: 믿음채소");
//                        tvVag1.setText("양파");
//                        tvEa1.setText(OnionEa+"");
//                        tvCoinTol1.setText((onionTotal+"")+"원");
//                        imgBot1.setImageResource(R.drawable.yangpa);
//                        tvReviewVa1.setText("양파");//장바구니에 내가 담은목록
//                        tvReviewCo1.setText(onionTotal+"");//장바구니에 내가 담은목록
//                        tvWon1.setText("원");
//                        Toast.makeText(getApplicationContext(), "양파가 장바구니에 담겼습니다.",  Toast.LENGTH_SHORT).show();
//                        fyBotWait.setVisibility(View.GONE);//장바구니 대기화면 \off
//
//                }
//                    break;


//shop 레이아웃 당근제어 버튼===========================================================================
//==================================================================================================
//==================================================================================================
                case R.id.btnP2://당근 증감버튼
                    if ((edCnt2.getText().toString()).equals(""))//에디트 텍스트 창에 공란 입력시 변수화 표시부분을 0으로 초기화
                    {
                        CarrotEa = 0;
                        edCnt2.setText((CarrotEa + ""));
                    }
                    CarrotEa = Integer.parseInt(edCnt2.getText().toString());

                    if ((CarrotEa + carrotTotalEa) < 100) {
                        if (CarrotEa > 100) {
                            Toast.makeText(getApplicationContext(), "1회 최대 100개", Toast.LENGTH_SHORT).show();
                            CarrotEa = 100;
                            edCnt2.setText((CarrotEa + ""));
                        } else {
                            CarrotEa++;
                            edCnt2.setText(CarrotEa + "");
                            tvCarrotAni.startAnimation(tvUp);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "상품 최대구매 개수는 1000개 입니다. ", Toast.LENGTH_SHORT).show();
                    }
                    break;
//                    CarrotEa++;
//                    edCnt2.setText(CarrotEa+"");
//                    tvCarrotAni.startAnimation(tvUp);
//                    break;
                case R.id.btnM2://당근 감소버튼=======================================================
                    CarrotEa--;
                    if (CarrotEa <= 0) {
                        CarrotEa = 0;
                        edCnt2.setText(CarrotEa + "");
                    } else {
                        edCnt2.setText(CarrotEa + "");
                        tvCarrotAni.startAnimation(tvDown);
                    }
                    break;

                case R.id.btnInten2://당근 담기 버튼==================================================
                    if ((edCnt2.getText().toString()).equals(""))//에디트 텍스트 창에 공란 입력시 변수화 표시부분을 0으로 초기화
                    {
                        CarrotEa = 0;
                        edCnt2.setText((CarrotEa + ""));
                    }
                    CarrotEa = Integer.parseInt(edCnt2.getText().toString());

                    if ((CarrotEa + carrotTotalEa) <= 100) {

                        int su2 = Integer.parseInt(edCnt2.getText().toString());//에디트 텍스트 창의 값을 받아온다
                        CarrotEa = su2;

                        if (CarrotEa == 0 || su2 == 0) {
                            Toast.makeText(getApplicationContext(), "채소를 담아 주세요.", Toast.LENGTH_SHORT).show();
                        } else if (CarrotEa > 0) {
                            if (CarrotEa > 100) {
                                CarrotEa = 100;
                                edCnt2.setText(CarrotEa + "");
                                Toast.makeText(getApplicationContext(), "1회 최대 100개", Toast.LENGTH_SHORT).show();
                            } else {
                                carrotTotal = CarrotEa * 900;
                                carrotLayout.setVisibility(View.VISIBLE);
                                tvStore2.setText("업체: 희망채소");
                                tvVag2.setText("당근");
                                tvEa2.setText(CarrotEa + "");
                                tvCoinTol2.setText((carrotTotal + "") + "원");
                                imgBot2.setImageResource(R.drawable.carrot);
                                tvReviewVa2.setText("당근");//장바구니에 내가 담은목록
                                tvReviewCo2.setText(carrotTotal + "");//장바구니에 내가 담은목록
                                tvWon2.setText("원");
                                Toast.makeText(getApplicationContext(), "당근이 장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show();
                                fyBotWait.setVisibility(View.GONE);//장바구니 대기화면 \off
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "상품 최대구매 개수는 1000개 입니다. ", Toast.LENGTH_SHORT).show();
                    }
                    break;
//                    if(CarrotEa == 0){
//                        Toast.makeText(getApplicationContext(), "채소를 담아 주세요.",  Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        carrotTotal =CarrotEa*900;
//                        carrotLayout.setVisibility(View.VISIBLE);
//                        tvStore2.setText("업체: 희망채소");
//                        tvVag2.setText("당근");
//                        tvEa2.setText(CarrotEa+"");
//                        tvCoinTol2.setText((carrotTotal+"")+"원");
//                        imgBot2.setImageResource(R.drawable.carrot);
//                        tvReviewVa2.setText("당근");//장바구니에 내가 담은목록
//                        tvReviewCo2.setText(carrotTotal+"");//장바구니에 내가 담은목록
//                        tvWon2.setText("원");
//                        Toast.makeText(getApplicationContext(), "당근이 장바구니에 담겼습니다.",  Toast.LENGTH_SHORT).show();
//                        fyBotWait.setVisibility(View.GONE);//장바구니 대기화면 \off
//                    }
//                    break;


//shop 레이아웃 토마토 제어 버튼========================================================================
//==================================================================================================
//==================================================================================================
                case R.id.btnP3://토마토 증감버튼=====================================================

                    if ((edCnt3.getText().toString()).equals(""))//에디트 텍스트 창에 공란 입력시 변수화 표시부분을 0으로 초기화
                    {
                        TomatoEa = 0;
                        edCnt3.setText((TomatoEa + ""));
                    }
                    TomatoEa = Integer.parseInt(edCnt3.getText().toString());
                    if ((TomatoEa + tomatoTotalEa) < 100) {
                        if (TomatoEa > 100) {
                            Toast.makeText(getApplicationContext(), "1회 최대 100개", Toast.LENGTH_SHORT).show();
                            TomatoEa = 100;
                            edCnt3.setText((TomatoEa + ""));
                        } else {
                            TomatoEa++;
                            edCnt3.setText(TomatoEa + "");
                            tvTomatoAni.startAnimation(tvUp);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "상품 최대구매 개수는 1000개 입니다. ", Toast.LENGTH_SHORT).show();
                    }
                    break;
//                    TomatoEa++;
//                    edCnt3.setText(TomatoEa+"");
//                    tvTomatoAni.startAnimation(tvUp);
//                    break;
                case R.id.btnM3://토마토 감소버튼
                    TomatoEa--;
                    if (TomatoEa <= 0) {
                        TomatoEa = 0;
                        edCnt3.setText(TomatoEa + "");
                    } else {
                        tvTomatoAni.startAnimation(tvDown);
                        edCnt3.setText(TomatoEa + "");
                    }
                    break;
                case R.id.btnInten3://토마토 담기 버튼

                    if ((edCnt3.getText().toString()).equals(""))//에디트 텍스트 창에 공란 입력시 변수화 표시부분을 0으로 초기화
                    {
                        TomatoEa = 0;
                        edCnt3.setText((TomatoEa + ""));
                    }
                    TomatoEa = Integer.parseInt(edCnt3.getText().toString());
                    if ((TomatoEa + tomatoTotalEa) <= 100) {
                        int su3 = Integer.parseInt(edCnt3.getText().toString());//에디트 텍스트 창의 값을 받아온다
                        TomatoEa = su3;

                        if (TomatoEa == 0 || su3 == 0) {
                            Toast.makeText(getApplicationContext(), "채소를 담아 주세요.", Toast.LENGTH_SHORT).show();
                        } else if (TomatoEa > 0) {
                            if (TomatoEa > 100) {
                                TomatoEa = 100;
                                edCnt3.setText(TomatoEa + "");
                                Toast.makeText(getApplicationContext(), "1회 최대 100개", Toast.LENGTH_SHORT).show();
                            } else {
                                tomatoTotal = TomatoEa * 900;
                                tomatoLayout.setVisibility(View.VISIBLE);
                                tvStore3.setText("업체: 소망채소");
                                tvVag3.setText("토마토");
                                tvEa3.setText(TomatoEa + "");
                                tvCoinTol3.setText((tomatoTotal + "") + "원");
                                imgBot3.setImageResource(R.drawable.tomato);
                                tvReviewVa3.setText("토마토");//장바구니에 내가 담은목록
                                tvReviewCo3.setText(tomatoTotal + "");//장바구니에 내가 담은목록
                                tvWon3.setText("원");
                                Toast.makeText(getApplicationContext(), "토마토가 장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show();
                                fyBotWait.setVisibility(View.GONE);//장바구니 대기화면 \off
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "상품 최대구매 개수는 1000개 입니다. ", Toast.LENGTH_SHORT).show();
                    }
                    break;
//                    if(TomatoEa == 0){
//                        Toast.makeText(getApplicationContext(), "채소를 담아 주세요.",  Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        tomatoTotal =TomatoEa*900;
//                        tomatoLayout.setVisibility(View.VISIBLE);
//                        tvStore3.setText("업체: 소망채소");
//                        tvVag3.setText("토마토");
//                        tvEa3.setText(TomatoEa+"");
//                        tvCoinTol3.setText((tomatoTotal+"")+"원");
//                        imgBot3.setImageResource(R.drawable.tomato);
//                        tvReviewVa3.setText("토마토");//장바구니에 내가 담은목록
//                        tvReviewCo3.setText(tomatoTotal+"");//장바구니에 내가 담은목록
//                        tvWon3.setText("원");
//                        Toast.makeText(getApplicationContext(), "토마토가 장바구니에 담겼습니다.",  Toast.LENGTH_SHORT).show();
//                        fyBotWait.setVisibility(View.GONE);//장바구니 대기화면 \off
//                    }
//                    break;
//shop 레이아웃 감자 제어 버튼==================================================================
//==========================================================================================
//==========================================================================================

                case R.id.btnP4://감자 증감버튼
                    if ((edCnt4.getText().toString()).equals(""))//에디트 텍스트 창에 공란 입력시 변수화 표시부분을 0으로 초기화
                    {
                        potatoEa = 0;
                        edCnt4.setText((potatoEa + ""));
                    }
                    potatoEa = Integer.parseInt(edCnt4.getText().toString());
                    if ((potatoEa + potatoTotalEa) < 100) {
                        if (potatoEa > 100) {
                            Toast.makeText(getApplicationContext(), "1회 최대 100개", Toast.LENGTH_SHORT).show();
                            potatoEa = 100;
                            edCnt4.setText((potatoEa + ""));
                        } else {
                            potatoEa++;
                            edCnt4.setText(potatoEa + "");
                            tvPotatoAni.startAnimation(tvUp);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "상품 최대구매 개수는 1000개 입니다. ", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnM4://감자 감소버튼
                    potatoEa--;
                    if (potatoEa <= 0) {
                        potatoEa = 0;
                        edCnt4.setText(potatoEa + "");
                    } else {
                        edCnt4.setText(potatoEa + "");
                        tvPotatoAni.startAnimation(tvDown);
                    }
                    break;
                case R.id.btnInten4://감자 담기 부분
                    if ((edCnt4.getText().toString()).equals(""))//에디트 텍스트 창에 공란 입력시 변수화 표시부분을 0으로 초기화
                    {
                        potatoEa = 0;
                        edCnt4.setText((potatoEa + ""));
                    }
                    potatoEa = Integer.parseInt(edCnt4.getText().toString());
                    if ((potatoEa + potatoTotalEa) <= 100) {
                        int su4 = Integer.parseInt(edCnt4.getText().toString());//에디트 텍스트 창의 값을 받아온다
                        potatoEa = su4;

                        if (potatoEa == 0 || su4 == 0) {
                            Toast.makeText(getApplicationContext(), "채소를 담아 주세요.", Toast.LENGTH_SHORT).show();
                        } else if (potatoEa > 0) {
                            if (potatoEa > 100) {
                                potatoEa = 100;
                                edCnt4.setText(potatoEa + "");
                                Toast.makeText(getApplicationContext(), "1회 최대 100개", Toast.LENGTH_SHORT).show();
                            } else {
                                potatoTotal = potatoEa * 500;
                                potatoLayout.setVisibility(View.VISIBLE);
                                tvStore4.setText("업체: 사랑채소");
                                tvVag4.setText("감자");
                                tvEa4.setText(potatoEa + "");
                                tvCoinTol4.setText((potatoTotal + "") + "원");
                                imgBot4.setImageResource(R.drawable.potato);
                                tvReviewVa4.setText("감자");//장바구니에 내가 담은목록
                                tvReviewCo4.setText(potatoTotal + "");//장바구니에 내가 담은목록
                                tvWon4.setText("원");
                                Toast.makeText(getApplicationContext(), "감자가 장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show();
                                fyBotWait.setVisibility(View.GONE);//장바구니 대기화면 \off
                            }
                        }
                    } else{
                        Toast.makeText(getApplicationContext(), "상품 최대구매 개수는 1000개 입니다. ", Toast.LENGTH_SHORT).show();
                    }

                    break;
//장바구니 담김 물품 취소버튼==========================================================================
//장바구니 담김 물품 취소버튼==========================================================================
                case R.id.btnCancel1:
                    OnionEa = 0;//양파 갯수 초기화
                    onionTotal = 0;//양파 가격 초기화
                    onionLayout.setVisibility(View.GONE);//양파레이아웃 가리기.

                    if(CarrotEa==0 &&TomatoEa==0 &&potatoEa==0 )//다른 채소 까지 비워져 있으면 대기화면 on
                    {
                        fyBotWait.setVisibility(View.VISIBLE);
                    } else{
                        fyBotWait.setVisibility(View.GONE);
                    }
                    tvReviewVa1.setText("");//뒷단 결제페이지 내역 초기화
                    tvReviewCo1.setText("");
                    tvWon1.setText("");


                    break;

                case R.id.btnCancel2:
                    CarrotEa  = 0;//당근 갯수 초기화
                    carrotTotal  = 0;//당근 가격 초기화
                    tvVag2.setText("");//빈 공간을 줌으로써
                    carrotLayout.setVisibility(View.GONE);//당근레이아웃 가리기.

                    if(OnionEa==0 &&TomatoEa==0 &&potatoEa==0 )//다른 채소 까지 비워져 있으면 대기화면 on
                    {
                        fyBotWait.setVisibility(View.VISIBLE);
                    } else{
                        fyBotWait.setVisibility(View.GONE);
                    }
                    tvReviewVa2.setText("");//뒷단 결제페이지 내역 초기화
                    tvReviewCo2.setText("");
                    tvWon2.setText("");
                    break;

                case R.id.btnCancel3:
                    TomatoEa = 0;//토마토 갯수 초기화
                    tomatoTotal = 0;//토마토 가격 초기화
                    tvVag3.setText("");//빈 공간을 줌으로써
                    tomatoLayout.setVisibility(View.GONE);//토마토레이아웃 가리기.

                    if(OnionEa==0 &&CarrotEa==0 &&potatoEa==0 )//다른 채소 까지 비워져 있으면 대기화면 on
                    {
                        fyBotWait.setVisibility(View.VISIBLE);
                    }
                    else{
                        fyBotWait.setVisibility(View.GONE);
                    }
                    tvReviewVa3.setText("");//뒷단 결제페이지 내역 초기화
                    tvReviewCo3.setText("");
                    tvWon3.setText("");
                    break;

                case R.id.btnCancel4:
                    potatoEa = 0;//감자 갯수 초기화
                    potatoTotal = 0;//감자 가격 초기화
                    potatoLayout.setVisibility(View.GONE);//토마토레이아웃 가리기.

                    if(OnionEa==0 &&CarrotEa==0 &&TomatoEa==0 )//다른 채소 까지 비워져 있으면 대기화면 on
                    {
                        fyBotWait.setVisibility(View.VISIBLE);
                    }
                    else{
                        fyBotWait.setVisibility(View.GONE);
                    }
                    tvReviewVa4.setText("");//뒷단 결제페이지 내역 초기화
                    tvReviewCo4.setText("");
                    tvWon4.setText("");
                    break;

//장바구니 결제 화면 제어 부분.=========================================================================
//장바구니 결제 화면 제어 부분.=========================================================================
//장바구니 결제 화면 제어 부분.=========================================================================
                case R.id.btnPay:
                    if (onionTotal == 0 && carrotTotal == 0 && tomatoTotal == 0 && potatoTotal == 0) {
                        vageTotalMoney = 0;
                    } else {
                        vageTotalMoney = onionTotal + carrotTotal + tomatoTotal + potatoTotal;
                    }

                    tvTolMoney.setText((vageTotalMoney + "") + "원");//결제 화면창으로 이동 할때 채소값들이 합산되어 보여짐.
                    fyBotWait.setVisibility(View.GONE);//장바구니 안내 화면
                    fyBot1.setVisibility(View.INVISIBLE);
                    fyBot2.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn50000:
                    fillMoeny += 50000;
                    tvInCoin.setText(fillMoeny + "");
                    break;
                case R.id.btn10000:
                    fillMoeny += 10000;
                    tvInCoin.setText(fillMoeny + "");
                    break;
                case R.id.btn5000:
                    fillMoeny += 5000;
                    tvInCoin.setText(fillMoeny + "");
                    break;
                case R.id.btn1000:
                    fillMoeny += 1000;
                    tvInCoin.setText(fillMoeny + "");
                    break;
                case R.id.btnCoinRe:
                    if (fillMoeny <= 0) {
                        tvInfo.setText("금액을 반환 할 수 없습니다..");
                    } else {
                        fillMoeny = 0;
                        tvInCoin.setText(fillMoeny + "");
                        tvInfo.setText("금액이 반환 됩니다.");

//                        try {
//                            sleep(3000);
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                case R.id.btnPayment://장바구니 안 최종결제부분.

                    if (vageTotalMoney <= fillMoeny && vageTotalMoney != 0) {

                        int num = fillMoeny - vageTotalMoney;
                        tvInfo.setText("결제가 완료 되었습니다.");
                        tvInCoin.setText(num + "");
                        tvTolMoney.setText("");
                        fillMoeny = num;
                        stayMoney = num;

                        tvReviewVa1.setText("");//장바구니에 내가 담은목록
                        tvReviewCo1.setText("");//구매완료 후 초기화
                        tvWon1.setText("");
                        tvReviewVa2.setText("");
                        tvReviewCo2.setText("");
                        tvWon2.setText("");
                        tvReviewVa3.setText("");
                        tvReviewCo3.setText("");
                        tvWon3.setText("");
                        tvReviewVa4.setText("");
                        tvReviewCo4.setText("");
                        tvWon4.setText("");

                        tvTolMoney.setText("");//담겼던 야채 금액 초기화.
                        vageTotalMoney = 0;


                        onionLayout.setVisibility(View.GONE);//결제가 다 되어 장바구니의 각야채 레이아웃 곤
                        carrotLayout.setVisibility(View.GONE);
                        tomatoLayout.setVisibility(View.GONE);
                        potatoLayout.setVisibility(View.GONE);

                        if (onionTotal > 0) {
                            //양파 관련 구매내역 남기기
                            myInfoVa1.setVisibility(View.VISIBLE);//나의정보창에 구매내역 확인 뷰.
                            myInfoImg1.setImageResource(R.drawable.yangpa);
                            tvMyinfoStore1.setText("업체: 믿음채소");
                            tvMyInfoVa1.setText("양파");
                            onionTotalEa = OnionEa + onionTotalEa;//나의 정보에 누적될 최종갯수
                            tvMyInfoEa1.setText(onionTotalEa + "");
                        }

                        if (carrotTotal > 0) {
                            //당근 관련 구매내역 남기기
                            myInfoVa2.setVisibility(View.VISIBLE);//나의정보창에 구매내역 확인 뷰.
                            myInfoImg2.setImageResource(R.drawable.carrot);
                            tvMyinfoStore2.setText("업체: 희망채소");
                            tvMyInfoVa2.setText("당근");
                            carrotTotalEa = CarrotEa + carrotTotalEa;//나의 정보에 누적될 최종갯수
                            tvMyInfoEa2.setText(carrotTotalEa + "");
                        }

                        if (tomatoTotal > 0) {
                            //토마토 관련 구매내역 남기기
                            myInfoVa3.setVisibility(View.VISIBLE);//나의정보창에 구매내역 확인 뷰.
                            myInfoImg3.setImageResource(R.drawable.tomato);
                            tvMyinfoStore3.setText("업체: 소망채소");
                            tvMyInfoVa3.setText("토마토");
                            tomatoTotalEa = TomatoEa + tomatoTotalEa;//나의 정보에 누적될 최종갯수
                            tvMyInfoEa3.setText(tomatoTotalEa + "");
                        }
                        if (potatoTotal > 0) {
                            //감자 구매내역 남기기
                            myInfoVa4.setVisibility(View.VISIBLE);//나의정보창에 구매내역 확인 뷰.
                            myInfoImg4.setImageResource(R.drawable.potato);
                            tvMyinfoStore4.setText("업체: 사랑채소");
                            tvMyInfoVa4.setText("감자");
                            potatoTotalEa = potatoEa + potatoTotalEa;//나의 정보에 누적될 최종갯수
                            tvMyInfoEa4.setText(potatoTotalEa + "");
                        }


                        onionTotal = 0;//각 야채 총금액들 초기화
                        carrotTotal = 0;
                        tomatoTotal = 0;
                        potatoTotal = 0;

                        OnionEa = 0;//구매완료 후 shop 레이아웃 갯수 초기화
                        CarrotEa = 0;
                        TomatoEa = 0;
                        potatoEa = 0;

                        edCnt1.setText("0");//구매완료 후 shop 레이아웃 부분의 갯수표시 초기화
                        edCnt2.setText("0");
                        edCnt3.setText("0");
                        edCnt4.setText("0");
                    } else if (vageTotalMoney > fillMoeny && vageTotalMoney != 0) {
                        tvInfo.setText("충전금액이 더 필요 합니다.");
                    } else if (vageTotalMoney == 0 && fillMoeny == 0) {
                        tvInfo.setText("상품을 장바구니에 담아주세요.");
                    } else if (fillMoeny > 0 && vageTotalMoney == 0) {
                        tvInfo.setText("상품을 장바구니에 담아주세요.");
                    }
                    break;


                case R.id.btnInfo:
                    myInfoLay.setVisibility(View.VISIBLE);
                    myBuyListLay.setVisibility(View.GONE);
                    fyInfoWait.setVisibility(View.GONE);

                    edFind.setText("");//화면 재 진입시 초기화
                    edName.setText("");
                    edHp.setText("");
                    tvUpName1.setText("");
                    tvUpHp1.setText("");
                    break;
                //개인정보 수정버튼====================================================
                case R.id.btnFind:

                    String idUpdateFind = edFind.getText().toString();
                    if (idUpdateFind.equals("")) {
                        break;
                    }

                    selectUpdate(idUpdateFind);
                    break;
                case R.id.btnUpdate:

                    String inputUpdateName = edName.getText().toString();
                    String inputUpdateHp = edHp.getText().toString();

                    String inputUpdateId = edFind.getText().toString();
                    if (inputUpdateId.equals("")) {
                        Toast.makeText(getApplicationContext(), "수정대상 아이디를 입력하세요.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if (inputUpdateName.equals("")) {
                        Toast.makeText(getApplicationContext(), "수정대상 이름을 입력하세요.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if (inputUpdateHp.equals("")) {
                        Toast.makeText(getApplicationContext(), "수정대상 연락처를 입력하세요.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    update(inputUpdateId, inputUpdateName, inputUpdateHp);
                    break;
            }
        }

    };


    //메인 4개 레이아웃 인비지블 메소드================================================================
    public void layoutView()
    {
        shopLayout.setVisibility(View.INVISIBLE);
        reviewLayout.setVisibility(View.INVISIBLE);
        bottleLayout.setVisibility(View.INVISIBLE);
        myLayout.setVisibility(View.INVISIBLE);
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

        edName.setText(name);
        edHp.setText(hp);
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
        tvUpName1.setText(name);
        tvUpHp1.setText(hp);
    }
}
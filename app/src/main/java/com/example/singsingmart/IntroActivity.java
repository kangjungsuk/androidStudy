package com.example.singsingmart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    TextView textView;
    Animation ani1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        textView = (TextView) findViewById(R.id.textView);
        ani1 = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.alpha);
        textView.startAnimation(ani1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {

                //동작수행할 부분
                Intent intent1 = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
            }

        },4000); //4초 후 인트로 실행
    }
}
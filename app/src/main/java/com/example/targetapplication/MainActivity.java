package com.example.targetapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
    AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.0f);


    public void toTarget(View view){

        Intent targetActivity = new Intent(getApplicationContext(), TargetPlatForm.class);
        startActivity(targetActivity);

    }

    public String readFromFile(Context context) {

        String ret = "";
        String fileName = "highscore.txt";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                if(stringBuilder.length() != 0) {
                    ret = stringBuilder.toString();
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        if(ret.equals("")){
            return "0";
        }

        return ret;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //puts screen in portrait mode
        setContentView(R.layout.activity_main);
        final Button start = (Button) findViewById(R.id.start);
        TextView highScore = (TextView) findViewById(R.id.highscore);

        String score = readFromFile(MainActivity.this);
        highScore.setText("HighScore: " + score);

        start.startAnimation(animation1);
        animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(2000);
        animation1.setStartOffset(1000);
        animation1.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                start.startAnimation(animation2);
            }

         /*   @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            } */

        });

        animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(2000);
        animation2.setStartOffset(1000);

        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                start.startAnimation(animation1);
            }
/*
            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            } */

        });


        start.startAnimation(animation1);

    }
}

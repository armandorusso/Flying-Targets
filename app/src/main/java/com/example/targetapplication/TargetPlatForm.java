package com.example.targetapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class TargetPlatForm extends AppCompatActivity {
    int score = 0;
    int x;
    int y;
    int lives = 0;
    long delay = 1250;
    long targetMoveDuration = 600;
    float destinationX = 0;
    float destinationY = 0;
    private Handler handler = new Handler();
    ImageView targets;
    int cloudNumber = 0;
    int targetNumber = 0;
    String cloudString = "cloud" + cloudNumber;
    int [] imageArray = new int[3];
    int[] targetArray = new int[5];
    int cloud1 = R.drawable.cloud1;
    int Cloud2 = R.drawable.cloud2;
    int cloud3 = R.drawable.cloud3;
    ImageView cloudImage1;
    ImageView cloudImage2;
    ImageView cloudImage3;
    ImageView cloudImage4;
    ImageView targetImage1;
 
    //on touch method
    public void moveTarget(View view) {

        move.run();

        if(score % 10 == 0 && targetMoveDuration > 500){
            targetMoveDuration -= 15;
        }

            moveIt();
            //final ImageView targets = (ImageView) findViewById(R.id.target);
            TextView counter = (TextView) findViewById(R.id.counter);

            view.setOnTouchListener(handleTouch);

            Log.i("Coordinates", x + " " + y);

            if (x == 0 && y == 0) {
                Log.i("Touch input", "miss");
                counter.setText(0 + "");
            } else if ((x >= 105 && x <= 128) || (y >= 105 && y <= 128)) { //if its a hit in the middle
                Log.i("Touch Input", "you hit the middle! +3 points!");
                score += 3;
                counter.setText(score + "");
                x = 0;
                y = 0;
            } else { //if its a hit, but not in the middle
                Log.i("Touch Input", "you hit the target! +1 points!");
                score++;
                counter.setText(score + "");
                x = 0;
                y = 0;
            }

    }

    //game over method
    public void setToZero(View view){

        ImageView background = (ImageView) findViewById(R.id.background);
        ImageView target = (ImageView) findViewById(R.id.target);
        TextView counter = (TextView) findViewById(R.id.counter);
        TextView startText = (TextView) findViewById(R.id.startText);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        writeToFile(score, TargetPlatForm.this);//set the new highscore

        score = 0;
        counter.setText(score + "");
        target.animate().translationX(0).setDuration(50);
        target.animate().translationY(0).setDuration(50);
        startText.setVisibility(View.VISIBLE);
        x = 0;
        y = 0;
        lives = 0;
        targetMoveDuration = 600;
        delay = 1250;
        lives = 0;
        destinationX = 0;
        destinationY = 0;
        handler.removeCallbacks(move);
        startText.setText("Game Over...Tap to try again");

    }

    public String readFromFile(Context context) {

        String ret = "";
        String fileName = "highscore.txt";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                Log.i("length of builder: ", stringBuilder.length() + "");
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
        if(ret.equals("")) {
            Log.i("In the file: ", ret);
            return "0";
        }
        Log.i("ret is: ", ret);
        return ret;
    }

    //writing the highscore
    public void writeToFile(int score, Context context) {
        String currentHighScore = readFromFile(TargetPlatForm.this);
        if(score > Integer.parseInt(currentHighScore)) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("highscore.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(score + "");
                //Toast.makeText(TargetPlatForm.this,"saved to " + getFilesDir() + "/" + "config.txt",Toast.LENGTH_LONG).show();
                outputStreamWriter.close();
            }
            catch (FileNotFoundException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }

    }


    //method to obtain the touch coordinates of the image
    private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getX();
                    y = (int) event.getY();
                    Log.i("TAG", "touched down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("TAG", "moving: (" + x + ", " + y + ")");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("TAG", "touched up");
                    break;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //puts screen in portrait mode
        setContentView(R.layout.activity_target_plat_form);
        targets = (ImageView) findViewById(R.id.target);
        ImageView cloud2 = (ImageView) findViewById(R.id.cloud2);
        imageArray[0] = cloud1;
        imageArray[1] = Cloud2;
        imageArray[2] = cloud3;
        cloudImage1 = (ImageView) findViewById(R.id.cloud1);
        cloudImage2 = (ImageView) findViewById(R.id.cloud2);
        cloudImage3 = (ImageView) findViewById(R.id.cloud3);
        cloudImage4 = (ImageView) findViewById(R.id.cloud4);


        targetImage1 = (ImageView) findViewById(R.id.target);
       
        cloudMovement.run();

    }

    //method to move the target. it takes into account the phone screen size so it doesnt go off screen
    public void moveIt(){
        TextView counter = (TextView) findViewById(R.id.counter);
        TextView startText = (TextView) findViewById(R.id.startText);

        startText.setVisibility(View.INVISIBLE);


        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        float maxWidth = (displayMetrics.widthPixels / 2) - 75; //256 is the pic size (256x256). The 256 is there so the image does not go off screen
        float minWidth = (0 - (displayMetrics.widthPixels / 2)) + 75;
        float maxHeight = (displayMetrics.heightPixels / 2) - 75;
        float minHeight = (0 - (displayMetrics.widthPixels / 2) +75);

        Log.i("e", " Destination X: " + destinationX + " GetX: " + targets.getX() + " TranslationX: " + targets.getTranslationX());
        if(destinationX == 0 || destinationY == 0) {
            float randomCoordinateWidth = Float.parseFloat((Math.random() * ((maxWidth - minWidth) + 1) + minWidth) + "");
            float randomCoordinateHeight = Float.parseFloat((Math.random() * ((maxHeight - minHeight) + 1) + minHeight) + "");
            destinationX = randomCoordinateWidth;
            destinationY = randomCoordinateHeight;
            targets.animate().translationX(randomCoordinateWidth).setDuration(targetMoveDuration);
            targets.animate().translationY(randomCoordinateHeight).setDuration(targetMoveDuration);
        }

        else if (Math.floor(destinationX) == Math.floor(targets.getTranslationX()) && Math.floor(destinationY) == Math.floor(targets.getTranslationY())){
            Log.i("else if", destinationX + " " + targets.getX() + " " + targets.getTranslationX());
            destinationX = 0;
            destinationY = 0;
        }

    }


    //runnable is used to infinitely loop over a function. here i use it to keep on calling the moveIt function, which moves the target
    private Runnable move = new Runnable() {
        @Override
        public void run() {
            moveIt(); //try putting the imageview in here in order to make a gif...
            handler.postDelayed(this, delay);
        }
    };

    private Runnable cloudMovement = new Runnable() {

        public void run() {
            if (cloudNumber < 3) {
                cloudImage1.setImageResource(imageArray[cloudNumber]);
                cloudImage2.setImageResource(imageArray[cloudNumber]);
                cloudImage3.setImageResource(imageArray[cloudNumber]);
                cloudImage4.setImageResource(imageArray[cloudNumber]);
                cloudNumber++;
            }
            else {
                cloudNumber = 0;
                cloudImage1.setImageResource(imageArray[cloudNumber]);
                cloudImage2.setImageResource(imageArray[cloudNumber]);
                cloudImage3.setImageResource(imageArray[cloudNumber]);
                cloudImage4.setImageResource(imageArray[cloudNumber]);
            }

            handler.postDelayed(this, 500);
        }

    };
}

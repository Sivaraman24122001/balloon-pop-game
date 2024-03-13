package com.addnoapp.poptheballoon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private TextView scoreTextView;
    private ImageView balloonImageView;
    private Button startButton;
    private Button restartButton;

    private int score = 0;
    private int missed = 0;
    private CountDownTimer timer;
    private Random random = new Random();
    private boolean gameRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        balloonImageView = findViewById(R.id.balloonImageView);



        startButton = findViewById(R.id.startButton);
        restartButton = findViewById(R.id.restartButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameRunning) { startGame();
                }
            }
        }
        );
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!gameRunning) {

                    restartGame();
                }
            }
        });
    }

    public void onStartButtonClick(View view) {
        startGame();
    }

    private void startGame() {
        gameRunning = true;
        startButton.setVisibility(View.GONE);
        restartButton.setVisibility(View.INVISIBLE);


        timer = new CountDownTimer(120000, 1000) { // 2 minutes
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time Remaining: " + millisUntilFinished / 60000 + ":" + (millisUntilFinished / 1000) % 60);
            }
            public void onFinish() {
                endGame();
            }
        }.start();



        balloonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  popBalloon();
            }
        });


        generateBalloon();
    }

    private void restartGame() {
        score = 0;
        missed = 0;

        scoreTextView.setText("Score: " + score + " | Missed: " + missed);

        startGame();
    }

    private void generateBalloon() {
        if (!gameRunning) {
            return;
        }

        int randomX = random.nextInt(800);
        int randomDuration = random.nextInt(2000) + 1000;

        balloonImageView.setX(randomX);
        balloonImageView.setVisibility(View.VISIBLE);

        balloonImageView.animate()
                .translationY(-800)

                .setDuration(randomDuration).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (gameRunning) {

                            missed++;
                            scoreTextView.setText("Score: " + score + " | Missed: " + missed);
                            balloonImageView.setVisibility(View.INVISIBLE);
                            generateBalloon();
                        }
                    }
                });
    }

    private void popBalloon() {
        if (gameRunning) {
            score += 2;
            scoreTextView.setText("Score: " + score + " | Missed: " + missed);
            balloonImageView.setVisibility(View.INVISIBLE);
            generateBalloon();
        }
    }

    private void endGame() {
        gameRunning = false;
        timer.cancel();
        Toast.makeText(MainActivity.this, "Game Over\nScore: " + score + "\nBalloons Missed: " + missed, Toast.LENGTH_LONG).show();
        restartButton.setVisibility(View.VISIBLE);
    }

}
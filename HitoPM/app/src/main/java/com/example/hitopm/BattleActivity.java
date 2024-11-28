package com.example.hitopm;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Importaciones adicionales para Firebase
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class BattleActivity extends AppCompatActivity {

    private TextView scoreTextView, enemyHealthTextView, levelTextView;
    private Button attackButton, endGameButton;
    private int enemyHealth = 100;
    private long startTime;
    private Player player;
    private String playerName;
    // Referencia a Firebase
    private DatabaseReference firebaseDatabase;


    private void setPlayer(Player player) {
        this.player = player;
    }

    public void dataBaseCallBack(ArrayList<Player> players){
        for(Player player:players) {
            if (player.getUsername().equals(playerName))
                setPlayer(player);
        }
        levelTextView.setText("Level: " + player.getLevel());
        startTime = SystemClock.elapsedRealtime();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        scoreTextView = findViewById(R.id.score_text_view);
        enemyHealthTextView = findViewById(R.id.enemy_health_text_view);
        levelTextView = findViewById(R.id.level_text_view);
        attackButton = findViewById(R.id.attack_button);
        endGameButton = findViewById(R.id.end_game_button);

        String userName = getIntent().getStringExtra("username");
        playerName = userName;

        if (Configuration.isUseFirebase()) {
            player = new Player(userName, 0, 1);  // Cargar datos desde Firebase en el futuro
            FirebaseHelper.loadRankingData(this, new FirebaseHelper.DataCallback() {
                @Override
                public void onDataLoaded(ArrayList<Player> players) {
                    dataBaseCallBack(players);
                }

                @Override
                public void onError(String error) {
                    Log.e("BattleActivity", "Error loading data: " + error);
                }
            });

            //loadRankingFromFirebase();
        } else {
            player = DatabaseHelper.loadPlayerData(this, userName);
            levelTextView.setText("Level: " + player.getLevel());
            startTime = SystemClock.elapsedRealtime();
        }

        attackButton.setOnClickListener(v -> {
            enemyHealth -= 10;
            enemyHealthTextView.setText("Enemy Health: " + enemyHealth);

            if (enemyHealth <= 0) {
                player.increaseLevel();
                calculateScore();
            }
        });

        endGameButton.setOnClickListener(v -> calculateScore());
    }

    private void calculateScore() {
        long endTime = SystemClock.elapsedRealtime();
        long elapsedTime = (endTime - startTime) / 1000;

        int score = Math.max(1000 - (int) (elapsedTime * 50), 100);
        scoreTextView.setText("Score: " + score);

        player = new Player(player.getUsername(), score, player.getLevel());

        DatabaseHelper.savePlayerScore(this, player);

        Intent intent = new Intent(BattleActivity.this, RankingActivity.class);
        startActivity(intent);
        finish();
    }

    //private void savePlayerScoreToFirebase() {
    //    firebaseDatabase.child(player.getUsername()).setValue(player);
    //}
}

package com.example.hitopm;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

// Importaciones adicionales para Firebase
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RankingActivity extends AppCompatActivity {

    private ListView rankingListView;
    private RankingAdapter adapter;

    private static DatabaseReference firebaseDatabase;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rankingListView = findViewById(R.id.list_ranking);

        if (Configuration.isUseFirebase()) {
            loadRankingFromFirebase();
        } else {
            ArrayList<Player> players = loadRankingDataFromJSON();
            setupAdapter(players);
        }
    }

    public void dataBaseCallBack(ArrayList<Player> players){
         setupAdapter(players);
    }

    private void loadRankingFromFirebase() {
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
    }

    private ArrayList<Player> loadRankingDataFromJSON() {
        ArrayList<Player> players = new ArrayList<>();
        try {
            JSONArray rankingArray = DatabaseHelper.loadRankingData(this);
            for (int i = 0; i < rankingArray.length(); i++) {
                JSONObject playerObject = rankingArray.getJSONObject(i);
                String username = playerObject.getString("username");
                int score = playerObject.getInt("score");
                int level = playerObject.optInt("level", 1);
                players.add(new Player(username, score, level));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }


    private void setupAdapter(ArrayList<Player> players) {
        players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
        adapter = new RankingAdapter(this, players);
        rankingListView.setAdapter(adapter);
    }
}

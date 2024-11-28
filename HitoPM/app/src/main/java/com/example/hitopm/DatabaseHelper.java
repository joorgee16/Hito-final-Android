package com.example.hitopm;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseHelper {
    private static final String FILE_NAME = "ranking_data.json";


    // Guardar puntuación y nivel del jugador
    public static void savePlayerScore(Context context, Player player) {
        if (Configuration.isUseFirebase()) {
            savePlayerScoreToFirebase(player);
        } else {
            savePlayerScoreToJSON(context, player);
        }
    }

    private static void savePlayerScoreToFirebase(Player player) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("ranking");
        databaseRef.child(player.getUsername()).setValue(player);
    }

    public static void savePlayerScoreToJSON(Context context, Player player) {
        try {
            // Cargar los datos existentes del archivo
            JSONArray rankingArray = loadRankingData(context);

            // Verificar si el jugador ya existe en el ranking
            boolean playerExists = false;
            for (int i = 0; i < rankingArray.length(); i++) {
                JSONObject existingPlayer = rankingArray.getJSONObject(i);
                if (existingPlayer.getString("username").equals(player.getUsername())) {
                    // Actualizar los datos del jugador existente
                    existingPlayer.put("score", player.getScore());
                    existingPlayer.put("level", player.getLevel());
                    playerExists = true;
                    break;
                }
            }

            // Si el jugador no existe, añadirlo al JSONArray
            if (!playerExists) {
                JSONObject playerJson = new JSONObject();
                playerJson.put("username", player.getUsername());
                playerJson.put("score", player.getScore());
                playerJson.put("level", player.getLevel());
                rankingArray.put(playerJson);
            }

            // Guardar el JSONArray actualizado de vuelta en el archivo
            FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(rankingArray.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.close();

        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error saving player data", e);
        }
    }



    public static JSONArray loadRankingData(Context context) {
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            return new JSONArray(jsonString);
        } catch (Exception e) {
            // Si el archivo no existe o hay un error, devolver un array vacío
            return new JSONArray();
        }
    }

    // Cargar datos específicos de un jugador
    public static Player loadPlayerData(Context context, String username) {
        try {
            JSONArray rankingArray = loadRankingData(context);
            for (int i = 0; i < rankingArray.length(); i++) {
                JSONObject playerJson = rankingArray.getJSONObject(i);
                if (playerJson.getString("username").equals(username)) {
                    int score = playerJson.getInt("score");
                    int level = playerJson.getInt("level");
                    return new Player(username, score, level);
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error loading player data", e);
        }
        return new Player(username, 0, 1); // Si no existe, inicia en nivel 1
    }
}



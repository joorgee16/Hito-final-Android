package com.example.hitopm;

import android.content.Context;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class FirebaseHelper {

    public interface DataCallback {
        void onDataLoaded(ArrayList<Player> players);
        void onError(String error);
    }

    public static void loadRankingData(Context context, DataCallback callback) {
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("ranking");

        // Usamos `addListenerForSingleValueEvent` para obtener los datos una vez.
        firebaseDatabase.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Si el task fue exitoso, procesamos los datos
                DataSnapshot dataSnapshot = task.getResult();
                ArrayList<Player> players = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Player player = snapshot.getValue(Player.class);
                    players.add(player);
                }

                // Llamamos al callback con los jugadores obtenidos
                callback.onDataLoaded(players);
            } else {
                // Si hubo un error, lo pasamos al callback
                callback.onError("Error loading data: " + task.getException().getMessage());
            }
        });
    }
}

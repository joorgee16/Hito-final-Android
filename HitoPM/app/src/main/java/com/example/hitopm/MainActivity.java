package com.example.hitopm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username_edit_text);
        startButton = findViewById(R.id.start_button);

        startButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pasa el nombre a la siguiente actividad (BattleActivity)
            Intent intent = new Intent(MainActivity.this, BattleActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}

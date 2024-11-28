package com.example.hitopm;

public class Player {
    private String username;
    private int score;
    private int level;

    public Player() { } // Constructor vacío para Firebase

    public Player(String username, int score, int level) {
        this.username = username;
        this.score = score;
        this.level = level;
    }

    // Getters y setters necesarios para Firebase
    public String getUsername() { return username; }
    public int getScore() { return score; }
    public int getLevel() { return level; }

    public void setUsername(String username) { this.username = username; }
    public void setScore(int score) { this.score = score; }
    public void setLevel(int level) { this.level = level; }

    public void increaseLevel() { this.level++; }
}


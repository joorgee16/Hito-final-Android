package com.example.hitopm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RankingAdapter extends ArrayAdapter<Player> {

    public RankingAdapter(Context context, ArrayList<Player> players) {
        super(context, 0, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Player player = getItem(position);

        TextView usernameTextView = convertView.findViewById(android.R.id.text1);
        TextView scoreTextView = convertView.findViewById(android.R.id.text2);

        if (player != null) {
            usernameTextView.setText(player.getUsername());
            scoreTextView.setText(String.valueOf(player.getScore()));
        }

        return convertView;
    }
}

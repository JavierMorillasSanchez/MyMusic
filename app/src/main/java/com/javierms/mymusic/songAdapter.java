package com.javierms.mymusic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class songAdapter extends ArrayAdapter<Song> {
    public songAdapter (@NonNull Context context, @NonNull List<Song> objects){
        super (context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemsong, null);

        TextView txtTitulo = convertView.findViewById(R.id.txtSongTitle);
        TextView txtAuthor = convertView.findViewById(R.id.txtAuthorName);

        Song song = getItem(position);
        txtTitulo.setText(song.getTitle());
        txtAuthor.setText(song.getArtist());


        return convertView;
    }
}

package com.javierms.mymusic;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class songList extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 99;
    ArrayList<Song> songArrayList;
    ListView lvSongs;
    songAdapter songsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        lvSongs = findViewById(R.id.lvSongs);
        songArrayList = new ArrayList<>();

        songsAdapter = new songAdapter(this, songArrayList);

        lvSongs.setAdapter(songsAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION );
            return;
        } else {
            getSongs();
        }

        playSong();

    }

    public void playSong(){
        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = songArrayList.get(position);
                Intent openMusicPlayer = new Intent(getApplicationContext(), MainActivity.class);
                openMusicPlayer.putExtra("objSong", song);
                startActivity(openMusicPlayer);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getSongs();
            }

        }
    }

    private void getSongs(){

        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songUri, null, null, null,null);
        if(songCursor != null && songCursor.moveToFirst()){

            int indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int indexArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int indexData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{
                String title = songCursor.getString(indexTitle);
                String artist = songCursor.getString(indexArtist);
                String path = songCursor.getString(indexData);
                Song song = new Song(title,artist,path);
                songArrayList.add(song);
            } while (songCursor.moveToNext());
        }

        songsAdapter.notifyDataSetChanged();

        //todo: tengo que ver como elegir la carpeta a la que quiero acceder
        //todo: toolbar para que vaya a la lista de canciones y al propio reproductor
        //todo: ecualizador para darle animacion a la musica
        //todo: que la app siga funcionando aunque el telefono este con la pantalla apagada.

    }
}
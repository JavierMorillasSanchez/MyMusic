package com.javierms.mymusic;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView txtDuracionInicial, txtDuracionFinal, txtCancion, txtAutor;
    SeekBar skbDuracion;
    ImageView btnPlayPause;

    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Song song = (Song) getIntent().getSerializableExtra("objSong");

        txtDuracionInicial = findViewById(R.id.txtDuracionInicial);
        txtDuracionFinal = findViewById(R.id.txtDuracionFinal);
        skbDuracion = findViewById(R.id.skbDuracion);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        txtCancion = findViewById(R.id.txtCancion);
        txtAutor = findViewById(R.id.txtAutor);

        skbDuracion.setProgress(0);
        txtCancion.setText(song.getTitle());
        txtAutor.setText(song.getArtist());


        musicPlayer = new MediaPlayer();

        musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {



        try {

            musicPlayer.setDataSource(song.getPath());
            musicPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f,0.5f);

        String duration = milisecondstoString(musicPlayer.getDuration());
        txtDuracionFinal.setText(duration);

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicPlayer.isPlaying()){
                    musicPlayer.pause();
                    btnPlayPause.setImageResource(R.drawable.botonplay);
                } else {
                    musicPlayer.start();
                    btnPlayPause.setImageResource(R.drawable.botonpause);
                }
            }
        });

        skbDuracion.setMax(musicPlayer.getDuration());
        skbDuracion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if (isFromUser){
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (musicPlayer!=null){
                    if(musicPlayer.isPlaying()){
                        try {
                            final double current = musicPlayer.getCurrentPosition();
                            final String elapsedTime = milisecondstoString((int) current);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtDuracionInicial.setText(elapsedTime);
                                    skbDuracion.setProgress(((int) current));
                                }
                            });

                            Thread.sleep(1000);
                        } catch (InterruptedException e){}

                    }
                }
            }
        }).start();


            }
        });

        musicPlayer.start();


    } // end main

    public String milisecondstoString (int songDuration){
        String elapsedTime = "";
        int minutes = songDuration / 1000 / 60;
        int seconds = songDuration / 1000 % 60;
        elapsedTime = minutes+":";
        if(seconds<10){
            elapsedTime +="0";
        }

        elapsedTime +=seconds;

        return elapsedTime;
        }
}

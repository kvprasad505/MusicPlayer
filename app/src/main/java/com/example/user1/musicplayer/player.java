package com.example.user1.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class player extends AppCompatActivity implements View.OnClickListener{

    static MediaPlayer mp;
    ArrayList<File> mysongs;
    Uri u;
    int position;
    Thread updateseekbar;
    TextView txtname;
    String name;
    Button prebtn,nextbtn,prefast,nextfast,playbtn;
    SeekBar sb;
    CircleImageView backbtn,profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
            setContentView(R.layout.activity_player);
            txtname=(TextView)findViewById(R.id.song_name);
            prebtn = (Button)findViewById(R.id.prebtn);
            nextbtn = (Button) findViewById(R.id.nextbtn);
            prefast = (Button) findViewById(R.id.prefast);
            nextfast = (Button) findViewById(R.id.nextfast);
            playbtn = (Button) findViewById(R.id.playtbn);
            backbtn = (CircleImageView) findViewById(R.id.backbtn);
            profile_image = (CircleImageView) findViewById(R.id.profile_image);
            sb=(SeekBar)findViewById(R.id.sb);

        prebtn.setOnClickListener(this);
        prefast.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        nextfast.setOnClickListener(this);
        playbtn.setOnClickListener(this);
        backbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent back = new Intent(player.this,MainActivity.class);
                startActivity(back);
            }
        });



        updateseekbar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentpostion = 0;
                sb.setMax(totalDuration);
                while(currentpostion < totalDuration){
                    try{
                        sleep(500);
                        currentpostion = mp.getCurrentPosition();
                        sb.setProgress(currentpostion);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if(mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mysongs = (ArrayList)b.getParcelableArrayList("songlist");
        position = b.getInt("pos" ,0);
        name = b.getString("song_name");
        txtname.setText(name);

        u= Uri.parse(mysongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setMax(mp.getDuration());
        updateseekbar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id){
            case R.id.playtbn:
                if(mp.isPlaying()){
                    playbtn.setText("play");
                    mp.pause();
                }
                else {
                    playbtn.setText("stop");
                    mp.start();

                }
            case R.id.nextfast:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.prefast:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.nextbtn:
                mp.stop();
                mp.release();
                int val=position++;
                u = Uri.parse(mysongs.get(val).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                playbtn.setText("stop");

                txtname.setText(mysongs.get(val).getName().toString().replace(".mp3", "").replace("wav", ""));
                break;
            case R.id.prebtn:

                mp.stop();
                mp.release();
                // position= (position-1<0)?mysongs.size()-1:position-1;
                if(position-1<0){
                    position = mysongs.size()-1;
                }
                else{
                    position=position-1;
                }
                u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                playbtn.setText("stop");
                txtname.setText(mysongs.get(position).getName().toString().replace(".mp3", "").replace("wav", ""));
                break;
        }

    }
}

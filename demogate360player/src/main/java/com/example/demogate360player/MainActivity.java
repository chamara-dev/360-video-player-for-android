package com.example.demogate360player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gate360player.SphericalPlayerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPlay = (Button) this.findViewById(R.id.button_play);
        btnPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button_play){
            this.openVideoPlayer();
        }
    }

    public void openVideoPlayer(){

        Intent intent = new Intent(this, SphericalPlayerActivity.class);
        String staticVideoPath =
                "android.resource://com.example.demogate360player/raw/" + R.raw.scrum;
        intent.putExtra("proxy", staticVideoPath);
        startActivity(intent);
    }
}

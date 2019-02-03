package com.example.kecseti.avoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kecseti.avoid.game.GameControl;
import com.example.kecseti.avoid.game.GameRunnable;
import com.example.kecseti.avoid.game.GameView;
import com.example.kecseti.avoid.game.MenuItemContainer;


//TODO
//Karakter coustom menü
//Csillag alapu fizetés
//RGB color scwith a karakteren és a keretén is
//Méret változtatása, növelé, csökkentés


public class MainActivity extends AppCompatActivity {

    MediaPlayer hatter;
    boolean isNoisy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        SharedPreferences mentes;
        mentes=this.getSharedPreferences("avoid",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=mentes.edit();
        ImageView textView=findViewById(R.id.cim);
        final ImageButton soundON=findViewById(R.id.soundON);
        final ImageButton soundOFF=findViewById(R.id.soundOFF);
        final ImageButton effectsON=findViewById(R.id.effectON);
        final ImageButton effectsOFF=findViewById(R.id.effectOFF);
        final ImageButton characterCoustom=findViewById(R.id.characterCoustom);
        ImageView startocollect=findViewById(R.id.startcolect);
        TextView starDisplay=findViewById(R.id.stars);


        characterCoustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openCharacterCoustom();
            }
        });

        MenuItemContainer menuItemContainer=new MenuItemContainer(this,textView,soundON,soundOFF,effectsON,effectsOFF,characterCoustom,startocollect);
        menuItemContainer.show();
        final GameView gameView=findViewById(R.id.gameView);
        gameView.inicilazieTextView(starDisplay);
        gameView.inicializeImageView(startocollect);
        starDisplay.setText(" "+mentes.getInt("star",0));
        int delay=15;

        GameRunnable gameRunnable=new GameRunnable(gameView,delay);
        gameView.setOnTouchListener(new GameControl(gameView,menuItemContainer));
        gameRunnable.fuss();

        boolean isEffecty=mentes.getBoolean("effect",true);
        isNoisy=mentes.getBoolean("sound",true);
        hatter= MediaPlayer.create(this,R.raw.hatter);
        hatter.setLooping(true);
        if(isNoisy) {
            soundOFF.setVisibility(View.INVISIBLE);
            soundON.setVisibility(View.VISIBLE);
            hatter.start();
        }else{
            soundOFF.setVisibility(View.VISIBLE);
            soundON.setVisibility(View.INVISIBLE);
        }

        if(isEffecty) {
            effectsOFF.setVisibility(View.INVISIBLE);
            effectsON.setVisibility(View.VISIBLE);
        }else{
            effectsON.setVisibility(View.INVISIBLE);
            effectsOFF.setVisibility(View.VISIBLE);
        }
        gameView.setNoisy(isEffecty);

        soundON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatter.pause();
                isNoisy=false;
                editor.remove("sound");
                editor.putBoolean("sound",isNoisy);
                editor.commit();
                soundON.setVisibility(View.INVISIBLE);
                soundOFF.setVisibility(View.VISIBLE);
            }
        });
        soundOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatter.start();
                hatter.setLooping(true);
                isNoisy=true;
                editor.remove("sound");
                editor.putBoolean("sound",isNoisy);
                editor.commit();
                soundOFF.setVisibility(View.INVISIBLE);
                soundON.setVisibility(View.VISIBLE);
            }
        });

       effectsOFF.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               gameView.setNoisy(true);
               editor.remove("effect");
               editor.putBoolean("effect",true);
               editor.commit();
               effectsOFF.setVisibility(View.INVISIBLE);
               effectsON.setVisibility(View.VISIBLE);
           }
       });

       effectsON.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               gameView.setNoisy(false);
               editor.remove("effect");
               editor.putBoolean("effect",false);
               editor.commit();
               effectsON.setVisibility(View.INVISIBLE);
               effectsOFF.setVisibility(View.VISIBLE);
           }
       });
    }

    private void openCharacterCoustom() {
        Intent intent=new Intent(this,caracterCoustom.class);
        startActivity(intent);
    }

    protected void onPause() {
        super.onPause();
        hatter.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(isNoisy) {
            hatter.start();
            hatter.isLooping();
        }
    }

}

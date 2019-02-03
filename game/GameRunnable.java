package com.example.kecseti.avoid.game;

import android.os.Handler;

public class GameRunnable  {
    Handler kezelo;
    int delay;
    GameView gameView;

    public GameRunnable (GameView gameView,int delay){
        this.delay=delay;
        this.gameView=gameView;
        kezelo=new Handler();
    }

    public void fuss() {
        kezelo.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameView.update();
                kezelo.postDelayed(this,delay);
            }
        },delay);
    }
}

package com.example.kecseti.avoid.game;

import android.os.Handler;

public class CoustomRunnable {

    Handler kezelo;
    CoustomView coustomView;

    public CoustomRunnable(CoustomView c){
        this.coustomView=c;
        kezelo=new Handler();

    }

    public void fuss() {
        kezelo.postDelayed(new Runnable() {
            @Override
            public void run() {
                coustomView.update();
                kezelo.postDelayed(this,40);
            }
        },40);
    }

}

package com.example.kecseti.avoid.game;

import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameControl implements View.OnTouchListener {

    GameView gameView;
    MenuItemContainer menuItemContainer;

    public GameControl(GameView gameView,MenuItemContainer m){
        this.gameView=gameView;
        this.menuItemContainer=m;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int hibaHatar=200;
        if(event.getAction()==MotionEvent.ACTION_MOVE &&
                Math.abs(event.getX()-gameView.getPlayerX())<hibaHatar &&
                Math.abs(event.getY()-gameView.getPlayerY())<hibaHatar){
            gameView.setRun(true);
            gameView.updateDir((int)event.getX(),(int)event.getY());
            menuItemContainer.hide();
            if(gameView.didLost()){
                if(Math.sqrt((event.getX()-gameView.getWidth()/2)*(event.getX()-gameView.getWidth()/2)+(event.getY()-gameView.getHeight()/2)*(event.getY()-gameView.getHeight()/2))<hibaHatar) {
                    gameView.kezd((int)event.getX(),(int)event.getY()+250);
                }
                else {
                    gameView.kezd(gameView.getWidth() / 2, gameView.getHeight() / 2);
                }
            }
        }
        else{
            gameView.setRun(false);
            menuItemContainer.show();
            gameView.setLost(false);
        }
        return true;
    }
}

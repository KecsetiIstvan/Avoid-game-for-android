package com.example.kecseti.avoid.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MenuItemContainer {

    ImageView textView;
    ImageButton soundon;
    ImageButton soundoff;
    ImageButton effecton;
    ImageButton effectoff;
    ImageButton character;
    ImageView starToCollect;
    Context context;

    public MenuItemContainer(Context c,ImageView textView,ImageButton so,ImageButton sf,ImageButton eo,ImageButton ef, ImageButton ch,ImageView i){
        this.textView=textView;
        this.soundon=so;
        this.soundoff=sf;
        this.effectoff=ef;
        this.effecton=eo;
        this.character=ch;
        this.context=c;
        this.starToCollect=i;
    }

    public void hide(){
        textView.setVisibility(View.INVISIBLE);
        soundon.setVisibility(View.INVISIBLE);
        soundoff.setVisibility(View.INVISIBLE);
        effectoff.setVisibility(View.INVISIBLE);
        effecton.setVisibility(View.INVISIBLE);
        character.setVisibility(View.INVISIBLE);
        starToCollect.setVisibility(View.VISIBLE);
    }
    public void show(){
        SharedPreferences mentes;
        mentes=context.getSharedPreferences("avoid",Context.MODE_PRIVATE);
        textView.setVisibility(View.VISIBLE);
        if(mentes.getBoolean("sound",true))
            soundon.setVisibility(View.VISIBLE);
        else
            soundoff.setVisibility(View.VISIBLE);
        if(!mentes.getBoolean("effect",true))
            effectoff.setVisibility(View.VISIBLE);
        else
            effecton.setVisibility(View.VISIBLE);
        character.setVisibility(View.VISIBLE);
        starToCollect.setVisibility(View.INVISIBLE);
    }

}

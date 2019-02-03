package com.example.kecseti.avoid;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.example.kecseti.avoid.game.CoustomRunnable;
import com.example.kecseti.avoid.game.CoustomView;

public class caracterCoustom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caracter_coustom);
        final SeekBar primary=findViewById(R.id.primary);
        SeekBar secondary=findViewById(R.id.secondary);

        LinearGradient test=new LinearGradient(0.f,0.f,1000,0.f,new int[]{0xFF000000,0xFF0000FF,0xFF00FF00,0xFF00FFFF,0xFFFF0000,0xFFFF00FF,0xFFFFFFFF},null,Shader.TileMode.CLAMP);
        ShapeDrawable shape=new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);
        primary.setProgressDrawable(shape);
        secondary.setProgressDrawable(shape);

        final CoustomView coustomView=findViewById(R.id.coustomView);
        CoustomRunnable gameRunnable=new CoustomRunnable(coustomView);
        gameRunnable.fuss();

        primary.setMax(256*7-1);
        primary.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    if(progress < 256){
                        b = progress;
                    } else if(progress < 256*2) {
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*3) {
                        g = 255;
                        b = progress%256;
                    } else if(progress < 256*4) {
                        r = progress%256;
                        g = 256 - progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*5) {
                        r = 255;
                        g = 0;
                        b = progress%256;
                    } else if(progress < 256*6) {
                        r = 255;
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if(progress < 256*7) {
                        r = 255;
                        g = 255;
                        b = progress%256;
                    }
                    coustomView.setCubeColor(r,g,b);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}

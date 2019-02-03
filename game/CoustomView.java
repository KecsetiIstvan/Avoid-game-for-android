package com.example.kecseti.avoid.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class CoustomView extends View {

    int r,g,b;
    int rv,gv,bv;
    Random rand=new Random();
    int szog=5;
    Paint p,keret;
    int br,bg,bb;
    int cr,cg,cb;

    public CoustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        r=rand.nextInt(240);
        g=rand.nextInt(240);
        b=rand.nextInt(240);
        rv=-1;
        gv=-1;
        bv=1;
        p=new Paint();
        keret=new Paint();

    }

    public void update(){
        if(r<=5 || r>=250)rv*=-1;
        if(g<=5 || g>=250)gv*=-1;
        if(b<=5 || b>=250)bv*=-1;
        r+=rv;
        g+=gv;
        b+=bv;
        szog+=4;
        this.invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(r, g,b));

        keret.setStyle(Paint.Style.STROKE);
        keret.setStrokeWidth(30);
        keret.setColor(Color.rgb(br,bg,bb));

        p.setColor(Color.rgb(cr,cg,cb));
        p.setStyle(Paint.Style.FILL);

        canvas.save();
        canvas.rotate(szog,this.getWidth()/2,300);
        canvas.drawRect(this.getWidth()/2-100,200,this.getWidth()/2+100,400,keret);
        canvas.drawRect(this.getWidth()/2-100,200,this.getWidth()/2+100,400,p);
        canvas.restore();

    }

    public void setCubeColor(int r,int g,int b){this.cr=r;this.cg=g;this.cb=b;}
    public void setBorderColor(int r,int g,int b){this.br=r;this.bg=g;this.bb=b;}

}

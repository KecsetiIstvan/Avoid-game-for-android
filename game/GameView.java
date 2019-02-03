package com.example.kecseti.avoid.game;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kecseti.avoid.R;
import com.example.kecseti.avoid.tiles.BasicTile;

import java.util.Random;

public class GameView extends View {

    float r,g,b,yv,playerSzog,enemySzog,xLep,yLep,starX,starY;
    int elozoY,enemyCount,playerSize,timer,points, collision,er,eg,eb,ekr,ekg,ekb,star;
    boolean lost,run,isNoisy;
    BasicTile player;
    BasicTile[] enemy;
    Random rand;
    Paint p,keret;
    Handler kezelo;
    ImageView starToCollect;
    MediaPlayer death;
    SoundPool enemyCollision;
    AudioAttributes audioAttributes;
    SharedPreferences mentes;
    TextView starDisplay;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rand=new Random();
        p=new Paint();
        keret=new Paint();
        timer=0;
        points=0;
        enemyCount=1;
        playerSize=120;
        player=new BasicTile(playerSize,playerSize);
        enemy=new BasicTile[1000];
        for(int i=0;i<enemyCount;i++) {
            int nagysag = rand.nextInt(playerSize*2) + 40;
            enemy[i] = new BasicTile(nagysag, nagysag);
        }
        lost=false;
        kezd(500,600);
        kezelo=new Handler();
        death=MediaPlayer.create(this.getContext(),R.raw.death);
        isNoisy=true;
        mentes=this.getContext().getSharedPreferences("avoid",Context.MODE_PRIVATE);
        star=mentes.getInt("star",0);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            audioAttributes=new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            enemyCollision=new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(10)
                    .build();
        }
        else{
            enemyCollision=new SoundPool(10,AudioManager.STREAM_MUSIC,0);
        }
        collision=enemyCollision.load(this.getContext(),R.raw.enemy,1);
    }

    public void kezd(int playerX,int playerY) {
        //Random startoló háttérszín
        r = 226;
        g = 73;
        b = 2;

        er=191;
        eg=93;
        eb=47;

        ekr=2;
        ekg=114;
        ekb=13;

        start(playerX,playerY);
    }

    public void start(int playerX,int playerY){
        yv=1f;
        playerSzog=0;
        enemySzog=0;
        elozoY=playerY;
        player.setLocation(playerX,playerY);
        enemyCount=0;
        for(int i=0;i<enemyCount;i++) {
            enemy[i].setLocation(rand.nextInt(1000), rand.nextInt(1500));
        }
        run=false;
        timer=0;
        points=0;
    }

    private boolean collisionPlayerEnemy(){
        int error=0;
        int playerX=player.getX()+player.getWidth()/2;
        int playerY=player.getY()+player.getHeight()/2;

        for(int i=0;i<enemyCount;i++) {
            int enemyX = enemy[i].getX() + enemy[i].getWidth() / 2;
            int enemyY = enemy[i].getY() + enemy[i].getHeight() / 2;

            if (Math.sqrt((playerX - enemyX) * (playerX - enemyX) + (playerY - enemyY) * (playerY - enemyY)) < player.getWidth() / 2 + enemy[i].getWidth() / 2 - error)
                return true;
        }
        return false;
    }

    private boolean collisionEnemyEnemy(){
        int error=0;
        for(int i=0;i<enemyCount;i++) {
            int enemy1X = enemy[i].getX() + enemy[i].getWidth() / 2;
            int enemy1Y = enemy[i].getY() + enemy[i].getHeight() / 2;
            for(int j=i+1;j<enemyCount;j++) {
                int enemy2X=enemy[j].getX()+enemy[j].getWidth()/2;
                int enemy2Y=enemy[j].getY()+enemy[j].getHeight()/2;
                if (Math.sqrt((enemy1X - enemy2X) * (enemy1X - enemy2X) + (enemy1Y - enemy2Y) * (enemy1Y - enemy2Y)) < enemy[j].getWidth() / 2 + enemy[i].getWidth() / 2 - error)
                    return true;
            }
        }
        return false;
    }

    private void collidedEnemyReset(){
        int error=0;
        for(int i=0;i<enemyCount;i++) {
            int enemy1X = enemy[i].getX() + enemy[i].getWidth() / 2;
            int enemy1Y = enemy[i].getY() + enemy[i].getHeight() / 2;
            for(int j=i+1;j<enemyCount;j++) {
                int enemy2X=enemy[j].getX()+enemy[j].getWidth()/2;
                int enemy2Y=enemy[j].getY()+enemy[j].getHeight()/2;
                if (Math.sqrt((enemy1X - enemy2X) * (enemy1X - enemy2X) + (enemy1Y - enemy2Y) * (enemy1Y - enemy2Y)) < enemy[j].getWidth() / 2 + enemy[i].getWidth() / 2 - error){
                    int nagysag = rand.nextInt(playerSize*2) + 40;
                    enemy[i]=new BasicTile(nagysag,nagysag);
                    if(rand.nextInt(100)<50) enemy[i].setLocation(-enemy[i].getHeight(),rand.nextInt(this.getHeight()));
                    else                             enemy[i].setLocation(this.getWidth(),rand.nextInt(this.getHeight()));
                }
            }
        }
    }

    private void enemyFollow(){
        for(int i=0;i<enemyCount;i++) {
            xLep = (enemy[i].getX() - player.getX()) / 60;
            yLep = (enemy[i].getY() - player.getY()) / 60;
            enemy[i].setLocation((float) enemy[i].getX() - xLep, (float) enemy[i].getY() - yLep);
        }
    }

    private void enemySpawn(){
        timer++;
        if(timer>=67){
            timer=0;
            int nagysag = rand.nextInt(playerSize*2) + 40;
            enemy[enemyCount]=new BasicTile(nagysag,nagysag);
            if(rand.nextInt(100)<50) enemy[enemyCount].setLocation(-enemy[enemyCount].getHeight(),rand.nextInt(this.getHeight()));
            else                            enemy[enemyCount].setLocation(this.getWidth(),rand.nextInt(this.getHeight()));
            enemyCount++;
        }
    }

    private boolean collectStar() {
        if(Math.sqrt(((player.getX()+player.getWidth()/2)-(starToCollect.getX()+starToCollect.getWidth()/2))*((player.getX()+player.getWidth()/2)-(starToCollect.getX()+starToCollect.getWidth()/2))+
                ((player.getY()+player.getHeight()/2)-(starToCollect.getY()+starToCollect.getHeight()/2))*((player.getY()+player.getHeight()/2)-(starToCollect.getY()+starToCollect.getHeight()/2)))<player.getWidth()/2+starToCollect.getWidth()/2)
                return true;
        return false;
    }
    private void starSpawn(int X,int Y){
        starX=rand.nextInt(X-80)+ 40f;
        starY=rand.nextInt(Y-80)+40f;
        starToCollect.setX(starX);
        starToCollect.setY(starY);
    }

    public void update(){
        if(!run){
            if(lost) {
                float ujy;
                ujy = player.getY() + yv;
                if (ujy > elozoY + 300 || ujy < elozoY - 300) yv *= -1;
                player.setLocation(player.getX(), (int) ujy);
            }
            playerSzog+=3;
            enemySzog+=5;
        }

        else{
            playerSzog+=20;
            enemySzog+=15;
            elozoY=player.getY();

            enemyFollow();
            enemySpawn();

            if(collectStar()){
                starSpawn(this.getWidth(),this.getHeight());
                star++;
                SharedPreferences.Editor editor=mentes.edit();
                editor.remove("star") ;
                editor.putInt("star",star);
                editor.commit();
                starDisplay.setText(" "+star);
            }

            if(collisionPlayerEnemy()){
                lost=true;
                if(isNoisy) death.start();
                if(points>mentes.getInt("rekord",0)){
                    SharedPreferences.Editor editor=mentes.edit();
                    editor.remove("rekord") ;
                    editor.putInt("rekord",points);
                    editor.commit();
                    final Toast toast = Toast.makeText(this.getContext(), "New high score: "+points, Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
            if(collisionEnemyEnemy()){
                points++;
                timer=0;
                collidedEnemyReset();
                if(isNoisy){
                    enemyCollision.play(collision,1.0f,1.0f,1,0,1.0f);
                }
            }

            if(points >=30 && points <60){
                if(r<=253)r++;//r=255
                if(g<=253)g++;//g=255;
                if(b>=1)  b--;//b=0;

                if(ekr<=247)ekr++;//ekr=247;
                if(ekg<=235)ekg++;//ekg=235;
                if(ekb<=170)ekb++;//ekb=170;

                if(er<=244)er++;//er=244;
                if(eg<=104)eg++;//eg=104;
                if(eb<=66) eb++;//eb=66;
            }

            if(points >=60 && points <90){

                if(r>=1)  r--;//r=0;
                if(g>=200)g--;//g=200;
                if(b<=20) b++;//b=20;

                if(ekr>=66) ekr--;//ekr=66;
                if(ekg>=244)ekg--;//ekg=244;
                if(ekb<=217)ekb++;//ekb=217;

                if(er>=66) er--;//er=66;
                if(eg>=194)eg--;//eg=194;
                if(eb<=244)eb++;//eb=244;

            }
            if(points >=90 && points <120){

                r=0;
                g=200;
                if(b<=250)b++; //b=250;

                if(ekr<=226)ekr++;//ekr=226;
                if(ekg>=179)ekg--;//ekg=179;
                if(ekb<=233)ekb++;//ekb=223;

                if(er<=210)er++;//er=211;
                if(eg>=105)eg--;//eg=105;
                if(eb>=142)eb--;//eb=142;

            }

            if(points >=120 && points <150){

                r=0;
                if(g>=1)g--;//g=0;
                b=250;

                if(er>=127)er--;//er=127;
                if(eg>=20) eg--;//eg=20;
                if(eb>=56) eb--;//eb=56;

                if(ekr>=198)ekr--;//ekr=198;
                if(ekg>=87) ekg--;//ekg=87;
                if(ekb>=148)ekb--;//ekb=148;

            }
            if(points >=150){

                r=0;
                g=0;
                if(b>=1)b--;//b=0;

                if(ekr<=250)ekr++;//ekr=250;
                if(ekg>=20) ekg--;//ekg=20;
                if(ekb>=20) ekb--;//ekb=20;

                if(er>=20)er--;//er=20;
                if(eg>=20)eg--;//eg=20;
                if(eb>=20)eb--;//eb=20;

            }


        }

        this.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.rgb((int)r, (int)g, (int)b));

            if(points != 0 ) {
                keret.setTextSize(300);
                keret.setStrokeWidth(5f);
                keret.setColor(Color.argb(0.2f, 0, 0, 0));
                if(points>=150)
                    keret.setColor(Color.argb(0.2f,1,0,0));
                String text="000";
                if (points <= 9)               text = "00" + points;
                if(points >=10 && points <=99) text="0"+points;
                if(points>99)                  text = "" + points;
                canvas.drawText(text, this.getWidth() / 2 - 300, this.getHeight() / 2, keret);
                if(run){
                    p.setTextSize(50);
                    p.setColor(Color.argb(0.7f,255,255,255));
                    p.setStrokeWidth(3f);
                    if(points<mentes.getInt("rekord",0))
                        canvas.drawText("High score: "+mentes.getInt("rekord",0),0,this.getHeight()-5,p);
                    else
                        canvas.drawText("High score: "+points,0,this.getHeight()-5,p);
                }
            }else{
                if(run){
                    keret.setTextSize(300);
                    keret.setStrokeWidth(5f);
                    keret.setColor(Color.argb(0.2f, 0, 0, 0));
                    if(points>=150)
                        keret.setColor(Color.argb(0.2f,1,0,0));
                    String text="000";
                    if (points <= 9)               text = "00" + points;
                    if(points >=10 && points <=99) text="0"+points;
                    if(points>99)                  text = "" + points;
                    canvas.drawText(text, this.getWidth() / 2 - 300, this.getHeight() / 2, keret);
                    p.setTextSize(50);
                    p.setColor(Color.argb(0.7f,255,255,255));
                    p.setStrokeWidth(3f);
                    if(points<mentes.getInt("rekord",0))
                        canvas.drawText("High score: "+mentes.getInt("rekord",0),0,this.getHeight()-5,p);
                    else
                        canvas.drawText("High score: "+points,0,this.getHeight()-5,p);
                }
            }

            p.setColor(Color.WHITE);
            p.setStyle(Paint.Style.FILL);

            keret.setStyle(Paint.Style.STROKE);
            keret.setStrokeWidth(30);
            keret.setColor(Color.BLACK);
            canvas.save();
            canvas.rotate(playerSzog,player.getX()+player.getWidth()/2,player.getY()+player.getHeight()/2);
            canvas.drawRect(player.getX(),player.getY(),player.getX()+player.getWidth(),player.getY()+player.getHeight(),keret);
            canvas.drawRect(player.getX(),player.getY(),player.getX()+player.getWidth(),player.getY()+player.getHeight(),p);
            canvas.restore();

            p.setColor(Color.rgb(er,eg,eb));
            keret.setColor(Color.rgb(ekr,ekg,ekb));
            keret.setStrokeWidth(20);
            for(int i=0;i<enemyCount;i++) {
                canvas.save();
                canvas.rotate(enemySzog, enemy[i].getX() + enemy[i].getWidth() / 2, enemy[i].getY() + enemy[i].getHeight() / 2);
                canvas.drawRect(enemy[i].getX(), enemy[i].getY(), enemy[i].getX() + enemy[i].getWidth(), enemy[i].getY() + enemy[i].getHeight(), keret);
                canvas.drawRect(enemy[i].getX(), enemy[i].getY(), enemy[i].getX() + enemy[i].getWidth(), enemy[i].getY() + enemy[i].getHeight(), p);
                canvas.restore();
            }
            if(points>150){
                starDisplay.setTextColor(Color.RED);
            }

    }

    public void updateDir(int x, int y){
        player.setLocation(x-player.getWidth()/2,y-player.getHeight()/2);
    }

    public int getPlayerX(){return player.getX();}
    public int getPlayerY(){return player.getY();}
    public void setRun(Boolean run){this.run=run;}
    public boolean didLost(){return this.lost;}
    public void setLost(boolean b){this.lost=b;}
    public void setNoisy(boolean b){this.isNoisy=b;}
    public void inicilazieTextView(TextView t){this.starDisplay=t;}
    public void inicializeImageView(ImageView i){this.starToCollect=i;starSpawn(600,1200);}

}



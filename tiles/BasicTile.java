package com.example.kecseti.avoid.tiles;

public class BasicTile {

    int x,y,width,height;

    public BasicTile(int width, int height) {
        this.width =  width;
        this.height = height;
    }
    public void setLocation(float x, float y){
        this.x=(int)x;
        this.y=(int)y;
    }
    public int  getWidth()            { return width;        }
    public int  getHeight()           { return height;       }
    public int  getX()                { return x;            }
    public int  getY()                { return y;            }
    public void setHeight(int height) { this.height = height;}
    public void setWidth(int width)   { this.width = width;  }

}

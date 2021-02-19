package com.giuseppe.igpe.arkanoid.Object;

import com.giuseppe.igpe.arkanoid.Abstract.GameObject;
import com.giuseppe.igpe.arkanoid.Config.Config;

import java.awt.*;

public class Brick extends GameObject {


    private boolean shot;
    private boolean haveCapsule;
    private boolean destroyable;
    private int remainShot;
    private int score;
    private int width = Config.BRICK_WIDTH , height = Config.BRICK_HEIGTH;


    public Brick(double x, double y, int width, int height, String color, int score, boolean haveCapsule,boolean destroyable,int remainShot) {

        super(x,y,width,height,color+"Wall.png");
        this.score = score;
        this.haveCapsule = haveCapsule;
        this.destroyable = destroyable;
        this.remainShot = remainShot;

    }

    public boolean isHaveCapsule() {
        return haveCapsule;
    }

    public boolean isShot() {
        return shot;
    }

    public double getX() {
        return x;
    }

    public void setShot(boolean inGame) {
        this.shot = inGame;
    }



    @Override
    public void render(Graphics g) {
        if(!shot) {
            g.drawImage(image,(int) x,(int) y,null);
        }
    }

    public int getRemainShot() {
        return remainShot;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int getHeight() { return height; }

    public boolean isDestroyable() {
        return destroyable;
    }

    public void shot() {
        if(--remainShot == 0 && destroyable)
            shot = true;
    }

}

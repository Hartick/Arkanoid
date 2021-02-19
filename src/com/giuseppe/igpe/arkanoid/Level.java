package com.giuseppe.igpe.arkanoid;

import com.giuseppe.igpe.arkanoid.Config.Config;
import com.giuseppe.igpe.arkanoid.Object.Brick;
import com.giuseppe.igpe.arkanoid.Object.Capsule;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Level {
    private int id;
    private BufferedImage background;
    private ArrayList<Brick> bricks;
    private ArrayList<Capsule> capsules;


    public Level(int id) {
        this.id = id;
        bricks = new ArrayList<Brick>();
    }
    

    public void init(int [][] brick) {
        double x;
        double y = 30 + Config.ALTEZZA_SCOREBAR;
        int capsuleAfter = 0;
        for(int i = 0; i<28; i++) {
            x = 30;
            for(int j = 0; j<11; j++) {
                if(capsuleAfter ==0) {
                    capsuleAfter = Config.CAPSULE_AFTER;
                }
                if(brick[i][j] > 1 && brick[i][j] < 9) {
                    bricks.add(new Brick(x,y,Config.BRICK_WIDTH,Config.BRICK_HEIGTH,Config.COLOR_MAP[brick[i][j] - 1],Config.BRICK_SCORE[brick[i][j] -1],--capsuleAfter == 0,true,1));
                }
                if(brick[i][j] == 9) {
                    bricks.add(new Brick(x,y,Config.BRICK_WIDTH,Config.BRICK_HEIGTH,Config.COLOR_MAP[8],Config.BRICK_SCORE[8],--capsuleAfter == 0,true,2));
                }
                if(brick[i][j] == 10) {
                    bricks.add(new Brick(x,y,Config.BRICK_WIDTH,Config.BRICK_HEIGTH,Config.COLOR_MAP[9],Config.BRICK_SCORE[9],false,false,0));
                }
                x+= Config.BRICK_WIDTH;
            }
            y += Config.BRICK_HEIGTH;
        }
    }

    public ArrayList<Brick> getAllBricks() {
        return bricks;
    }

    public void render(Graphics g) {
        for (Brick brick:bricks) {
            brick.render(g);
        }
    }

    public int getRemainBrick() {
        int number = 0;
        for(Brick brick:bricks) {
            if(!brick.isShot() && brick.isDestroyable())
                number++;
        }
        return number;
    }

    public BufferedImage getLevelBgImage() {
        return background;
    }

    public void setLevelBgImage(String bg) {
        try {
            background = ImageIO.read(new File("res/level_bg_"+bg+".png"));
        } catch (IOException ex) {ex.getCause();}
    }

}

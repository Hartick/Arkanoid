package com.giuseppe.igpe.arkanoid.Object;

import com.giuseppe.igpe.arkanoid.Abstract.GameObject;
import com.giuseppe.igpe.arkanoid.Config.Config;
import com.giuseppe.igpe.arkanoid.Game;

import java.awt.event.KeyListener;


public class Player extends GameObject {
    private double speedX;
    private static Player player_instance = null;
    private int lives;
    private int score = 0;
    private boolean magnet = false;
    private boolean laser = false;
    private boolean needChangeLevel = false;
    private double magnetBallDifferencePaddle = 0;
    private Player() {
        super(Config.LARGHEZZA / 2.0 - 50, Config.LUNGHEZZA - 100 + Config.ALTEZZA_SCOREBAR,Config.INIT_PLAYER_WIDTH,Config.PLAYER_HEIGTH,"player_small.png");
        this.speedX = 0;
        lives = Config.PLAYER_LIFE;
    }

    public static Player getPlayer() {
        if(player_instance == null) {
            player_instance = new Player();
        }
        return player_instance;
    }

    public void move() {
        if ((speedX < 0 && x + (width / 2) - 30 > width / 2) || (speedX > 0 && x + width < Config.LARGHEZZA - 27))
            x += speedX;
        if(x + width > Config.LARGHEZZA - 27) {
            x = Config.LARGHEZZA - width - 27;
        }
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }
    private void die() {
        lives--;
    }
    private void lifeUp() { lives++; }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCapsuleEffect(int type) {
        switch (type) {
            case Capsule.CATCH:
                restorePlayerConfig();
                magnet = true;
                break;
            case Capsule.ENLARGE:
                restorePlayerConfig();
                width = Config.LARGE_PLAYER_WIDTH;
                Game.playSound("enlarge_capsule.wav",true);
                setImage("player_large.png");
                break;
            case Capsule.LASER:
                restorePlayerConfig();
                setImage("shot_vaus.png");
                laser = true;
                break;
            case Capsule.SLOW:
                Ball.getBall().setVelocity(Config.INIT_BALL_VELOCITY);
                break;
            case Capsule.WARP:
                needChangeLevel = true;
                break;
            case Capsule.PLAYER:
                lifeUp();
                Game.playSound("player_up.wav",true);
                break;

        }
    }

    private void restorePlayerConfig() {
        magnet = false;
        width = Config.INIT_PLAYER_WIDTH;
        setImage("player_small.png");
        laser = false;
    }

    private void restorePlayerInit() {
        restorePlayerConfig();
        setX(Config.LARGHEZZA / 2.0 - 50);
        //setY(Config.LUNGHEZZA - 100 - Config.ALTEZZA_SCOREBAR);
    }

    public void destroy(boolean lose) {
        if(lose) {
            die();
        }
        restorePlayerInit();
    }

    public boolean isNeedChangeLevel() {
        return needChangeLevel;
    }

    public void setNeedChangeLevelFalse() {
        needChangeLevel = false;
    }

    public boolean haveMagnet() {
        return magnet;
    }

    public boolean haveLaser() {
        return laser;
    }

    public void initPlayer() {
        restorePlayerInit();
        score = 0;
        lives = Config.PLAYER_LIFE;
    }

    public double getMagnetBallDifferencePaddle() {
        return magnetBallDifferencePaddle;
    }

    public void setBallMagnetDistance() {
        magnetBallDifferencePaddle = Ball.getBall().getX() - x;
    }
}

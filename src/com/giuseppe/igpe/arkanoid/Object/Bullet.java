package com.giuseppe.igpe.arkanoid.Object;

import com.giuseppe.igpe.arkanoid.Abstract.GameObject;
import com.giuseppe.igpe.arkanoid.Config.Config;

public class Bullet extends GameObject {
    private int velocity = Config.BULLET_VELOCITY;

    public Bullet(double x, double y) {
        super(x,y,5,21,"bullet.png");
    }

    public boolean move() {
        return (y-= velocity) < Config.LUNGHEZZA - 30;
    }
}


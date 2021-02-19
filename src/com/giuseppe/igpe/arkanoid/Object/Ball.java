package com.giuseppe.igpe.arkanoid.Object;

import com.giuseppe.igpe.arkanoid.Abstract.GameObject;
import com.giuseppe.igpe.arkanoid.Config.Config;
import com.giuseppe.igpe.arkanoid.Game;
import javafx.scene.shape.Circle;

import java.awt.*;

public class Ball extends GameObject {
    private double velocity,dx,dy;
    private static Ball ball_instance = null;

    private Ball() {
        super(setInitBallX(),Player.getPlayer().getY()-24,Config.BALL_RADIUS,Config.BALL_RADIUS,"EnergyBall.png");
        this.velocity = Config.INIT_BALL_VELOCITY;
        this.dx = 0.1;
        this.dy = -Config.INIT_BALL_VELOCITY;
    }

    public static Ball getBall() {
        if(ball_instance == null) {
            ball_instance = new Ball();
        }
        return ball_instance;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image,(int) x, (int) y, null);
    }



    public void bouncePlayer(GameObject o) {
        double ballCenterX = x + getWidth() / 2.0;
        double hittableWidth = o.getWidth();
        double hittableCenterX = o.getX() + hittableWidth/2;
        // Calculate the position of the ball relative to the center of
        // the object, and express this as a number between -1 and +1.
        double posX = (ballCenterX - hittableCenterX) / (hittableWidth/2);
        final double influenceX = 0.75;
        dx = velocity * posX * influenceX;
        dy = Math.sqrt(velocity*velocity - dx*dx) *
                (dy > 0? -1 : 1);
    }

    public void move() {
        if(new Rectangle(Player.getPlayer().getRect()).intersects(getRect())){
            bouncePlayer(Player.getPlayer());
            Game.playSound("hit_vaus.wav",true);
            if(Player.getPlayer().haveMagnet()) {
                Game.game.setIsAlive(false);
                Player.getPlayer().setBallMagnetDistance();
            }
        }
        x += dx;
        y += dy;
        if(x < 28 || x > Config.LARGHEZZA - 27 - Config.BALL_RADIUS / 2)
            dx = -dx;
        if(y < 30 + Config.ALTEZZA_SCOREBAR)
            dy = -dy;
    }

    public boolean lose() {
        return y > Player.getPlayer().getY();
    }

    public void destroy() {
        ball_instance = null;
    }

    private static double setInitBallX() {
        return Player.getPlayer().getX() + Player.getPlayer().getWidth() / 2.0 - Config.BALL_RADIUS / 2.0;
    }

}

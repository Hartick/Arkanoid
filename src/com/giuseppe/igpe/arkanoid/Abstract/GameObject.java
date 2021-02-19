package com.giuseppe.igpe.arkanoid.Abstract;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class GameObject{
    protected double x,y;
    protected int height,width;
    protected Image image;


    public GameObject(double x, double y, int width, int height, String name) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        setImage(name);
    }

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public double getX() { return x;}
    public double getY() { return y; }
    public int getWidth() {
        return width;
    }
    public int getHeight() { return height; }
    public void setWidth(int width) { this.width = width; }
    public void render(Graphics g) {
        g.drawImage(image,(int) x,(int) y,null);
    }
    protected void setImage(String name) {
        try {
            URL url = new File("res/"+name).toURI().toURL();
            this.image = new ImageIcon(url).getImage();
        } catch (Exception e) {
            Logger.getLogger(GameObject.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    public Rectangle getRect() {
        return new Rectangle((int)x,(int)y,width,height);
    }

}

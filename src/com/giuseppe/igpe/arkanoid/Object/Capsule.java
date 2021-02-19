package com.giuseppe.igpe.arkanoid.Object;

import com.giuseppe.igpe.arkanoid.Abstract.GameObject;
import com.giuseppe.igpe.arkanoid.Config.Config;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Capsule extends GameObject {

    public final static int CATCH = 1;
    public final static int ENLARGE = 2;
    public final static int LASER = 3;
    public final static int PLAYER = 4;
    public final static int SLOW = 5;
    public final static int WARP = 6;
    int velocity = Config.CAPSULE_VELOCITY;

    private int type;

    public Capsule(double x, double y,String name,int type) {
        super(x,y,10,10,name);
        this.type = type;
    }

    public void render(Graphics g) {
        g.drawImage(image,(int)x,(int)y,null);
    }

    public boolean whenSpawned() {
            return (y+= velocity) > Player.getPlayer().getY() + Player.getPlayer().getHeight() / 2 ;
    }

    public int getType() {
        return type;
    }

    public static String getNameFromValue(int type) {
        Class<Capsule> c = Capsule.class;
        String name="";
        for (Field f : c.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
                try {
                    if((int) f.get(null) == type) {
                        name=f.getName();
                        break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return name;
    }

}

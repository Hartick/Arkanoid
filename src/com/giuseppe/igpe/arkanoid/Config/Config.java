package com.giuseppe.igpe.arkanoid.Config;

public class Config {
    public static final int ALTEZZA_SCOREBAR = 60;
    public static final int LARGHEZZA = 654;
    public static final int LUNGHEZZA = 860;
    public static final int LUNGHEZZA_FINESTRA = LUNGHEZZA + ALTEZZA_SCOREBAR;
    public static final int BRICK_WIDTH = 54;
    public static final int BRICK_HEIGTH = BRICK_WIDTH / 2;
    public static final int BALL_RADIUS = 15;
    public static final int PLAYER_SPEED = 8;
    public static final int INIT_PLAYER_WIDTH = 104;
    public static final int LARGE_PLAYER_WIDTH = 144;
    public static final int PLAYER_HEIGTH = 24;
    public static final int PLAYER_LIFE = 3;
    public static final double INIT_BALL_VELOCITY = 2.5;
    public static final long THREAD_SLEEP_MILLIS = 5;
    public static final int CAPSULE_AFTER = 4;
    public static final int CAPSULE_VELOCITY = 1;
    public static final int BULLET_VELOCITY = 8;
    public static final String NOME_GIOCO = "Arkanoid";
    public static final String [] COLOR_MAP = {"white", "orange", "azure", "green", "red", "blue","pink","yellow", "silver", "gold"};
    public static final int [] BRICK_SCORE = {50,60,70,80,90,100,110,120,50,0};
}

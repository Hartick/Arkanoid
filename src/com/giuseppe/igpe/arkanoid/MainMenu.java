package com.giuseppe.igpe.arkanoid;

import com.giuseppe.igpe.arkanoid.Config.Config;
import com.giuseppe.igpe.arkanoid.Object.Ball;
import com.giuseppe.igpe.arkanoid.Object.Player;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class MainMenu {

    private BufferedImage arkanoid_logo;
    private BufferedImage unical_logo;

    public MainMenu() {
        try {
            arkanoid_logo = ImageIO.read(new File("res/arkanoid-logo.png"));
            unical_logo = ImageIO.read(new File("res/logoUnical.gif"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void render(Graphics g){
        g.drawImage(arkanoid_logo,60,0,null);
        g.drawImage(unical_logo,20 ,Config.LUNGHEZZA_FINESTRA - 250,null);
        g.setFont(new Font("Arial",Font.BOLD,25));
        g.setColor(Color.white);
        if(Game.state == Game.State.MENU) {
            Game.playSound("introSong.wav", false);
            g.drawString("PRESS ENTER FOR NEW GAME", Config.LARGHEZZA / 2 - Config.LARGHEZZA / 3, Config.LUNGHEZZA_FINESTRA / 2 - 130);
        } else if (Game.state== Game.State.PAUSE) {
            g.drawString("PRESS ESC TO PLAY GAME", 180, Config.LUNGHEZZA / 2 - 130);
        } else if (Game.state == Game.State.LOSE) {
            g.drawString("GAME OVER", 250, Config.LUNGHEZZA / 2 - 130);
            Game.playSound("game_over.wav", false);
            TimerTask task = new TimerTask() {
                public void run() {
                    Player.getPlayer().initPlayer();
                    ManageLevels.restart();
                    Ball.getBall().destroy();
                    Game.state = Game.State.MENU;
                    Game.game.repaint();
                }
            };
            Timer timer = new Timer();

            long delay = 3000L;
            timer.schedule(task, delay);
        } else if (Game.state == Game.State.LEVEL) {
            g.drawString("ROUND " + (ManageLevels.getNumberCurrentLevel() + 1), Config.LARGHEZZA / 2 - 75 , Config.LUNGHEZZA_FINESTRA / 2);
            TimerTask task = new TimerTask() {
                public void run() {
                    Game.state = Game.State.GAME;
                    Game.playSound("startLevel.wav", false);
                    if(!Game.t.isAlive()) {
                        Game.t.start();
                    }
                    Game.game.setInGame(true);
                    Game.game.repaint();
                }
            };
            Timer timer = new Timer();

            long delay = 2500L;
            timer.schedule(task, delay);
        } else if(Game.state == Game.State.WIN) {
            g.drawString("YOU WIN", Config.LARGHEZZA / 2 - 75 , Config.LUNGHEZZA_FINESTRA / 2);
        }
        g.drawString("PROGETTO PER IGPE", 220, Config.LUNGHEZZA_FINESTRA - 200);
        g.drawString("UNIVERSITA' DELLA CALABRIA", 220, Config.LUNGHEZZA_FINESTRA - 175);
        g.setFont(new Font("Arial",Font.BOLD,15));
        g.drawString("@Credits Giuseppe Scarfò Matricola 189876", 180, Config.LUNGHEZZA_FINESTRA - 100);
    }
}

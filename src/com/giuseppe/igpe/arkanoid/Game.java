package com.giuseppe.igpe.arkanoid;

import com.giuseppe.igpe.arkanoid.Config.Config;
import com.giuseppe.igpe.arkanoid.Object.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ListIterator;

public class Game extends JPanel implements KeyListener,Runnable {
    public static Thread t;
    private Ball b;
    private Player p;
    private ManageLevels l;
    private BufferedImage image;
    private ArrayList<Capsule> capsules;
    private ArrayList<Bullet> bullets;
    private MainMenu menu;
    private boolean inGame = false;
    private boolean isAlive = false;

    public static Game game = null;
    private static Clip clip;
    public enum State {
       PAUSE,
       MENU,
       GAME,
       LOSE,
       LEVEL,
       WIN
    }
    public static State state = State.MENU;

    public Game() {
        t = new Thread(this);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setVisible(true);
        setBackground(Color.black);
        b = Ball.getBall();
        p = Player.getPlayer();
        l = new ManageLevels();
        capsules = new ArrayList<Capsule>();
        bullets = new ArrayList<Bullet>();
        menu = new MainMenu();
        try {
            clip = AudioSystem.getClip();
        } catch (Exception e) {}
        game = this;
    }

    private void init(Graphics g) {
        if(state != State.GAME ) {
            menu.render(g);
        } else {
            inGame = true;
            scoreBar(g);
            setLevelBackground(g);
            try {
                paintBorders(g);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            b.render(g);
            p.render(g);
            l.getCurrentLevel().render(g);
            renderCapsule(g);
            renderBullet(g);
            g.dispose();
        }
    }

    private void scoreBar(Graphics g) {
        g.setFont(new Font("Arial",Font.ITALIC,20));
        g.setColor(Color.red);
        g.drawString("SCORE",20,30);
        g.drawString("LEVEL",270,30);
        g.drawString("LIFE",500,30);
        g.setColor(Color.white);
        g.drawString(Integer.toString(Player.getPlayer().getScore()),40,50);
        g.drawString(Integer.toString(ManageLevels.getNumberCurrentLevel() + 1),300,50);
        g.drawString(Integer.toString(Player.getPlayer().getLives()),520,50);
    }

    private void setLevelBackground(Graphics g) {
        this.image = l.getCurrentLevel().getLevelBgImage();
        int xI = 0, yI = Config.ALTEZZA_SCOREBAR;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double repeatWidth = getWidth() / imageWidth + 1;
        double repeatHeight = Config.LUNGHEZZA / imageHeight;
        for (int i = 0; i < repeatHeight; i++) {
            for (int j = 0; j < repeatWidth; j++) {
                g.drawImage(image, xI, yI, null);
                xI += imageWidth;
            }
            xI = 0;
            yI += imageHeight;
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        init(g);
    }

    private void paintBorders(Graphics g) throws IOException {
        Image borderLeft = ImageIO.read(new File("res/border_left.png"));
        Image borderRight = ImageIO.read(new File("res/border_right.png"));
        Image borderTop = ImageIO.read(new File("res/border_top.png"));
        g.drawImage(borderLeft, 0, Config.ALTEZZA_SCOREBAR, null);
        g.drawImage(borderRight, Config.LARGHEZZA - 27, Config.ALTEZZA_SCOREBAR, null);
        g.drawImage(borderTop, 0, Config.ALTEZZA_SCOREBAR, null);

    }

    private void renderCapsule(Graphics g) {
        for(Capsule c : capsules) {
            c.render(g);
        }
    }

    private void renderBullet(Graphics g) {
        for(Bullet bullet : bullets) {
            System.out.println("Bullet!");
            bullet.render(g);
        }
    }

    @Override
    public void run() {
        while (true) {
            sleep(0);
            while (inGame) {
                p.move();
                if(!p.haveMagnet()) {
                    b.setX(p.getX() + p.getWidth() / 2.0);
                } else {
                    b.setX(p.getX() + p.getMagnetBallDifferencePaddle());
                }
                b.setY(Player.getPlayer().getY()-24);
                checkCapsule();
                sleep(0);
                repaint();
                while (isAlive) {
                    p.move();
                    b.move();
                    checkCollision();
                    checkCapsule();
                    checkCollisionBullet();
                    repaint();
                    sleep(0);
                    checkLives();
                    checkWin();
                    if (state == State.PAUSE || state == State.LOSE) {
                        break;
                    }
                }
                if (state == State.PAUSE || state == State.LOSE) {
                    break;
                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(!isAlive && state == State.GAME) {
                isAlive = true;
            }
            if(isAlive && p.haveLaser()) {
                bullets.add(new Bullet(p.getX(),p.getY()));
                bullets.add(new Bullet(p.getX() + p.getWidth(),p.getY()));
                playSound("bullet.wav",true);
            }
            if(!Player.getPlayer().haveMagnet() && !isAlive) {
                isAlive = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            p.setSpeedX(Config.PLAYER_SPEED);
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            p.setSpeedX(-Config.PLAYER_SPEED);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(Game.state == State.MENU) {
                state = State.LEVEL;
                repaint();
                stopClip();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (state == State.PAUSE) {
                state = State.GAME;
                inGame = true;
                repaint();

            } else if (state == State.GAME) {
                state = State.PAUSE;
                inGame = false;
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT) {
            p.setSpeedX(0);
        }
    }

    private void sleep(long millis) {
        if(millis == 0) {
            millis = Config.THREAD_SLEEP_MILLIS;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void checkLives() {
        if (b.lose()) {
            isAlive = false;
            playSound("lose.wav",false);
            b.destroy();
            p.destroy(true);
            capsules = new ArrayList<Capsule>();
            bullets = new ArrayList<Bullet>();
            if (p.getLives() == 0) {
                inGame = false;
                state = State.LOSE;
                repaint();
            } else {
                state = State.LEVEL;
                inGame = false;
                b = Ball.getBall();
                repaint();
            }
        }
    }

    private void changeLevel() {
        if(ManageLevels.getNumberCurrentLevel() < 33) {
            isAlive = false;
            p.destroy(false);
            b.destroy();
            capsules = new ArrayList<Capsule>();
            bullets = new ArrayList<Bullet>();
            b = Ball.getBall();
            l.levelUp();
            state = State.LEVEL;
            inGame = false;
            repaint();
        } else {
            state = State.WIN;
        }
    }

    private void checkWin() {
        if(l.getCurrentLevel().getRemainBrick() == 0) {
            changeLevel();
        }
        if (p.isNeedChangeLevel()) {
            changeLevel();
            p.setNeedChangeLevelFalse();
        }
    }

    private void checkCollision() {
        double ballRadius = b.getWidth() / 2.0;
        Brick collidedBrick = null;
        for (Brick brick : l.getCurrentLevel().getAllBricks()) {

            if (brick.isShot())
                continue;


            if (b.getRect().intersects(brick.getRect())) {
                collidedBrick = brick;
               System.out.println("Collide!");
                System.out.println("X Ball: " + b.getX() + "Y Ball: " +b.getY() );
                System.out.println("X init brick: "+ brick.getX() + "X finish break: " + (brick.getX() + brick.getWidth()));
                System.out.println("Y init brick: "+ brick.getY() + "Y finish break: " + (brick.getY() + brick.getHeight()));
                break;
            }
        }

        if (collidedBrick != null) {
            if ((b.getY() < (collidedBrick.getY() - collidedBrick.getHeight() / 2.0)) ||
                    (b.getY() > (collidedBrick.getY() + collidedBrick.getHeight() / 2.0))) {
                b.setDy(-1.0 * b.getDy());
            } else {
                b.setDx(-1.0 * b.getDx());
                System.out.println("Enter here");
            }

            if (collidedBrick.isDestroyable()) {
                if(collidedBrick.getRemainShot() > 1) {
                    playSound("hit_undestroyable_brick.wav",true);
                } else {
                    playSound("hit_brick.wav",true);
                }
                collidedBrick.shot();

                p.setScore(p.getScore() + collidedBrick.getScore());
                b.setVelocity(b.getVelocity() + 0.05);
                if (collidedBrick.isShot()) {
                    if (collidedBrick.isHaveCapsule()) {
                        int type = (int) (Math.random() * (6 - 1 + 1)) + 1;
                        capsules.add(
                                new Capsule(

                                        collidedBrick.getX() + collidedBrick.getWidth() / 2.0,
                                        collidedBrick.getY() + collidedBrick.getHeight() / 2.0,
                                        "capsule_"+Capsule.getNameFromValue(type).toLowerCase()+".gif",
                                        type
                                )
                        );
                    }
                }
            } else {
                playSound("hit_undestroyable_brick.wav",true);
            }
        }
    }

    private void checkCollisionBullet() {
        ListIterator<Bullet> bulletListIterator = bullets.listIterator();
         while (bulletListIterator.hasNext()){
             Bullet currentBullet = bulletListIterator.next();
             if (!currentBullet.move()) {
                 bulletListIterator.remove();
             }
            for (Brick brick : l.getCurrentLevel().getAllBricks()) {
                if (brick.isShot())
                    continue;
                if (currentBullet.getRect().intersects(brick.getRect())) {
                    if (brick.isDestroyable()) {
                        if (brick.getRemainShot() >= 2) {
                            playSound("hit_undestroyable_brick.wav",true);
                        } else {
                            playSound("hit_brick.wav",true);
                        }
                        brick.shot();
                        bulletListIterator.remove();
                        p.setScore(p.getScore() + brick.getScore());
                        break;
                    }
                }
            }
        }
    }

    private void checkCapsule() {
        ListIterator<Capsule> capsuleListIterator = capsules.listIterator();
        while(capsuleListIterator.hasNext()){
            Capsule currentCapsule = capsuleListIterator.next();
            if(currentCapsule.whenSpawned()) {
                capsuleListIterator.remove();
            }
            if(currentCapsule.getRect().intersects(p.getRect())) {
                p.setCapsuleEffect(currentCapsule.getType());
                capsuleListIterator.remove();
            }
        }
    }

    public static synchronized void playSound(final String name, boolean withOther) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    clip = AudioSystem.getClip();
                    URL url = new File("res/Sounds/"+name).toURI().toURL();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
                        clip.open(inputStream);
                        clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void stopClip() {
        clip.stop();
    }
    public void setInGame(boolean inGame) { this.inGame = inGame; }
    public void setIsAlive(boolean isAlive) { this.isAlive = isAlive; }
    public boolean getIsAlive() {return isAlive; }
}

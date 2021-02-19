package com.giuseppe.igpe.arkanoid;

import java.util.ArrayList;

public class ManageLevels {
    private static ArrayList<Level> levels;
    private static int currentLevel = 0;


    public ManageLevels() {
        restart();
    }

    private static void init() {
        int i = 0;
        for(int[][] level: com.giuseppe.igpe.arkanoid.Config.Level.LEVELS) {
            Level l = new Level(i++);
            l.setLevelBgImage(Integer.toString(i%4 + 1));
            l.init(level);

            levels.add(l);
        }
    }

    public Level getCurrentLevel() {
        Level l = null;
        try {
            l = this.levels.get(currentLevel);
        } catch (NullPointerException e) {}
        return l;
    }

    public static int getNumberCurrentLevel() {
        return currentLevel;
    }

    public void levelUp() {
        currentLevel++;
    }

    public static void restart() {
        levels = new ArrayList<Level>();
        init();
        currentLevel = 0;
    }

}

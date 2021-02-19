package com.giuseppe.igpe.arkanoid;

import com.giuseppe.igpe.arkanoid.Config.Config;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static java.lang.System.exit;

public class Launcher {
    public static void main(String args[]) {
        
        try {
            Windows window = new Windows(Config.LARGHEZZA, Config.LUNGHEZZA_FINESTRA, Config.NOME_GIOCO, new Game());
        } catch (IOException e) {}
    }
}

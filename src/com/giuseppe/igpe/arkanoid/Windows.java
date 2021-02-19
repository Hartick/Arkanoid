package com.giuseppe.igpe.arkanoid;

import com.giuseppe.igpe.arkanoid.Config.Config;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Windows  {
    public Windows (int width, int height, String title, Game game) throws IOException {
        JFrame f = new JFrame(title);
        f.setPreferredSize(new Dimension(width,height));
        f.setMinimumSize(new Dimension(width,height));
        f.setMaximumSize(new Dimension(width,height));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.add(game);
    }
}

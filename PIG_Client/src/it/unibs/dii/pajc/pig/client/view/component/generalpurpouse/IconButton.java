package it.unibs.dii.pajc.pig.client.view.component.generalpurpouse;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class IconButton extends JButton {

    public IconButton(String iconResourcePath, String alternativeText, int height) {
        super();

        try {
            URL iconURl = getClass().getResource(iconResourcePath);
            BufferedImage img = ImageIO.read(iconURl);
            ImageIcon icon = new ImageIcon(img.getScaledInstance(height, height, Image.SCALE_SMOOTH));
            this.setBorderPainted(false);
            this.setBorder(BorderFactory.createEmptyBorder());
            this.setFocusPainted(false);
            this.setContentAreaFilled(false);
            this.setIcon(icon);
            this.setPreferredSize(new Dimension(height, height));
        } catch (IOException e) {
            this.setText(alternativeText);
        }
    }

}

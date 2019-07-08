package sample;

import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class TextEffect {

    /**
     * Adds a piece of text onto the given image
     * @param img The image to add text to
     * @param text The text to be added
     * @param x The x-coordinate of the beginning of the text
     * @param y The y-coordicate of the beginning of the text
     * @param c The color of the text
     * @param font The font of the text
     * @param angle The angle at which the text should be added
     * @return A new BufferedImage with the given text added
     */
    public BufferedImage apply(BufferedImage img, String text,  int x, int y, Color c, Font font,  double angle) {
        Graphics2D g = img.createGraphics();
        g.setColor(c);
        g.setFont(font);
        g.rotate(angle);
        g.drawString(text, x, y );
        return img;
    }
}

package sample;

import java.awt.image.BufferedImage;


/**
 * This class represents an effect on one of the color channels, e.g. red color channel
 */
public class ColorComponentEffect {

    private Color color;

    /**
     *
     * @param c  The color component as an instance of the Color enumeration. I.e. red, green, blue or alpha
     */
    public ColorComponentEffect(Color c ) {
       this.color = c;


   }
    /** Private helper funtion. Returns the new color after adjustment for one pixel*/
    private int pixelOp(int c, float amount) {
        int componentValue = (c & color.getBitmask()) >> color.getBits();
        int newValue = clamp(componentValue *amount);
        int b = ~color.getBitmask();
        //System.out.println(Integer.toHexString(b));

        return (c & b) + (newValue << color.getBits());

    }

    /**
     * Applies a single color channel adjust to the given image.
     * @param img The image to be edited
     * @param amount The percentage that the new color should be in comparison to the old. E.g. 1.5 = 150 %.
     * @return The adjusted image.
     */
    public BufferedImage filter(BufferedImage img, float amount) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                int newValue =pixelOp(img.getRGB(x,y), amount);
                res.setRGB(x,y, newValue);

            }
        }
        return res;
    }
    /** Makes sure the value is an integer between 0 and 255. */
    private int clamp(float v) {

        return Math.min(Math.max(0, (int) v), 255);
    }
}


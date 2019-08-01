package sample;

import java.awt.image.BufferedImage;
import java.util.concurrent.*;


/**
 * This class represents an effect on one of the color channels, e.g. red color channel
 */
public class ColorComponentEffect {

    private Color color;
    private int numCores = Runtime.getRuntime().availableProcessors();
    private  ExecutorService executorService = Executors.newFixedThreadPool(numCores);

    /**
     *
     * @param c  The color component as an instance of the Color enumeration. I.e. red, green, blue or alpha
     */
    public ColorComponentEffect(Color c ) {
       this.color = c;


   }
    /** Private helper funtion. Returns the new color after adjustment for one pixel*/
    private int pixelOp(int c, double amount) {
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
    public BufferedImage filter(BufferedImage img, double amount) {
        long start = System.currentTimeMillis();
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                int newValue =pixelOp(img.getRGB(x,y), amount);
                res.setRGB(x,y, newValue);

            }
        }
        System.out.println((System.currentTimeMillis() - start) / 1000.0 + "s");
        return res;
    }
    /** Makes sure the value is an integer between 0 and 255. */
    private int clamp(double v) {

        return Math.min(Math.max(0, (int) v), 255);
    }

    public BufferedImage filterThreaded(BufferedImage img, double amount) {
        long start = System.currentTimeMillis();
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        //int numCores = Runtime.getRuntime().availableProcessors();
        //System.out.println("Number of cores: " + numCores);
        CountDownLatch latch = new CountDownLatch(numCores);

        int partitionWidth = (int) ((double) res.getWidth() / numCores);
        //System.out.println("Partition width: " + partitionWidth);
        //int partitionHeight = res.getHeight() / 2;


        for(int n = 0; n < numCores ; n++) {
            int finalN = n;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    for(int y = 0; y < img.getHeight(); y++) {
                        for (int x = finalN * partitionWidth; x < (finalN + 1) * partitionWidth; x++) {

                            int newValue = pixelOp(img.getRGB(x, y), amount);
                            res.setRGB(x, y, newValue);

                            //System.out.println("Latch: " + latch.getCount());


                        }
                    }
                    latch.countDown();
                }
            };
            //System.out.println("Submitting task #" +n);
            executorService.submit(task);
        }



        try {
            latch.await();
            //System.out.println("Tasks completed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println((System.currentTimeMillis() - start)  / 1000.0 + " s");
            return res;
        }


    }
}


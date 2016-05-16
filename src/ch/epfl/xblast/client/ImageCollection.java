package ch.epfl.xblast.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * Class grouping Images together. Here it is used to group the Images for the
 * players, the explosions etc.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class ImageCollection {

    /**
     * ImageCollection for the players Images
     */
    public static final ImageCollection PLAYERS = new ImageCollection("player");
    /**
     * ImageCollection for the boards blocks
     */
    public static final ImageCollection BLOCKS = new ImageCollection("block");
    /**
     * ImageCollection for bombs and blasts
     */
    public static final ImageCollection EXPLOSIONS = new ImageCollection(
            "explosion");
    /**
     * ImageCollection for the Scoreboard
     */
    public static final ImageCollection SCORE = new ImageCollection("score");

    private final List<BufferedImage> images;

    /**
     * Creates a new ImageCollection from a specified folder. Gives an empty
     * Collection if folder name is corrupted, leaves out corrupted files.
     * 
     * @param folderName
     *            name of the folder containing the images
     */
    public ImageCollection(String folderName) {
        images = new ArrayList<BufferedImage>();
        // ignore exceptions in class ImageCollection: try with empty catch
        File dir;
        try {
            // get directory
            dir = new File(ImageCollection.class.getClassLoader()
                    .getResource(folderName).toURI());
            for (File f : dir.listFiles()) {
                // try/catch for parseInt() and read(). catch does nothing, so
                // corrupted images are ignored
                try {
                    // add image to the right index
                    int number = Integer.parseInt(f.getName().substring(0, 3));
                    images.add(number, ImageIO.read(f));
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Returns an image at the specified index or throws an exception.
     * 
     * @param index
     *            number of the desired image
     * @return the image at the specified index
     * @throws NoSuchElementException
     *             if the image does not exist in this collection
     */
    public BufferedImage image(int index) {
        if (images.get(index) == null) {
            throw new NoSuchElementException("Image does not exist.");
        } else {
            return images.get(index);
        }
    }

    /**
     * Returns an image at the specified index or null.
     * 
     * @param index
     *            number of the desired image
     * @return the image at the specified index or null if it doesn't exist in
     *         this collection.
     */
    public BufferedImage imageOrNull(int index) {
        return images.get(index);
    }

}

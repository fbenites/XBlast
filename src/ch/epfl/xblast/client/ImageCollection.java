package ch.epfl.xblast.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * Class grouping Images together. Here it is used to group the Images for the
 * players, the explosions etc.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class ImageCollection {

    private final Map<Integer, BufferedImage> images;

    /**
     * Creates a new ImageCollection from a specified folder. Gives an empty
     * Collection if folder name is corrupted, leaves out corrupted files.
     * 
     * @param folderName
     *            name of the folder containing the images
     */
    public ImageCollection(String folderName) {
        images = new HashMap<Integer, BufferedImage>();
        File dir;
        // try to read folder, no exception should be thrown here, ignore
        // exception/empty catch
        try {
            dir = new File(ImageCollection.class.getClassLoader()
                    .getResource(folderName).toURI());
            for (File f : dir.listFiles()) {
                // try to get images
                try {
                    int number = Integer.parseInt(f.getName().substring(0, 3));
                    images.put(number, ImageIO.read(f));
                } catch (Exception e) {
                    // skip corrupted files, ignore exception
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

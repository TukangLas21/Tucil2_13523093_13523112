package quadtree;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class IOHandler {
    
    // Load image dari path sebagai BufferedImage
    public static BufferedImage getImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            return image;
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            return null;
        }
    }

    // Dapatkan image data dalam bentuk array 3D
    // Array: [x][y][arrRGB]
    public static int[][][] getImageData(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][][] imageData = new int[width][height][4];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                imageData[i][j] = extractRGB(rgb);
            }
        }
        return imageData;
    }

    // Extract nilai RGB dari sebuah piksel
    // arrRGB = [Red, Green, Blue, Alpha]
    public static int[] extractRGB(int rgb) {
        int[] arrRGB = new int[4];
        arrRGB[0] = (rgb >> 16) & 0xFF; // Red
        arrRGB[1] = (rgb >> 8) & 0xFF;  // Green
        arrRGB[2] = rgb & 0xFF;         // Blue
        arrRGB[3] = (rgb >> 24) & 0xFF; // Alpha
        return arrRGB;
    }
}

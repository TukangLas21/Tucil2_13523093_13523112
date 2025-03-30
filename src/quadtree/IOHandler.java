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

    // Mendapatkan file extension
    public static String getExtension(String imagePath) {
        int dotIndex = imagePath.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < imagePath.length() - 1) {
            return imagePath.substring(dotIndex + 1).toLowerCase();
        }
        return ""; // Return string kosong jika tidak ada extension
    }

    // Mendapatkan ukuran file
    public static long getFileSize(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            return file.length(); // Mengembalikan ukuran file dalam byte
        } else {
            System.err.println("File not found: " + imagePath);
            return -1; // Mengembalikan -1 jika file tidak ditemukan
        }
    }

    // Save image pada output path
    public static void saveImage(BufferedImage image, String outputPath, String extension) {
        try {
            File outputFile = new File(outputPath, "default." + extension);
            // File outputFile = new File(outputPath);
            if (!extension.isEmpty()) {
                ImageIO.write(image, extension, outputFile);
            } else {
                // Jika tidak ada extension, gunakan default JPG
                ImageIO.write(image, "jpg", outputFile);
            }
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
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

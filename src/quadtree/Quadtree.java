package quadtree;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Quadtree {
    public Node root;
    public double threshold; // minimum 0.0
    public int minSize; // minimum 1 pixel
    public BufferedImage imageData;
    private long nodeCount = 0; 
    private int depth = 0;

    public Quadtree(double threshold, int minSize) {
        this.root = null; 
        this.threshold = threshold; 
        this.minSize = minSize; 
        this.imageData = null; 
    }

    public long getNodeCount() {
        return nodeCount; 
    }

    public int getDepth() {
        return depth; 
    }

    public void CreateQuadtree(BufferedImage imageData, String errorMethod, boolean verbose) {
        if (imageData == null) {
            throw new IllegalArgumentException("Image data cannot be null.");
        }
        this.imageData = imageData; 
        int width = imageData.getWidth(); 
        int height = imageData.getHeight(); 
        int avgRed = (int) getAverage(0, 0, width, height, "r");
        int avgGreen = (int) getAverage(0, 0, width, height, "g");
        int avgBlue = (int) getAverage(0, 0, width, height, "b");
        Color colorNode = new Color(avgRed, avgGreen, avgBlue);
        this.root = new Node(0, 0, width, height, colorNode.getRGB(), 0); 

        BuildQuadTree(root, imageData, errorMethod, calcError(root.x, root.y, root.width, root.height, errorMethod), 1, verbose);
    }

    public void CreateQuadtree(BufferedImage imageData, String errorMethod) {
        CreateQuadtree(imageData, errorMethod, false); 
    }

    // Helper method to build the quadtree recursively
    private void BuildQuadTree(Node node, BufferedImage imageData, String errorMethod, double error, int level, boolean verbose) {
        
        if (node == null) {
            return; 
        }
        if (node.width <= 1 && node.height <= 1) {
            return; 
        }
        if ((node.width/2 * node.height/2) < minSize) {
            return; 
        }
        if (error < threshold) {
            return;
        }

        if (level > depth) {
            depth = level; 
        }

        // Verbose output for debugging
        if (verbose) {
            if (level < 5) System.out.println("Node Count: " + nodeCount + ", Depth: " + depth + ", Level: " + level + ", Error: " + error);
        }

        int leftHalfWidth = (node.width + 1) / 2;
        int topHalfHeight = (node.height + 1) / 2;
        int rightHalfWidth = node.width - leftHalfWidth;
        int bottomHalfHeight = node.height - topHalfHeight;

        double errors[] = new double[4];

        for (int i = 0; i < 4; i++) {
            errors[i] = Double.POSITIVE_INFINITY;
        }

        errors[0] = calcError(node.x, node.y, leftHalfWidth, topHalfHeight, errorMethod); // Kiri Atas
        errors[1] = calcError(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, errorMethod); // Kanan Atas
        errors[2] = calcError(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, errorMethod); // Kiri Bawah
        errors[3] = calcError(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, errorMethod); // Kanan Bawah

        node.children = new Node[4];

        int avgRed = (int) getAverage(node.x, node.y, leftHalfWidth, topHalfHeight, "r");
        int avgGreen = (int) getAverage(node.x, node.y, leftHalfWidth, topHalfHeight, "g");
        int avgBlue = (int) getAverage(node.x, node.y, leftHalfWidth, topHalfHeight, "b");
        Color colorNode = new Color(avgRed, avgGreen, avgBlue);
        int argb = colorNode.getRGB();

        node.children[0] = new Node(node.x, node.y, leftHalfWidth, topHalfHeight, argb, level); // Kiri Atas

        avgRed = (int) getAverage(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, "r");
        avgGreen = (int) getAverage(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, "g");
        avgBlue = (int) getAverage(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, "b");
        colorNode = new Color(avgRed, avgGreen, avgBlue);
        argb = colorNode.getRGB();

        node.children[1] = new Node(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, argb, level); // Kanan Atas

        avgRed = (int) getAverage(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, "r");
        avgGreen = (int) getAverage(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, "g");
        avgBlue = (int) getAverage(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, "b");

        colorNode = new Color(avgRed, avgGreen, avgBlue);
        argb = colorNode.getRGB(); 

        node.children[2] = new Node(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, argb, level); // Kiri Bawah

        avgRed = (int) getAverage(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, "r");
        avgGreen = (int) getAverage(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, "g");
        avgBlue = (int) getAverage(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, "b");
        colorNode = new Color(avgRed, avgGreen, avgBlue);
        argb = colorNode.getRGB(); 

        node.children[3] = new Node(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, argb, level); // Kanan Bawah

        for (int i = 0; i < 4; i++) {
            if (node.children[i] != null) {
                BuildQuadTree(node.children[i], imageData, errorMethod, errors[i], level + 1, verbose);
                nodeCount++;
            }
        }
    }

    public BufferedImage ImageFromQuadtree(String extension) {
        if (extension.equals("png") || extension.equals("gif") || extension.equals("tiff")) {
            BufferedImage newImageData = new BufferedImage(root.width, root.height, BufferedImage.TYPE_INT_ARGB); 
            FillImageData(root, newImageData); 
            return newImageData; 
        } else {
            BufferedImage newImageData = new BufferedImage(root.width, root.height, BufferedImage.TYPE_INT_RGB); 
            FillImageData(root, newImageData);
            return newImageData;
        }
    }

    public void FillImageData(Node node, BufferedImage newImageData) {
        if (node == null) {
            return;
        }
        if (node.children == null) {
            for (int i = node.x; i < node.x + node.width; i++) {
                for (int j = node.y; j < node.y + node.height; j++) {
                    newImageData.setRGB(i, j, (node.argb & 0x00FFFFFF) | (imageData.getRGB(i, j) & 0xFF000000));
                }
            }
            return;
        }
        for (Node child : node.children) {
            FillImageData(child, newImageData);
        }
    }

    /**
     * 
     * @param originalImageFile
     * @param targetCompressionPercentage Range 0.0 - 100.0
     * @param errorMethod
     * @return
     */
    public static BufferedImage targetedPercentageCompress(File originalImageFile, double targetCompressionPercentage, String errorMethod, String extension) {
        if (targetCompressionPercentage < 0 || targetCompressionPercentage > 100) {
            throw new IllegalArgumentException("Target compression percentage must be between 0 and 100.");
        }

        // Create bin folder to store temporary image file
        String binDir = System.getProperty("java.io.tmpdir") + File.separator + "quadtree" + File.separator + "bin";
        File binFolder = new File(binDir);
        if (!binFolder.exists()) {
            binFolder.mkdirs();
        }

        // Initialize image
        BufferedImage image;
        try {
            image = ImageIO.read(originalImageFile);
        } catch (Exception e) {
            throw new RuntimeException("Error reading image file: " + e.getMessage());
        }

        if (image == null) {
            throw new IllegalArgumentException("Image not found. Please try again.");
        }

        // Get original file size
        long originalFileSize = originalImageFile.length();

        // Calculate target file size
        long targetFileSize = (long) ((1 - targetCompressionPercentage / 100) * originalFileSize);

        // Initialize quadtree
        double upper = 255 * 255 / 4;
        double lower = 0;
        double mid = (upper + lower) / 2;
        int minBlockSize = 1;
        Quadtree quadtree;
        double tolerance = 1; // Tolerance for binary search
        double stoppingThreshold = 0.5; // Stopping threshold for binary search

        int iteration = 1;
        double lastPercent = targetCompressionPercentage;

        while (true) { 
            quadtree = new Quadtree(mid, minBlockSize);
            quadtree.CreateQuadtree(image, errorMethod);
            BufferedImage compressedImage = quadtree.ImageFromQuadtree(extension);
            File compressedImageFile = new File(binDir + File.separator + "compressed_image." + extension);
            try {
                ImageIO.write(compressedImage, extension, compressedImageFile);
            } catch (Exception e) {
                throw new RuntimeException("Error writing compressed image file: " + e.getMessage());
            }
            long compressedFileSize = compressedImageFile.length();
            double percent = compressionPercentage(originalFileSize, compressedFileSize);

            // Verbose
            System.out.println("Iteration " + iteration + ": Compression percentage: " + percent + "%, Target: " + targetCompressionPercentage + "%, File size: " + compressedFileSize + " bytes");
            iteration++;

            if (Math.abs(percent - lastPercent) < stoppingThreshold) {
                // If the difference is less than the stopping threshold, break the loop
                return compressedImage; // Return the compressed image
            }
            lastPercent = percent; // Update last percent

            if (percent >= targetCompressionPercentage) {
                // Check if the difference is within the tolerance
                if (Math.abs(percent - targetCompressionPercentage) <= tolerance) {
                    return compressedImage; // Return the compressed image
                } else {
                    upper = mid; // Adjust upper bound
                }
            } else {
                lower = mid; // Adjust lower bound
            }
            mid = (upper + lower) / 2; // Update mid value
        }
    }


    public double calcError(int x, int y, int width, int height, String method) {
        double error = Double.POSITIVE_INFINITY;
        switch (method) {
            case "variance" -> error = calcVariance(x, y, width, height);
            case "mad" -> error = calcMAD(x, y, width, height);
            case "mpd" -> error = calcMPD(x, y, width, height);
            case "entropy" -> error = calcEntropy(x, y, width, height);
            // case "ssim" -> error = calcSSIM(x, y, width, height);
            default -> {
            }
        }
        return error;
    }

    // Variansi
    public double calcVariance(int x, int y, int width, int height) {
        double varRed = getVariance(x, y, width, height, "r");
        double varGreen = getVariance(x, y, width, height, "g");
        double varBlue = getVariance(x, y, width, height, "b");
        // double varAlpha = getVariance(x, y, width, height, "a");

        return (varRed + varGreen + varBlue) / 3.0;
    }
    
    // Mendapatkan nilai rata-rata setiap kanal
    // TODO: optimasi menggunakan matriks nilai integral (mendapatkan nilai mean menjadi O(1) untuk tiap iterasi)
    public double getAverage(int x, int y, int width, int height, String channel) {
        double sum = 0;
        for (int j = y; j < y + height; j++) {
            for (int i = x; i < x + width; i++) {
                switch (channel) {
                    case "r" -> sum += (imageData.getRGB(i, j) >> 16) & 0xFF; // Mengambil nilai merah dari argb  
                    case "g" -> sum += (imageData.getRGB(i, j) >> 8) & 0xFF; // Mengambil nilai hijau dari argb
                    case "b" -> sum += imageData.getRGB(i, j) & 0xFF; // Mengambil nilai biru dari argb
                    case "a" -> sum += (imageData.getRGB(i, j) >> 24) & 0xFF; // Mengambil nilai alpha dari argb
                }
            }
        }
        int length = width * height;
        return sum / (length == 0 ? 1 : length); // Menghindari pembagian dengan nol
    }


    // Mendapatkan nilai variansi setiap kanal warna
    // TODO: optimasi menggunakan matriks nilai integral (mendapatkan nilai mean menjadi O(1) untuk tiap iterasi)
    public double getVariance(int x, int y, int width, int height, String channel) {
        double avg = getAverage(x, y, width, height, channel);
        double sum = 0;
        for (int j = y; j < y + height; j++) {
            for (int i = x; i < x + width; i++) {
                switch (channel) {
                    case "r" -> sum += Math.pow(((imageData.getRGB(i, j) >> 16) & 0xFF) - avg, 2); // Mengambil nilai merah dari argb
                    case "g" -> sum += Math.pow(((imageData.getRGB(i, j) >> 8) & 0xFF) - avg, 2); // Mengambil nilai hijau dari argb
                    case "b" -> sum += Math.pow((imageData.getRGB(i, j) & 0xFF) - avg, 2); // Mengambil nilai biru dari argb
                    case "a" -> sum += Math.pow(((imageData.getRGB(i, j) >> 24) & 0xFF) - avg, 2); // Mengambil nilai alpha dari argb
                }
            }
        }
        int length = width * height;
        return sum / (length == 0 ? 1 : length); // Menghindari pembagian dengan nol
    }

    public double calcMAD(int x, int y, int width, int height) {
        double madRed = getMAD(x, y, width, height, "r");
        double madGreen = getMAD(x, y, width, height, "g");
        double madBlue = getMAD(x, y, width, height, "b");
        // double madAlpha = getMAD(x, y, width, height, "a");

        return (madRed + madGreen + madBlue) / 3.0;
    }

    // Mendapatkan nilai MAD setiap kanal warna
    // TODO: optimasi menggunakan matriks nilai integral (mendapatkan nilai mean menjadi O(1) untuk tiap iterasi)
    public double getMAD(int x, int y, int width, int height, String channel) {
        double avg = getAverage(x, y, width, height, channel);
        double sum = 0;
        for (int j = y; j < y + height; j++) {
            for (int i = x; i < x + width; i++) {
                switch (channel) {
                    case "r" -> sum += Math.abs(((imageData.getRGB(i, j) >> 16) & 0xFF) - avg);
                    case "g" -> sum += Math.abs(((imageData.getRGB(i, j) >> 8) & 0xFF) - avg);
                    case "b" -> sum += Math.abs((imageData.getRGB(i, j) & 0xFF) - avg);
                    case "a" -> sum += Math.abs(((imageData.getRGB(i, j) >> 24) & 0xFF) - avg);
                }
            }
        }
        int length = width * height;
        return sum / (length == 0 ? 1 : length);
    }

    public double calcMPD(int x, int y, int width, int height) {
        double mpdRed = getMPD(x, y, width, height, "r");
        double mpdGreen = getMPD(x, y, width, height, "g");
        double mpdBlue = getMPD(x, y, width, height, "b");
        // double mpdAlpha = getMPD(x, y, width, height, "a");

        return (mpdRed + mpdGreen + mpdBlue) / 3.0;
    }

    // TODO: optimasi menggunakan quadtree untuk menghitung min dan max tiap blok (O(n^2))
    public double getMPD(int x, int y, int width, int height, String channel) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int value = 0;

        for (int j = y; j < y + height; j++) {
            for (int i = x; i < x + width; i++) {
                switch (channel) {
                    case "r" -> value = ((imageData.getRGB(i, j) >> 16) & 0xFF);
                    case "g" -> value = ((imageData.getRGB(i, j) >> 8) & 0xFF);
                    case "b" -> value = (imageData.getRGB(i, j) & 0xFF);
                    case "a" -> value = ((imageData.getRGB(i, j) >> 24) & 0xFF);
                }
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }
        }
        return max - min;
    }

    public double calcEntropy(int x, int y, int width, int height) {
        double entropyRed = getEntropy(x, y, width, height, "r");
        double entropyGreen = getEntropy(x, y, width, height, "g");
        double entropyBlue = getEntropy(x, y, width, height, "b");
        // double entropyAlpha = getEntropy(x, y, width, height, "a");

        return (entropyRed + entropyGreen + entropyBlue) / 3.0;
    }

    public double getEntropy(int x, int y, int width, int height, String channel) {
        double entropy = 0;
        int[] histogram = new int[256];
        int totalPixels = width * height;

        for (int j = y; j < y + height; j++) {
            for (int i = x; i < x + width; i++) {
                switch (channel) {
                    case "r" -> histogram[(imageData.getRGB(i, j) >> 16) & 0xFF]++;
                    case "g" -> histogram[(imageData.getRGB(i, j) >> 8) & 0xFF]++;
                    case "b" -> histogram[imageData.getRGB(i, j) & 0xFF]++;
                    case "a" -> histogram[(imageData.getRGB(i, j) >> 24) & 0xFF]++;
                }
            }
        }

        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > 0) {
                double p = (double) histogram[i] / totalPixels;
                entropy -= p * Math.log(p) / Math.log(2);
            }
        }

        return entropy;
    }

    public static double compressionPercentage(long originalSize, long compressedSize) {
        return ((double) (originalSize - compressedSize) / originalSize) * 100;
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\Karol\\ITB\\Teknik-Informatika\\semester_4\\IF2211_StrategiAlgoritma\\Tucil2_13523093_13523112\\test\\plant.jpg"; // Ganti dengan path gambar yang sesuai
        File originalImageFile = new File(filePath);
        String extension = IOHandler.getExtension(filePath);
        String errorMethod = "variance"; // Ganti dengan metode error yang diinginkan
        double targetCompressionPercentage = 1.0; // Ganti dengan persentase kompresi yang diinginkan

        BufferedImage compressedImage = Quadtree.targetedPercentageCompress(originalImageFile, targetCompressionPercentage, errorMethod, extension);
        
        // Save file 
        String outputPath = "C:\\Users\\Karol\\ITB\\Teknik-Informatika\\semester_4\\IF2211_StrategiAlgoritma\\Tucil2_13523093_13523112\\test\\";
        String fileName = "compressed_image"; 

        String outputFilePath = outputPath + fileName + "." + extension;
        File outputFile = new File(outputFilePath);
        try {
            ImageIO.write(compressedImage, extension, outputFile);
            System.out.println("Compressed image saved at: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error saving compressed image: " + e.getMessage());
        }
        System.out.println("Original file size: " + originalImageFile.length() + " bytes");
        System.out.println("Compressed file size: " + outputFile.length() + " bytes");
        System.out.println("Compression percentage: " + Quadtree.compressionPercentage(originalImageFile.length(), outputFile.length()) + "%");
        System.out.println("Node count: " + compressedImage.getWidth() * compressedImage.getHeight() / (originalImageFile.length() / outputFile.length()));
    }
}

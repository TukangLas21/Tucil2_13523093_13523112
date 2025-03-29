package quadtree;
import java.awt.image.BufferedImage;
public class Quadtree {
    public Node root;
    public double threshold; 
    public int min_size;
    public BufferedImage imageData;

    public Quadtree(double threshold, int min_size) {
        this.root = null; 
        this.threshold = threshold; 
        this.min_size = min_size; 
        this.imageData = null; 
    }

    public void CreateQuadtree(BufferedImage imageData, String errorMethod) {
        this.imageData = imageData; 
        int width = imageData.getWidth(); 
        int height = imageData.getHeight(); 
        this.root = new Node(0, 0, width, height, imageData.getRGB(0, 0)); 

        // // Debugging
        // int leftHalfWidth = (root.width + 1) / 2;
        // int topHalfHeight = (root.height + 1) / 2;
        // int rightHalfWidth = root.width - leftHalfWidth;
        // int bottomHalfHeight = root.height - topHalfHeight;
        // double topRightError = calcVariance(leftHalfWidth, root.y, rightHalfWidth, topHalfHeight);
        // double bottomLeftError = calcVariance(root.x, topHalfHeight, leftHalfWidth, bottomHalfHeight);
        // double bottomRightError = calcVariance(leftHalfWidth, topHalfHeight, rightHalfWidth, bottomHalfHeight);
        // System.out.println("Root: " + root.x + ", " + root.y + ", " + root.width + ", " + root.height + ", " + root.argb);
        // System.out.println("Dim: " + leftHalfWidth + ", " + topHalfHeight + ", " + rightHalfWidth + ", " + bottomHalfHeight);
        // System.out.println("Err: " + topRightError + ", " + bottomLeftError + ", " + bottomRightError);


        BuildQuadTree(root, imageData, errorMethod, calcError(root.x, root.y, root.width, root.height, errorMethod)); // Menghitung error untuk node root
    }


    // Helper method to build the quadtree recursively
    private void BuildQuadTree(Node node, BufferedImage imageData, String errorMethod, double error) {
        
        if (node == null) {
            return; 
        }
        if (node.width <= 1 && node.height <= 1) {
            return; 
        }
        if ((node.width/2 * node.height/2) < min_size) {
            return; 
        }
        if (error < threshold) {
            return; 
        }
        // System.out.println("BuildQuadTree: " + node.x + ", " + node.y + ", " + node.width + ", " + node.height + ", " + node.argb);

        int leftHalfWidth = (node.width + 1) / 2;
        int topHalfHeight = (node.height + 1) / 2;
        int rightHalfWidth = node.width - leftHalfWidth;
        int bottomHalfHeight = node.height - topHalfHeight;

        double errors[] = new double[4];

        for (int i = 0; i < 4; i++) {
            errors[i] = Double.MAX_VALUE;
        }

        errors[0] = calcError(node.x, node.y, leftHalfWidth, topHalfHeight, errorMethod); // Kiri Atas
        errors[1] = calcError(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, errorMethod); // Kanan Atas
        errors[2] = calcError(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, errorMethod); // Kiri Bawah
        errors[3] = calcError(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, errorMethod); // Kanan Bawah

        // System.out.println("Error: " + topLeftError + ", " + topRightError + ", " + bottomLeftError + ", " + bottomRightError);
        // System.out.println("Threshold: " + threshold);
        

        // TODO: gunakan mean untuk mengisi warna node
        node.children = new Node[4];

        int avgRed = (int) getAverage(node.x, node.y, leftHalfWidth, topHalfHeight, "r");
        int avgGreen = (int) getAverage(node.x, node.y, leftHalfWidth, topHalfHeight, "g");
        int avgBlue = (int) getAverage(node.x, node.y, leftHalfWidth, topHalfHeight, "b");
        int argb = (0xFF << 24) | (avgRed << 16) | (avgGreen << 8) | avgBlue; // Menggunakan alpha 255
        node.children[0] = new Node(node.x, node.y, leftHalfWidth, topHalfHeight, argb); // Kiri Atas

        avgRed = (int) getAverage(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, "r");
        avgGreen = (int) getAverage(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, "g");
        avgBlue = (int) getAverage(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, "b");
        argb = (0xFF << 24) | (avgRed << 16) | (avgGreen << 8) | avgBlue; // Menggunakan alpha 255
        node.children[1] = new Node(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, argb); // Kanan Atas

        avgRed = (int) getAverage(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, "r");
        avgGreen = (int) getAverage(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, "g");
        avgBlue = (int) getAverage(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, "b");
        argb = (0xFF << 24) | (avgRed << 16) | (avgGreen << 8) | avgBlue; // Menggunakan alpha 255
        node.children[2] = new Node(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, argb); // Kiri Bawah

        avgRed = (int) getAverage(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, "r");
        avgGreen = (int) getAverage(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, "g");
        avgBlue = (int) getAverage(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, "b");
        argb = (0xFF << 24) | (avgRed << 16) | (avgGreen << 8) | avgBlue; // Menggunakan alpha 255
        node.children[3] = new Node(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, argb); // Kanan Bawah
        
        

        for (int i = 0; i < 4; i++) {
            if (node.children[i] != null) {
                BuildQuadTree(node.children[i], imageData, errorMethod, errors[i]); 
            }
        }
    }

    public BufferedImage ImageFromQuadtree() {
        BufferedImage newImageData = new BufferedImage(root.width, root.height, BufferedImage.TYPE_INT_ARGB); 
        FillImageData(root, newImageData); 
        return newImageData; 
    }

    public void FillImageData(Node node, BufferedImage imageData) {
        if (node == null) {
            return; 
        }
        for (int i = node.x; i < node.x + node.width; i++) {
            for (int j = node.y; j < node.y + node.height; j++) {
                imageData.setRGB(i, j, node.argb);
            }
        }
        if (node.children != null) {
            for (Node child : node.children) {
                FillImageData(child, imageData);
            }
        }
    }

    public double calcError(int x, int y, int width, int height, String method) {
        double error = 0;
        switch (method) {
            case "variance" -> error = calcVariance(x, y, width, height);
            // case "mad" -> error = calcMAD(x, y, width, height);
            // case "mpd" -> error = calcMPD(x, y, width, height);
            // case "entropy" -> error = calcEntropy(x, y, width, height);
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
    public double getAverage(int x, int y, int width, int height, String channel) {
        double sum = 0;
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
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
    public double getVariance(int x, int y, int width, int height, String channel) {
        double avg = getAverage(x, y, width, height, channel);
        double sum = 0;
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
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

}

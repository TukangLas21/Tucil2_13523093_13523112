package quadtree;
import java.awt.image.BufferedImage;
public class Quadtree {
    public Node root;
    public double threshold; 
    public int min_size;
    public BufferedImage imageData;

    public Quadtree(int threshold, int min_size) {
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
        BuildQuadTree(root, imageData, errorMethod); 
    }

    // public Node CreateNodes(int x, int y, int width, int height, int[][][] imageData) {
    //     return new Node(x, y, width, height, imageData); // Placeholder
    // }

    private void BuildQuadTree(Node node, BufferedImage imageData, String errorMethod) {

        if (node == null) {
            return; 
        }
        if (node.width <= 1 && node.height <= 1) {
            return; 
        }

        int leftHalfWidth = (node.width + 1) / 2;
        int topHalfHeight = (node.height + 1) / 2;
        int rightHalfWidth = node.width - leftHalfWidth;
        int bottomHalfHeight = node.height - topHalfHeight;

        double topLeftError = 0;
        double topRightError = 0;
        double bottomLeftError = 0;
        double bottomRightError = 0;

        switch (errorMethod) {
            case "variance" -> {
                // TODO: Implementasi error variance
            }
            case "mad" -> {
                // TODO: Implementasi error mean absolute deviation
            }
            case "mpd" -> {
                // TODO: Implementasi error mean pixel difference
            }
            case "entropy" -> {
                // TODO: Implementasi error entropy
            }
            case "ssim" -> {
                // TODO: Implementasi error structural similarity index
            }
            default -> {
            }
        }
        
        // if (error < threshold || halfWidth * halfHeight < min_size) {
        //     return;
        // }

        // TODO: gunakan mean untuk mengisi warna node
        node.children = new Node[4];
        
        if (topHalfHeight * leftHalfWidth >= min_size && topLeftError > threshold) {
            node.children[0] = new Node(node.x, node.y, leftHalfWidth, topHalfHeight, imageData.getRGB(node.x, node.y)); // Kiri Atas
        } else {
            node.children[0] = null; 
        }
        if (topHalfHeight * rightHalfWidth >= min_size && topRightError > threshold) {
            node.children[1] = new Node(node.x + leftHalfWidth, node.y, rightHalfWidth, topHalfHeight, imageData.getRGB(node.x + leftHalfWidth, node.y)); // Kanan Atas
        } else {
            node.children[1] = null; 
        }
        if (bottomHalfHeight * leftHalfWidth >= min_size && bottomLeftError > threshold) {
            node.children[2] = new Node(node.x, node.y + topHalfHeight, leftHalfWidth, bottomHalfHeight, imageData.getRGB(node.x, node.y + topHalfHeight)); // Kiri Bawah
        } else {
            node.children[2] = null; 
        }
        if (bottomHalfHeight * rightHalfWidth >= min_size && bottomRightError > threshold) {
            node.children[3] = new Node(node.x + leftHalfWidth, node.y + topHalfHeight, rightHalfWidth, bottomHalfHeight, imageData.getRGB(node.x + leftHalfWidth, node.y + topHalfHeight)); // Kanan Bawah
        } else {
            node.children[3] = null; 
        }

        for (Node child : node.children) {
            BuildQuadTree(child, imageData, errorMethod);
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
}

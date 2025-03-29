package quadtree;
public class Quadtree {
    public Node root;
    public double threshold; 
    public int min_size;

    public Quadtree(int threshold, int min_size) {
        this.root = null; // Inisialisasi root sebagai null
        this.threshold = threshold; // Set threshold untuk pembagian
        this.min_size = min_size; // Set ukuran minimum untuk pembagian
    }

    // Placeholder CreateTree dan CreateNodes
    public void CreateTree(int x, int y, int[][][] imageData) {

    }

    public Node CreateNodes(int x, int y, int width, int height, int[][][] imageData) {
        return new Node(x, y, width, height, imageData); // Placeholder
    }

    public static double calcAverage(Node QTreeNode) {
        double averageRed, averageGreen, averageBlue;
        double average = 0;

        averageRed = QTreeNode.getAverageRed();
        averageGreen = QTreeNode.getAverageGreen();
        averageBlue = QTreeNode.getAverageBlue();

        average = (averageRed + averageGreen + averageBlue) / 3.0;

        return average;
    }

    // Variansi
    public static double calcVariance(Node QTreeNode) {
        double varRed = QTreeNode.getVarianceRed();
        double varGreen = QTreeNode.getVarianceGreen();
        double varBlue = QTreeNode.getVarianceBlue();

        return (varRed + varGreen + varBlue) / 3.0;
    }

    public static double calcMAD(Node QTreeNode) {
        double MADRed = QTreeNode.getMADRed();
        double MADGreen = QTreeNode.getMADGreen();
        double MADBlue = QTreeNode.getMADBlue();

        return (MADRed + MADGreen + MADBlue) / 3.0;
    }

    public static double calcMaxDiff(Node QTreeNode) {
        double maxDiffRed = QTreeNode.getMaxDiffRed();
        double maxDiffGreen = QTreeNode.getMaxDiffGreen();
        double maxDiffBlue = QTreeNode.getMaxDiffBlue();

        return (maxDiffRed + maxDiffGreen + maxDiffBlue) / 3.0;
    }

    public static double calcEntropy(Node QTreeNode) {
        double entropyRed = QTreeNode.getEntropyRed();
        double entropyGreen = QTreeNode.getEntropyGreen();
        double entropyBlue = QTreeNode.getEntropyBlue();

        return (entropyRed + entropyGreen + entropyBlue) / 3.0;
    }
}

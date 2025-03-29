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

}

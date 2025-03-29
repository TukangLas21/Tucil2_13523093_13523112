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

    public void CreateChildren(Node node, int[][][] imageData) {
        int halfWidth = node.width / 2;
        int halfHeight = node.height / 2;
        
        if (halfWidth * halfHeight < min_size) {
            return;
        }

        node.children = new Node[4];
        node.children[0] = CreateNodes(node.x, node.y, halfWidth, halfHeight, imageData); // Kiri Atas
        node.children[1] = CreateNodes(node.x + halfWidth, node.y, halfWidth, halfHeight, imageData); // Kanan Atas
        node.children[2] = CreateNodes(node.x, node.y + halfHeight, halfWidth, halfHeight, imageData); // Kiri Bawah
        node.children[3] = CreateNodes(node.x + halfWidth, node.y + halfHeight, halfWidth, halfHeight, imageData); // Kanan Bawah

    }
}

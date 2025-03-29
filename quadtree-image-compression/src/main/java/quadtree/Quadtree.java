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
    public void CreateTree(int x, int y, int size, int[][] image) {
        this.root = CreateNodes(x, y, size, image); // Buat node root
        // Implementasi untuk membagi node menjadi anak-anaknya
        // Misalnya, jika ukuran > threshold, bagi menjadi 4 anak
        if (size > threshold) {
            int newSize = size / 2;
            root.children = new Node[4];
            root.children[0] = CreateNodes(x, y, newSize, image); // NW
            root.children[1] = CreateNodes(x + newSize, y, newSize, image); // NE
            root.children[2] = CreateNodes(x, y + newSize, newSize, image); // SW
            root.children[3] = CreateNodes(x + newSize, y + newSize, newSize, image); // SE
        }
    }

    public Node CreateNodes(int x, int y, int size, int[][] image) {
        return new Node(x, y, size); // Placeholder
    }

}

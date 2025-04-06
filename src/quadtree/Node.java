package quadtree;
public class Node {
    int x, y; // x as column, y as row
    int width, height; 
    int argb;
    int level;
    double error;
    boolean isLeaf; 
    Node[] children;

    public Node(int x, int y, int width, int height, int argb, int level, double error) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.argb = argb;
        this.level = level;
        this.error = error;
        this.isLeaf = true; // Set isLeaf to true by default
        this.children = null; 
    }
}

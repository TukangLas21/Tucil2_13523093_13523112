package quadtree;
public class Node {
    int x, y; // x as column, y as row
    int width, height; 
    int argb;
    int level;
    Node[] children;

    public Node(int x, int y, int width, int height, int argb, int level) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.argb = argb;
        this.level = level;
        this.children = null; 
    }
}

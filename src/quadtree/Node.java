package quadtree;
public class Node {
    int x, y; // Koordinat blok (piksel kiri atas)
    int width, height; // Ukuran blok
    int[][][] arrRGB;
    Node[] children; // Array untuk menyimpan anak-anak node

    public Node(int x, int y, int width, int height, int[][][] arrRGB) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.arrRGB = arrRGB;   
        this.children = null; 
    }
}

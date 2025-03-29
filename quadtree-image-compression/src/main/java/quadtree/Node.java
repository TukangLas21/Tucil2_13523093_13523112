package quadtree;
public class Node {
    int x, y; // Koordinat blok (piksel kiri atas)
    int size; // Ukuran blok
    int r, g, b; // Nilai warna setiap kanal 
    Node[] children; // Array untuk menyimpan anak-anak node

    public Node(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.r = 0;  
        this.g = 0;  
        this.b = 0; 
        this.children = null;  
    }

    public Node(int x, int y, int size, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.children = null; // Inisialisasi children sebagai null
    }
}

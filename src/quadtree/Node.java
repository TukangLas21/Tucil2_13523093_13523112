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

    // Mendapatkan nilai tiap kanal dari setiap piksel
    public int[] getArrRed() {
        int[] arrRed = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                arrRed[i * height + j] = arrRGB[i][j][0]; 
            }
        }
        return arrRed;
    }

    public int[] getArrGreen() {
        int[] arrGreen = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                arrGreen[i * height + j] = arrRGB[i][j][1]; 
            }
        }
        return arrGreen;
    }

    public int[] getArrBlue() {
        int[] arrBlue = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                arrBlue[i * height + j] = arrRGB[i][j][2]; 
            }
        }
        return arrBlue;
    }
    
    // Mendapatkan nilai rata-rata setiap kanal
    public double getAverageRed() {
        int[] arrRed = getArrRed();
        double sum = 0;
        for (int i = 0; i < arrRed.length; i++) {
            sum += arrRed[i];
        }
        return sum / arrRed.length;
    }

    public double getAverageGreen() {
        int[] arrGreen = getArrGreen();
        double sum = 0;
        for (int i = 0; i < arrGreen.length; i++) {
            sum += arrGreen[i];
        }
        return sum / arrGreen.length;
    }

    public double getAverageBlue() {
        int[] arrBlue = getArrBlue();
        double sum = 0;
        for (int i = 0; i < arrBlue.length; i++) {
            sum += arrBlue[i];
        }
        return sum / arrBlue.length;
    }

    // Mendapatkan nilai variansi setiap kanal warna
    public double getVarianceRed() {
        int[] arrRed = getArrRed();
        double avgRed = getAverageRed();
        double sum = 0;
        for (int i = 0; i < arrRed.length; i++) {
            sum += Math.pow(arrRed[i] - avgRed, 2);
        }
        return sum / arrRed.length;
    }

    public double getVarianceGreen() {
        int[] arrGreen = getArrGreen();
        double avgGreen = getAverageGreen();
        double sum = 0;
        for (int i = 0; i < arrGreen.length; i++) {
            sum += Math.pow(arrGreen[i] - avgGreen, 2);
        }
        return sum / arrGreen.length;
    }

    public double getVarianceBlue() {
        int[] arrBlue = getArrBlue();
        double avgBlue = getAverageBlue();
        double sum = 0;
        for (int i = 0; i < arrBlue.length; i++) {
            sum += Math.pow(arrBlue[i] - avgBlue, 2);
        }
        return sum / arrBlue.length;
    }

    // Mendapatkan nilai MAD setiap kanal warna
    public double getMADRed() {
        int[] arrRed = getArrRed();
        double avgRed = getAverageRed();
        double sum = 0;
        for (int i = 0; i < arrRed.length; i++) {
            sum += Math.abs(arrRed[i] - avgRed);
        }
        return sum / arrRed.length;
    }

    public double getMADGreen() {
        int[] arrGreen = getArrGreen();
        double avgGreen = getAverageGreen();
        double sum = 0;
        for (int i = 0; i < arrGreen.length; i++) {
            sum += Math.abs(arrGreen[i] - avgGreen);
        }
        return sum / arrGreen.length;
    }

    public double getMADBlue() {
        int[] arrBlue = getArrBlue();
        double avgBlue = getAverageBlue();
        double sum = 0;
        for (int i = 0; i < arrBlue.length; i++) {
            sum += Math.abs(arrBlue[i] - avgBlue);
        }
        return sum / arrBlue.length;
    }

    // Mendapatkan max difference setiap kanal warna
    public int getMaxDiffRed() {
        int[] arrRed = getArrRed();
        int min = 0;
        int max = 0;

        for (int i = 0; i < arrRed.length; i++) {
            if (arrRed[i] < min) {
                min = arrRed[i];
            }
            if (arrRed[i] > max) {
                max = arrRed[i];
            }
        }

        return max - min;
    }

    public int getMaxDiffGreen() {
        int[] arrGreen = getArrGreen();
        int min = 0;
        int max = 0;

        for (int i = 0; i < arrGreen.length; i++) {
            if (arrGreen[i] < min) {
                min = arrGreen[i];
            }
            if (arrGreen[i] > max) {
                max = arrGreen[i];
            }
        }

        return max - min;
    }

    public int getMaxDiffBlue() {
        int[] arrBlue = getArrBlue();
        int min = 0;
        int max = 0;

        for (int i = 0; i < arrBlue.length; i++) {
            if (arrBlue[i] < min) {
                min = arrBlue[i];
            }
            if (arrBlue[i] > max) {
                max = arrBlue[i];
            }
        }

        return max - min;
    }

    // Mendapatkan nilai entropy
    public double getEntropyRed() {
        int[] arrRed = getArrRed();
        double entropyRed = 0;
        int[] redHist = new int[256];

        for (int i = 0; i < arrRed.length; i++) {
            redHist[arrRed[i]]++;
        }

        for (int i = 0; i < redHist.length; i++) {
            if (redHist[i] > 0) {
                double p = (double) redHist[i] / arrRed.length;
                entropyRed -= p * Math.log(p) / Math.log(2);
            }
        }

        return entropyRed;
    }

    public double getEntropyGreen() {
        int[] arrGreen = getArrGreen();
        double entropyGreen = 0;
        int[] greenHist = new int[256];

        for (int i = 0; i < arrGreen.length; i++) {
            greenHist[arrGreen[i]]++;
        }

        for (int i = 0; i < greenHist.length; i++) {
            if (greenHist[i] > 0) {
                double p = (double) greenHist[i] / arrGreen.length;
                entropyGreen -= p * Math.log(p) / Math.log(2);
            }
        }

        return entropyGreen;
    }

    public double getEntropyBlue() {
        int[] arrBlue = getArrBlue();
        double entropyBlue = 0;
        int[] blueHist = new int[256];

        for (int i = 0; i < arrBlue.length; i++) {
            blueHist[arrBlue[i]]++;
        }

        for (int i = 0; i < blueHist.length; i++) {
            if (blueHist[i] > 0) {
                double p = (double) blueHist[i] / arrBlue.length;
                entropyBlue -= p * Math.log(p) / Math.log(2);
            }
        }

        return entropyBlue;
    }
}

import java.awt.image.BufferedImage;
import java.util.Scanner;

import quadtree.IOHandler;
import quadtree.Quadtree;

public class Main {
    
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        
        System.out.println("------------------------------------------------------------------------");
        System.out.println("                      QuadTree Image Compressor                         ");
        System.out.println("------------------------------------------------------------------------");

        while (true) { 
            
            // Inputs
            // General TODO: Verifikasi input
            System.out.print("Silakan masukkan alamat gambar: ");
            String filePath = scanner.nextLine();

            BufferedImage image = IOHandler.getImage(filePath);
            if (image == null) {
                System.out.println("Gambar tidak ditemukan. Silakan coba lagi.");
                continue;
            }
            String extension = IOHandler.getExtension(filePath);
            if (extension.isEmpty()) {
                System.out.println("Format gambar tidak valid. Silakan coba lagi.");
                continue;
            }
            long fileSizeBefore = IOHandler.getFileSize(filePath);

            String errorMethod = "";
            while (true) { 
                System.out.println("Silakan pilih metode perhitungan error: ");
                System.out.println("1. Variance");
                System.out.println("2. Mean Absolute Deviation");
                System.out.println("3. Max Pixel Difference");
                System.out.println("4. Entropy");
                System.out.print("Pilihan: ");
                String userChoice = scanner.nextLine();
                int choice = Integer.parseInt(userChoice);

                if (choice < 1 || choice > 4) {
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    continue;
                } 

                switch (choice) {
                    case 1:
                        errorMethod = "variance";
                        break;
                    case 2:
                        errorMethod = "mad";
                        break;
                    case 3:
                        errorMethod = "mpd";
                        break;
                    case 4:
                        errorMethod = "entropy";
                        break;
                }
                
                break;
            }
            
            // TODO: verifikasi minimum size dan threshold sesuai dengan metode error yang dipilih
            System.out.print("Silakan masukkan threshold atau ambang batas: ");
            String thresholdInput = scanner.nextLine();
            double threshold = Double.parseDouble(thresholdInput); 

            System.out.print("Silakan masukkan ukuran blok minimum: ");
            String minBlockSizeInput = scanner.nextLine();
            int minBlockSize = Integer.parseInt(minBlockSizeInput);

            // TODO: input target kompresi (persen) disini 

            System.out.print("Silakan masukkan alamat output: ");
            String outputPath = scanner.nextLine();

            // TODO: alamat GIF

            // Proses
            long startTime = System.currentTimeMillis();
            Quadtree quadtree = new Quadtree(threshold, minBlockSize);
            quadtree.CreateQuadtree(image, errorMethod);
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;

            // Outputs
            System.out.println("Waktu kompresi: " + runTime + " ms");
            System.out.println("Ukuran file sebelum kompresi: " + fileSizeBefore + " bytes");

            BufferedImage compressedImage = quadtree.ImageFromQuadtree();
            IOHandler.saveImage(compressedImage, outputPath, extension);
            System.out.println("Gambar berhasil disimpan di: " + outputPath);

            long fileSizeAfter = IOHandler.getFileSize(outputPath + "/default." + extension); // TODO: tambahkan nama file
            System.out.println("Ukuran file setelah kompresi: " + fileSizeAfter + " bytes");

            double compressionRatio = (double) fileSizeBefore / fileSizeAfter * 100;
            System.out.printf("Persentase kompresi: %.2f%%\n", compressionRatio);

            // TODO: kedalaman dan jumlah simpul pohon
            System.out.println("Kedalaman pohon: " + quadtree.getDepth());
            System.out.println("Jumlah simpul pohon: " + quadtree.getNodeCount());
            System.out.println("------------------------------------------------------------------------");
            break;
        }
    }
}

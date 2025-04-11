import java.awt.image.BufferedImage;
import java.io.File;
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
            // Input alamat absolut gambar
            System.out.print("Silakan masukkan alamat gambar, gunakan \"/\" sebagai seperator: ");
            String filePath = scanner.nextLine().replaceAll("^['\"]+|['\"]+$", "");

            // Validasi gambar yang di-input
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
            long fileSizeBefore = IOHandler.getFileSize(filePath); // Catat ukuran file sebelum dikompresi

            // Input metode perhitungan error
            String errorMethod = "";
            while (true) { 
                System.out.println("Silakan pilih metode perhitungan error: ");
                System.out.println("1. Variance");
                System.out.println("2. Mean Absolute Deviation");
                System.out.println("3. Max Pixel Difference");
                System.out.println("4. Entropy");
                System.out.print("Pilihan: ");
                String userChoice = scanner.nextLine();
                if (userChoice.isEmpty()) {
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    continue;
                }
                int choice;
                try {
                    choice = Integer.parseInt(userChoice);
                } catch (NumberFormatException e) {
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    continue;
                }

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
            
            // Input ambang batas 
            String thresholdInput = "";
            double threshold = 0;
            while (true) {
                System.out.print("Silakan masukkan ambang batas (max: " + Quadtree.MaximumError(errorMethod) + "): ");
                thresholdInput = scanner.nextLine();
                if (thresholdInput.isEmpty()) {
                    System.out.println("Ambang batas tidak valid. Silakan coba lagi.");
                    continue;
                }
                try {
                    threshold = Double.parseDouble(thresholdInput);
                } catch (NumberFormatException e) {
                    System.out.println("Ambang batas tidak valid. Silakan coba lagi.");
                    continue;
                }
                if (threshold < 0 || threshold > Quadtree.MaximumError(errorMethod)) {
                    System.out.println("Ambang batas tidak valid. Silakan coba lagi.");
                    continue;
                }
                break;
            }

            // Input ukuran blok minimum 
            String minBlockSizeInput = "";
            int minBlockSize = 0;
            while (true) {
                System.out.print("Silakan masukkan ukuran blok minimum: ");
                minBlockSizeInput = scanner.nextLine();
                if (minBlockSizeInput.isEmpty()) {
                    System.out.println("Ukuran blok minimum tidak valid. Silakan coba lagi.");
                    continue;
                }
                try {
                    minBlockSize = Integer.parseInt(minBlockSizeInput);
                } catch (NumberFormatException e) {
                    System.out.println("Ukuran blok minimum tidak valid. Silakan coba lagi.");
                    continue;
                }
                if (minBlockSize < 1) {
                    System.out.println("Ukuran blok minimum tidak valid. Silakan coba lagi.");
                    continue;
                }
                break;
            }

            // Input target kompresi
            String targetKompresi = "";
            double targetPercent = 0;
            while (true) { 
                System.out.print("Silakan masukkan target kompresi (0..1): ");
                targetKompresi = scanner.nextLine();
                if (targetKompresi.isEmpty()) {
                    System.out.println("Target kompresi tidak valid. Silakan coba lagi.");
                    continue;
                }
                try {
                    targetPercent = Double.parseDouble(targetKompresi);
                } catch (NumberFormatException e) {
                    System.out.println("Target kompresi tidak valid. Silakan coba lagi.");
                    continue;
                }
                if (targetPercent < 0 || targetPercent > 1) {
                    System.out.println("Masukkan target tidak valid. Silakan masukkan dalam bentuk floating number (0..1).");
                } else {
                    break;
                }
            }

            targetPercent *= 100;

            // Input alamat absolut gambar hasil kompresi
            String outputPath = IOHandler.getOutputPath();

            String outputFileName = IOHandler.getFileName(outputPath, extension);

            String absolutePath = outputPath + File.separator + outputFileName + "." + extension;

            // Input pilihan dan alamat absolut GIF
            boolean saveGIF = false;
            System.out.print("Apakah Anda ingin menyimpan dalam bentuk GIF? (Y/N): ");
            String gifChoice = scanner.nextLine();
            while (true) {
                if (gifChoice.equals("y") || gifChoice.equals("Y")) {
                    saveGIF = true;
                    break;
                } else if (gifChoice.equals("n") || gifChoice.equals("N")) {
                    break;
                } else {
                    System.out.println("Masukan tidak valid, silakan masukkan Y atau N: ");
                    gifChoice = scanner.nextLine();
                }
            }

            String outputPathGIF = "";
            String GIFname = "";
            if (saveGIF) {
                outputPathGIF = IOHandler.getPathGIF();
                GIFname = IOHandler.getFileName(outputPathGIF, "gif");
            }

            // Proses + catat waktu
            long startTime;
            Quadtree quadtree;
            if (targetPercent == 0.0) {
                startTime = System.currentTimeMillis();
                quadtree = new Quadtree(threshold, minBlockSize);
                System.out.println("Mengompresi gambar dengan metode " + errorMethod + ", threshold " + threshold + ", dan minimum size " + minBlockSize + ".");
                quadtree.CreateQuadtree(image, errorMethod);
            } else {
                File imageFile = new File(filePath);
                System.out.println("Anda memilih untuk mengompresi gambar dengan target persentase " + targetPercent + "%" + " menggunakan metode " + errorMethod + " dan minimum size " + minBlockSize + ".");
                System.out.print("Silakan pilih batas atas pencarian threshold. Tekan enter untuk default (" + Quadtree.MaximumError(errorMethod) + "): ");
                String maxThresholdInput;
                double maxThreshold = 0;
                while (true) {
                    maxThresholdInput = scanner.nextLine();
                    if (maxThresholdInput.isEmpty()) {
                        break;
                    }
                    try {
                        maxThreshold = Double.parseDouble(maxThresholdInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Ambang batas tidak valid. Silakan coba lagi.");
                        continue;
                    }
                    if (maxThreshold < 0) {
                        System.out.println("Ambang batas tidak valid. Silakan coba lagi.");
                        continue;
                    }
                    break;
                }
                if (maxThresholdInput.isEmpty()) {
                    maxThreshold = Quadtree.MaximumError(errorMethod);
                }
                startTime = System.currentTimeMillis();
                System.out.println("Mengompresi gambar dengan target persentase " + targetPercent + "%, metode " + errorMethod + ", threshold " + threshold + ", minimum size " + minBlockSize + ", dan batas atas pencarian threshold " + maxThreshold + ".");
                quadtree = Quadtree.TargetedPercentageCompress(imageFile, targetPercent, errorMethod, maxThreshold, extension, false);
            }
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;

            // Outputs
            // Output gambar setelah dikompresi
            BufferedImage compressedImage = quadtree.ImageFromQuadtree(extension);
            if (compressedImage == null) {
                System.out.println("Gambar tidak dapat dikompresi. Silakan coba lagi.");
                continue;
            }
            IOHandler.saveImage(compressedImage, absolutePath, extension);

            // Output GIF hasil (jika diinginkan)
            if (saveGIF) {
                BufferedImage[] frames = quadtree.GetFrames(extension);
                IOHandler.createGIF(frames, outputPathGIF, GIFname);
            }

            // Output waktu eksekusi
            System.out.println("Waktu kompresi: " + runTime + " ms");

            // Output ukuran gambar sebelum
            System.out.println("Ukuran file sebelum kompresi: " + fileSizeBefore + " bytes");

            // Output ukuran gambar setelah
            long fileSizeAfter = IOHandler.getFileSize(absolutePath); 
            System.out.println("Ukuran file setelah kompresi: " + fileSizeAfter + " bytes");

            // Output persentase kompresi
            double compressionRatio = Quadtree.compressionPercentage(fileSizeBefore, fileSizeAfter);
            System.out.printf("Persentase kompresi: %.2f%%\n", compressionRatio);

            // Output kedalaman pohon
            System.out.println("Kedalaman pohon: " + quadtree.getDepth());

            // Output banyak simpul pohon
            System.out.println("Jumlah simpul pohon: " + quadtree.getNodeCount());

            System.out.println("------------------------------------------------------------------------");
        }
    }
}

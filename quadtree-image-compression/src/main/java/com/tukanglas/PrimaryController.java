package com.tukanglas;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import quadtree.Quadtree;

public class PrimaryController {
    // Exit button
    @FXML
    Button exitButton = new Button();

    // Reset button
    @FXML
    Button resetButton = new Button();

    // Input image components
    @FXML
    ImageView inputImageView = new ImageView();
    @FXML
    Button selectInputImageButton = new Button();
    @FXML
    Label inputImageDimensionLabel = new Label();
    @FXML
    Label inputImageSizeLabel = new Label();

    // Compressed image components
    @FXML
    ImageView compressedImageView = new ImageView();
    @FXML
    Button exportCompressedButton = new Button();
    @FXML
    Button exportGifButton = new Button();
    @FXML
    Label compressionTimeLabel = new Label();
    @FXML
    Label compressedSizeLabel = new Label();
    @FXML
    Label compressionPercentageLabel = new Label();
    @FXML
    Label compressedTreeDepthLabel = new Label();
    @FXML
    Label compressedNodeCountLabel = new Label();
    
    // Error measurement methods components
    @FXML
    ToggleGroup errorMeasurementMethodsToggleGroup = new ToggleGroup();

    @FXML
    RadioButton varianceButton = new RadioButton();
    @FXML
    TextField varianceThresholdTextField = new TextField();

    @FXML
    RadioButton meanAbsoluteDeviationButton = new RadioButton();
    @FXML
    TextField meanAbsoluteDeviationThresholdTextField = new TextField();

    @FXML
    RadioButton maxPixelDifferenceButton = new RadioButton();
    @FXML
    TextField maxPixelDifferenceThresholdTextField = new TextField();

    @FXML
    RadioButton entropyButton = new RadioButton();
    @FXML
    TextField entropyThresholdTextField = new TextField();

    @FXML
    RadioButton structuralSimilarityIndexButton = new RadioButton();
    @FXML
    TextField structuralSimilarityIndexThresholdTextField = new TextField();

    // Minimum block size components
    @FXML
    TextField minimumBlockSizeTextField = new TextField();

    // Compression percentage components
    @FXML
    RadioButton compressionPercentageActiveButton = new RadioButton();
    @FXML
    TextField compressionPercentageTextField = new TextField();
    @FXML
    Slider compressionPercentageSlider = new Slider();

    // Compression button
    @FXML
    Label compressionErrorMessageLabel = new Label();
    @FXML
    Button compressButton = new Button();

    // Private attributes
    private File inputImageFile = null;
    private File compressedImageFile = null;
    private File compressedGifFile = null;
    private Quadtree quadtree = null;

    // Initialize
    @FXML
    private void initialize() {
        // Set toggle group
        varianceButton.setToggleGroup(errorMeasurementMethodsToggleGroup);
        meanAbsoluteDeviationButton.setToggleGroup(errorMeasurementMethodsToggleGroup);
        maxPixelDifferenceButton.setToggleGroup(errorMeasurementMethodsToggleGroup);
        entropyButton.setToggleGroup(errorMeasurementMethodsToggleGroup);
        structuralSimilarityIndexButton.setToggleGroup(errorMeasurementMethodsToggleGroup);

        // Set default values
        varianceThresholdTextField.setText("0.0");
        meanAbsoluteDeviationThresholdTextField.setText("0.0");
        maxPixelDifferenceThresholdTextField.setText("0");
        entropyThresholdTextField.setText("0.0");
        structuralSimilarityIndexThresholdTextField.setText("0.0");

        minimumBlockSizeTextField.setText("1");

        compressionPercentageTextField.setText("0");
        compressionPercentageSlider.setValue(0);

        // Set disable
        varianceThresholdTextField.setDisable(true);
        meanAbsoluteDeviationThresholdTextField.setDisable(true);
        maxPixelDifferenceThresholdTextField.setDisable(true);
        entropyThresholdTextField.setDisable(true);
        structuralSimilarityIndexThresholdTextField.setDisable(true);

        minimumBlockSizeTextField.setDisable(false);

        compressionPercentageTextField.setDisable(true);
        compressionPercentageSlider.setDisable(true);

        compressButton.setDisable(true);

        // Hide labels
        inputImageDimensionLabel.setVisible(false);
        inputImageSizeLabel.setVisible(false);

        compressionTimeLabel.setVisible(false);
        compressedSizeLabel.setVisible(false);
        compressionPercentageLabel.setVisible(false);
        compressedTreeDepthLabel.setVisible(false);
        compressedNodeCountLabel.setVisible(false);

        compressionErrorMessageLabel.setVisible(false);
    }

    // Exit button
    @FXML
    private void exit() throws IOException {
        System.exit(0);
    }

    // Reset button
    @FXML
    private void reset() {
        // Reset all fields and labels to their initial state
        inputImageView.setImage(null);
        compressedImageView.setImage(null);
        inputImageDimensionLabel.setText("");
        inputImageSizeLabel.setText("");
        compressionTimeLabel.setText("");
        compressedSizeLabel.setText("");
        compressionPercentageLabel.setText("");
        compressedTreeDepthLabel.setText("");
        compressedNodeCountLabel.setText("");
        compressionErrorMessageLabel.setVisible(false);

        // Reset radio buttons
        errorMeasurementMethodsToggleGroup.selectToggle(null);
        varianceButton.setSelected(false);
        meanAbsoluteDeviationButton.setSelected(false);
        maxPixelDifferenceButton.setSelected(false);
        entropyButton.setSelected(false);
        structuralSimilarityIndexButton.setSelected(false);

        inputImageFile = null;
        compressedImageFile = null;
        compressedGifFile = null;

        initialize();
    }

    // Select input image button
    @FXML
    private void selectInputImage() {
        // Open file chooser dialog to select input image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Input Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        inputImageFile = fileChooser.showOpenDialog(null);
        if (inputImageFile != null) {
            // Load the selected image and display it in the inputImageView
            String imagePath = inputImageFile.toURI().toString();
            inputImageView.setImage(new javafx.scene.image.Image(imagePath));
            inputImageDimensionLabel.setText(inputImageView.getImage().getWidth() + " x " + inputImageView.getImage().getHeight());
            double imageSize = inputImageFile.length() / 1024.0; // Convert to KB
            if (imageSize > 1024) {
                imageSize /= 1024.0; // Convert to MB
                inputImageSizeLabel.setText(String.format("%.2f MB", imageSize));
            } else {
                inputImageSizeLabel.setText(String.format("%.2f KB", imageSize));
            }
            inputImageDimensionLabel.setVisible(true);
            inputImageSizeLabel.setVisible(true);
        }
    }

    @FXML
    private void compress() {
        // TODO: Implement compression logic
        if (inputImageFile != null) {
            // Perform compression using the selected error measurement method and parameters
            // Update the compressedImageView with the compressed image
            // Update the compression time, size, percentage, tree depth, and node count labels
            // Show the compressed image and labels
            // compressedImageView.setImage(...);
            // compressionTimeLabel.setText(...);
            // compressedSizeLabel.setText(...);
            // compressionPercentageLabel.setText(...);
            // compressedTreeDepthLabel.setText(...);
            // compressedNodeCountLabel.setText(...);

            int minBlockSize = Integer.parseInt(minimumBlockSizeTextField.getText());
            double threshold = 0.0;
            if (varianceButton.isSelected()) {
                threshold = Double.parseDouble(varianceThresholdTextField.getText());
            } else if (meanAbsoluteDeviationButton.isSelected()) {
                threshold = Double.parseDouble(meanAbsoluteDeviationThresholdTextField.getText());
            } else if (maxPixelDifferenceButton.isSelected()) {
                threshold = Integer.parseInt(maxPixelDifferenceThresholdTextField.getText());
            } else if (entropyButton.isSelected()) {
                threshold = Double.parseDouble(entropyThresholdTextField.getText());
            } else if (structuralSimilarityIndexButton.isSelected()) {
                threshold = Double.parseDouble(structuralSimilarityIndexThresholdTextField.getText());
            }
            String method;
            if (varianceButton.isSelected()) {
                method = "variance";
            } else if (meanAbsoluteDeviationButton.isSelected()) {
                method = "mad";
            } else if (maxPixelDifferenceButton.isSelected()) {
                method = "mpd";
            } else if (entropyButton.isSelected()) {
                method = "entropy";
            } else if (structuralSimilarityIndexButton.isSelected()) {
                method = "ssim";
            } else {
                method = null;
            }
            quadtree = new Quadtree(threshold, minBlockSize);

            compressionErrorMessageLabel.setText("Compressing...");
            compressionErrorMessageLabel.setStyle("-fx-text-fill:rgb(0, 0, 0);"); // Green color for success
            compressionErrorMessageLabel.setVisible(true);

            // Start timer
            long startTime = System.currentTimeMillis();

            try {
                quadtree.CreateQuadtree(ImageIO.read(inputImageFile), method); // Placeholder for error method
                System.out.println("Quadtree created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Stop timer
            long endTime = System.currentTimeMillis();
            double compressionTime = (endTime - startTime);

            compressedImageView.setImage(SwingFXUtils.toFXImage(quadtree.ImageFromQuadtree(), null)); 

            compressionErrorMessageLabel.setText("Success");
            compressionErrorMessageLabel.setStyle("-fx-text-fill: #1fc81f;"); 
            
            System.out.println("Compressed image created successfully.");

            // Update labels with compression details
            // compressedSizeLabel.setText(String.format("%.2f KB", (inputImageFile.length() / 1024.0))); // Placeholder for compressed size
            compressionTimeLabel.setText(String.format("%.2f s", compressionTime)); // Placeholder for compression time
            compressedSizeLabel.setText("0.0 KB"); // Placeholder for compressed size
            compressedNodeCountLabel.setText(String.valueOf(quadtree.getNodeCount())); // Placeholder for node count
            compressedTreeDepthLabel.setText(String.valueOf(quadtree.getDepth())); // Placeholder for tree depth

            // Set enabled labels
            compressionTimeLabel.setVisible(true);
            compressedSizeLabel.setVisible(true);
            compressionPercentageLabel.setVisible(true);
            compressedTreeDepthLabel.setVisible(true);
            compressedNodeCountLabel.setVisible(true);
        } else {
            compressionErrorMessageLabel.setStyle("-fx-text-fill: #c81f1f;");
            compressionErrorMessageLabel.setText("Please select an input image.");
            compressionErrorMessageLabel.setVisible(true);
        }
    }

    // Export compressed image button
    @FXML
    private void exportCompressed() {
        // Open file chooser dialog to select output location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Compressed Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setInitialFileName("compressed_image.png"); // Default file name
        File outputFile = fileChooser.showSaveDialog(null);
        if (outputFile != null) {
            // Save the compressed image to the selected location
            try {
                // Assuming quadtree.ImageFromQuadtree() returns a BufferedImage
                // and compressedImageFile is the file to save the image to
                compressedImageFile = outputFile;
                ImageIO.write(quadtree.ImageFromQuadtree(), "png", compressedImageFile); // Placeholder for output file
                // Use SwingFXUtils to convert the image to a JavaFX Image
                // and then write it to the output file
                // ImageIO.write(SwingFXUtils.fromFXImage(compressedImageView.getImage(), null), "png", compressedImageFile);
                compressionErrorMessageLabel.setStyle("-fx-text-fill: #1fc81f;");
                compressionErrorMessageLabel.setText("Compressed image exported successfully.");
                compressionErrorMessageLabel.setVisible(true);
            } catch (IOException e) {
                compressionErrorMessageLabel.setStyle("-fx-text-fill: #c81f1f;");
                compressionErrorMessageLabel.setText("Failed to export compressed image.");
                compressionErrorMessageLabel.setVisible(true);
            }
        }
    }

    // Export GIF button
    @FXML
    private void exportGif() {
        // TODO: Implement GIF export logic
        
        // Open file chooser dialog to select output location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export GIF");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("GIF Files", "*.gif"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setInitialFileName("compressed_image.gif"); // Default file name
        File outputFile = fileChooser.showSaveDialog(null);
        if (outputFile != null) {
            // Save the compressed image as a GIF to the selected location
            try {
                ImageIO.write(ImageIO.read(compressedImageFile), "gif", outputFile);
                compressionErrorMessageLabel.setStyle("-fx-text-fill: #1fc81f;");
                compressionErrorMessageLabel.setText("GIF exported successfully.");
                compressionErrorMessageLabel.setVisible(true);
            } catch (IOException e) {
                compressionErrorMessageLabel.setStyle("-fx-text-fill: #c81f1f;");
                compressionErrorMessageLabel.setText("Failed to export GIF.");
                compressionErrorMessageLabel.setVisible(true);
            }
        }
    }

    // Error measurement methods
    @FXML
    private void variance() {
        varianceThresholdTextField.setDisable(false);
        meanAbsoluteDeviationThresholdTextField.setDisable(true);
        maxPixelDifferenceThresholdTextField.setDisable(true);
        entropyThresholdTextField.setDisable(true);
        structuralSimilarityIndexThresholdTextField.setDisable(true);

        checkCompressButtonDisable();
    }

    @FXML
    private void meanAbsoluteDeviation() {
        varianceThresholdTextField.setDisable(true);
        meanAbsoluteDeviationThresholdTextField.setDisable(false);
        maxPixelDifferenceThresholdTextField.setDisable(true);
        entropyThresholdTextField.setDisable(true);
        structuralSimilarityIndexThresholdTextField.setDisable(true);

        checkCompressButtonDisable();
    }

    @FXML
    private void maxPixelDifference() {
        varianceThresholdTextField.setDisable(true);
        meanAbsoluteDeviationThresholdTextField.setDisable(true);
        maxPixelDifferenceThresholdTextField.setDisable(false);
        entropyThresholdTextField.setDisable(true);
        structuralSimilarityIndexThresholdTextField.setDisable(true);

        checkCompressButtonDisable();
    }

    @FXML
    private void entropy() {
        varianceThresholdTextField.setDisable(true);
        meanAbsoluteDeviationThresholdTextField.setDisable(true);
        maxPixelDifferenceThresholdTextField.setDisable(true);
        entropyThresholdTextField.setDisable(false);
        structuralSimilarityIndexThresholdTextField.setDisable(true);

        checkCompressButtonDisable();
    }

    @FXML
    private void structuralSimilarityIndex() {
        varianceThresholdTextField.setDisable(true);
        meanAbsoluteDeviationThresholdTextField.setDisable(true);
        maxPixelDifferenceThresholdTextField.setDisable(true);
        entropyThresholdTextField.setDisable(true);
        structuralSimilarityIndexThresholdTextField.setDisable(false);

        checkCompressButtonDisable();
    }

    // Compression percentage
    @FXML
    private void compressionPercentageActive() {
        if (compressionPercentageActiveButton.isSelected()) {
            compressionPercentageTextField.setDisable(false);
            compressionPercentageSlider.setDisable(false);

            // Set Error measurement method radio buttons disable
            varianceButton.setDisable(true);
            meanAbsoluteDeviationButton.setDisable(true);
            maxPixelDifferenceButton.setDisable(true);
            entropyButton.setDisable(true);
            structuralSimilarityIndexButton.setDisable(true);
    
            // Set Error measurement method text fields disable
            varianceThresholdTextField.setDisable(true);
            meanAbsoluteDeviationThresholdTextField.setDisable(true);
            maxPixelDifferenceThresholdTextField.setDisable(true);
            entropyThresholdTextField.setDisable(true);
            structuralSimilarityIndexThresholdTextField.setDisable(true);
    
            // Set Minimum block size text field disable
            minimumBlockSizeTextField.setDisable(true);

            // Set Compression button enable
            checkCompressButtonDisable();
        } else {
            compressionPercentageTextField.setDisable(true);
            compressionPercentageSlider.setDisable(true);

            // Set Error measurement method radio buttons disable
            varianceButton.setDisable(false);
            meanAbsoluteDeviationButton.setDisable(false);
            maxPixelDifferenceButton.setDisable(false);
            entropyButton.setDisable(false);
            structuralSimilarityIndexButton.setDisable(false);  

            // Set Error measurement method text fields disable
            varianceThresholdTextField.setDisable(true);
            meanAbsoluteDeviationThresholdTextField.setDisable(true);
            maxPixelDifferenceThresholdTextField.setDisable(true);
            entropyThresholdTextField.setDisable(true);
            structuralSimilarityIndexThresholdTextField.setDisable(true);

            // Set Minimum block size text field disable
            minimumBlockSizeTextField.setDisable(false);

            // Set Compression button disable
            checkCompressButtonDisable();
        }
    }

    @FXML
    private void compressionPercentageTextField() {
        double value = Double.parseDouble(compressionPercentageTextField.getText());
        if (value < 0) {
            value = 0;
        } else if (value > 100) {
            value = 100;
        }
        compressionPercentageTextField.setText(String.valueOf((int) value));
        compressionPercentageSlider.setValue(value);
        compressionPercentageTextField.positionCaret(compressionPercentageTextField.getText().length());
        checkCompressButtonDisable();
    }

    @FXML
    private void compressionPercentageSlider() {
        compressionPercentageTextField.setText(String.valueOf((int) compressionPercentageSlider.getValue()));
        checkCompressButtonDisable();
    }

    // ========================= HELPERS =========================
    @FXML
    private void checkCompressButtonDisable() {
        try {
            compressionErrorMessageLabel.setVisible(false);
            if (compressionPercentageActiveButton.isSelected()) {
                if (Double.parseDouble((compressionPercentageTextField.getText())) == 0.0) {
                    compressButton.setDisable(true);
                } else {
                    compressButton.setDisable(false);
                }
            } else {
                boolean thresholdValid = false;

                if (varianceButton.isSelected()) {
                    varianceThresholdTextField.setDisable(false);
                    thresholdValid = !varianceThresholdTextField.getText().isEmpty() && Double.parseDouble(varianceThresholdTextField.getText()) >= 0.0;
                } else if (meanAbsoluteDeviationButton.isSelected()) {
                    meanAbsoluteDeviationThresholdTextField.setDisable(false);
                    thresholdValid = !meanAbsoluteDeviationThresholdTextField.getText().isEmpty() && Double.parseDouble(meanAbsoluteDeviationThresholdTextField.getText()) >= 0.0;
                } else if (maxPixelDifferenceButton.isSelected()) {
                    maxPixelDifferenceThresholdTextField.setDisable(false);
                    thresholdValid = !maxPixelDifferenceThresholdTextField.getText().isEmpty() && Integer.parseInt(maxPixelDifferenceThresholdTextField.getText()) >= 0;
                } else if (entropyButton.isSelected()) {
                    entropyThresholdTextField.setDisable(false);
                    thresholdValid = !entropyThresholdTextField.getText().isEmpty() && Double.parseDouble(entropyThresholdTextField.getText()) >= 0.0;
                } else if (structuralSimilarityIndexButton.isSelected()) {
                    structuralSimilarityIndexThresholdTextField.setDisable(false);
                    thresholdValid = !structuralSimilarityIndexThresholdTextField.getText().isEmpty() && Double.parseDouble(structuralSimilarityIndexThresholdTextField.getText()) >= 0.0;
                }
    
                if (minimumBlockSizeTextField.getText().isEmpty() || Integer.parseInt(minimumBlockSizeTextField.getText()) < 1) {
                    compressButton.setDisable(true);
                } else {
                    compressButton.setDisable(!thresholdValid);
                }
            }
        } catch (NumberFormatException e) {
            compressButton.setDisable(true);
            compressionErrorMessageLabel.setStyle("-fx-text-fill: #c81f1f;");
            compressionErrorMessageLabel.setText("Please enter a valid number.");
            compressionErrorMessageLabel.setVisible(true);
        }
    }
}

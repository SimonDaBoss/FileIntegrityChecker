package com.cis256.fileintegrity;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Main JavaFX Application for File Integrity Checker
 * CIS 256 Cybersecurity Project
 * 
 * This application allows users to:
 * 1. Upload a file and compare its hash to a known hash
 * 2. Compare hashes of two different files
 */
public class FileIntegrityChecker extends Application {
    
    private Stage primaryStage;
    private HashManager hashManager;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.hashManager = new HashManager();
        primaryStage.setTitle("File Integrity Checker - SHA-256");
        
        // Create TabPane
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Tab 1: File Integrity Checker with HashTable
        Tab tab1 = new Tab("Integrity Checker (HashTable)");
        tab1.setContent(createHashComparisonTab());
        
        // Tab 2: Two File Comparison
        Tab tab2 = new Tab("File vs File Comparison");
        tab2.setContent(createFileComparisonTab());
        
        tabPane.getTabs().addAll(tab1, tab2);
        
        // Create scene
        Scene scene = new Scene(tabPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Creates the Hash Comparison tab where user uploads a file and it's automatically
     * checked against the stored hash table (HashMap data structure)
     */
    private VBox createHashComparisonTab() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.TOP_CENTER);
        
        // Title
        Label titleLabel = new Label("File Integrity Checker");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label descLabel = new Label("Select a file to check its integrity using the HashTable");
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        
        // File selection section
        HBox fileSection = new HBox(10);
        fileSection.setAlignment(Pos.CENTER);
        
        TextField filePathField = new TextField();
        filePathField.setPromptText("No file selected");
        filePathField.setEditable(false);
        filePathField.setPrefWidth(500);
        
        Button selectFileBtn = new Button("Select File to Check");
        selectFileBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        
        fileSection.getChildren().addAll(filePathField, selectFileBtn);
        
        // Status label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Current hash display
        Label currentHashLabel = new Label("Current File Hash:");
        currentHashLabel.setStyle("-fx-font-weight: bold;");
        
        TextArea currentHashArea = new TextArea();
        currentHashArea.setEditable(false);
        currentHashArea.setPrefRowCount(2);
        currentHashArea.setWrapText(true);
        currentHashArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        
        // Stored hash display (if exists)
        Label storedHashLabel = new Label("Stored Hash (from HashTable):");
        storedHashLabel.setStyle("-fx-font-weight: bold;");
        storedHashLabel.setVisible(false);
        
        TextArea storedHashArea = new TextArea();
        storedHashArea.setEditable(false);
        storedHashArea.setPrefRowCount(2);
        storedHashArea.setWrapText(true);
        storedHashArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        storedHashArea.setVisible(false);
        
        // Result label
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        resultLabel.setWrapText(true);
        resultLabel.setMaxWidth(700);
        resultLabel.setAlignment(Pos.CENTER);
        
        // Action buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button updateHashBtn = new Button("Update Stored Hash");
        updateHashBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        updateHashBtn.setVisible(false);
        
        Button viewAllBtn = new Button("View All Stored Hashes");
        viewAllBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Button clearAllBtn = new Button("Clear Hash Table");
        clearAllBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        
        buttonBox.getChildren().addAll(updateHashBtn, viewAllBtn, clearAllBtn);
        
        // HashTable info
        Label hashTableInfo = new Label("Files tracked in HashTable: " + hashManager.getFileCount());
        hashTableInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        
        // File selection logic
        selectFileBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File to Check Integrity");
            File file = fileChooser.showOpenDialog(primaryStage);
            
            if (file != null) {
                String filePath = file.getAbsolutePath();
                filePathField.setText(filePath);
                resultLabel.setText("");
                updateHashBtn.setVisible(false);
                storedHashLabel.setVisible(false);
                storedHashArea.setVisible(false);
                
                try {
                    // Compute current hash
                    String currentHash = HashUtility.computeFileHash(filePath);
                    currentHashArea.setText(currentHash);
                    
                    // Check if file is recognized in hash table
                    if (hashManager.isFileRecognized(filePath)) {
                        // File is in hash table - compare hashes
                        String storedHash = hashManager.getStoredHash(filePath);
                        storedHashArea.setText(storedHash);
                        storedHashLabel.setVisible(true);
                        storedHashArea.setVisible(true);
                        
                        statusLabel.setText("Status: File is RECOGNIZED in HashTable");
                        statusLabel.setTextFill(Color.BLUE);
                        
                        if (HashUtility.compareHashes(currentHash, storedHash)) {
                            resultLabel.setText("✓ FILE INTEGRITY VERIFIED!\nThe file matches the stored hash. No modifications detected.");
                            resultLabel.setTextFill(Color.GREEN);
                        } else {
                            resultLabel.setText("⚠ WARNING: FILE HAS BEEN MODIFIED!\nThe current hash does NOT match the stored hash.");
                            resultLabel.setTextFill(Color.RED);
                            updateHashBtn.setVisible(true);
                        }
                    } else {
                        // File is NOT in hash table - save it
                        hashManager.storeHash(filePath, currentHash);
                        statusLabel.setText("Status: File is NEW - Hash saved to HashTable");
                        statusLabel.setTextFill(Color.ORANGE);
                        resultLabel.setText("✓ NEW FILE REGISTERED!\nThis file has been added to the HashTable.\nFuture checks will verify against this hash.");
                        resultLabel.setTextFill(Color.GREEN);
                        hashTableInfo.setText("Files tracked in HashTable: " + hashManager.getFileCount());
                    }
                    
                } catch (IOException | NoSuchAlgorithmException ex) {
                    currentHashArea.setText("Error computing hash: " + ex.getMessage());
                    showError("Error", "Failed to compute hash: " + ex.getMessage());
                }
            }
        });
        
        // Update hash button logic (when file has been modified)
        updateHashBtn.setOnAction(e -> {
            String filePath = filePathField.getText();
            String newHash = currentHashArea.getText();
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Update Hash");
            confirm.setHeaderText("Update stored hash for this file?");
            confirm.setContentText("This will replace the old hash with the new one in the HashTable.");
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    hashManager.updateHash(filePath, newHash);
                    storedHashArea.setText(newHash);
                    resultLabel.setText("✓ HASH UPDATED!\nThe HashTable now stores the new hash for this file.");
                    resultLabel.setTextFill(Color.GREEN);
                    updateHashBtn.setVisible(false);
                }
            });
        });
        
        // View all button logic
        viewAllBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hash Table Contents");
            alert.setHeaderText("All Stored File Hashes");
            
            TextArea textArea = new TextArea(hashManager.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            textArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11px;");
            
            alert.getDialogPane().setContent(textArea);
            alert.getDialogPane().setPrefSize(700, 500);
            alert.showAndWait();
        });
        
        // Clear all button logic
        clearAllBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Clear Hash Table");
            confirm.setHeaderText("Clear all stored hashes?");
            confirm.setContentText("This will remove all " + hashManager.getFileCount() + " entries from the HashTable.\nThis action cannot be undone.");
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    hashManager.clearAll();
                    hashTableInfo.setText("Files tracked in HashTable: 0");
                    resultLabel.setText("HashTable cleared successfully!");
                    resultLabel.setTextFill(Color.GRAY);
                    filePathField.clear();
                    currentHashArea.clear();
                    storedHashArea.clear();
                    statusLabel.setText("");
                    storedHashLabel.setVisible(false);
                    storedHashArea.setVisible(false);
                    updateHashBtn.setVisible(false);
                }
            });
        });
        
        container.getChildren().addAll(
            titleLabel, descLabel,
            hashTableInfo,
            new Separator(),
            fileSection,
            statusLabel,
            currentHashLabel, currentHashArea,
            storedHashLabel, storedHashArea,
            resultLabel,
            buttonBox
        );
        
        return container;
    }
    
    /**
     * Creates the File Comparison tab where user selects two files to compare their hashes
     */
    private VBox createFileComparisonTab() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.TOP_CENTER);
        
        // Title
        Label titleLabel = new Label("Compare Two Files");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label descLabel = new Label("Select two files to compare their SHA-256 hashes");
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        
        // File 1 section
        VBox file1Section = new VBox(10);
        Label file1Label = new Label("File 1:");
        file1Label.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        HBox file1Box = new HBox(10);
        file1Box.setAlignment(Pos.CENTER_LEFT);
        
        TextField file1PathField = new TextField();
        file1PathField.setPromptText("No file selected");
        file1PathField.setEditable(false);
        file1PathField.setPrefWidth(400);
        
        Button selectFile1Btn = new Button("Select File 1");
        selectFile1Btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        
        file1Box.getChildren().addAll(file1PathField, selectFile1Btn);
        
        TextArea hash1Area = new TextArea();
        hash1Area.setEditable(false);
        hash1Area.setPrefRowCount(2);
        hash1Area.setWrapText(true);
        hash1Area.setPromptText("Hash will appear here...");
        hash1Area.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        
        file1Section.getChildren().addAll(file1Label, file1Box, hash1Area);
        
        // File 2 section
        VBox file2Section = new VBox(10);
        Label file2Label = new Label("File 2:");
        file2Label.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        HBox file2Box = new HBox(10);
        file2Box.setAlignment(Pos.CENTER_LEFT);
        
        TextField file2PathField = new TextField();
        file2PathField.setPromptText("No file selected");
        file2PathField.setEditable(false);
        file2PathField.setPrefWidth(400);
        
        Button selectFile2Btn = new Button("Select File 2");
        selectFile2Btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        
        file2Box.getChildren().addAll(file2PathField, selectFile2Btn);
        
        TextArea hash2Area = new TextArea();
        hash2Area.setEditable(false);
        hash2Area.setPrefRowCount(2);
        hash2Area.setWrapText(true);
        hash2Area.setPromptText("Hash will appear here...");
        hash2Area.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        
        file2Section.getChildren().addAll(file2Label, file2Box, hash2Area);
        
        // Compare button
        Button compareBtn = new Button("Compare Files");
        compareBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        compareBtn.setPrefWidth(200);
        compareBtn.setDisable(true);
        
        // Result label
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // File 1 selection logic
        selectFile1Btn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select First File");
            File file = fileChooser.showOpenDialog(primaryStage);
            
            if (file != null) {
                file1PathField.setText(file.getAbsolutePath());
                resultLabel.setText("");
                
                try {
                    String hash = HashUtility.computeFileHash(file.getAbsolutePath());
                    hash1Area.setText(hash);
                    
                    if (!hash2Area.getText().isEmpty()) {
                        compareBtn.setDisable(false);
                    }
                } catch (IOException | NoSuchAlgorithmException ex) {
                    hash1Area.setText("Error computing hash: " + ex.getMessage());
                    showError("Error", "Failed to compute hash: " + ex.getMessage());
                }
            }
        });
        
        // File 2 selection logic
        selectFile2Btn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Second File");
            File file = fileChooser.showOpenDialog(primaryStage);
            
            if (file != null) {
                file2PathField.setText(file.getAbsolutePath());
                resultLabel.setText("");
                
                try {
                    String hash = HashUtility.computeFileHash(file.getAbsolutePath());
                    hash2Area.setText(hash);
                    
                    if (!hash1Area.getText().isEmpty()) {
                        compareBtn.setDisable(false);
                    }
                } catch (IOException | NoSuchAlgorithmException ex) {
                    hash2Area.setText("Error computing hash: " + ex.getMessage());
                    showError("Error", "Failed to compute hash: " + ex.getMessage());
                }
            }
        });
        
        // Compare button logic
        compareBtn.setOnAction(e -> {
            String hash1 = hash1Area.getText().trim();
            String hash2 = hash2Area.getText().trim();
            
            boolean match = HashUtility.compareHashes(hash1, hash2);
            
            if (match) {
                resultLabel.setText("✓ FILES ARE IDENTICAL - Hashes match!");
                resultLabel.setTextFill(Color.GREEN);
            } else {
                resultLabel.setText("✗ FILES ARE DIFFERENT - Hashes do not match!");
                resultLabel.setTextFill(Color.RED);
            }
        });
        
        container.getChildren().addAll(
            titleLabel, descLabel,
            new Separator(),
            file1Section,
            new Separator(),
            file2Section,
            compareBtn,
            resultLabel
        );
        
        return container;
    }
    
    /**
     * Shows an error dialog
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}


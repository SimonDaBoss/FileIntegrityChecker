package com.cis256.fileintegrity;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages a HashTable (HashMap) of file paths and their SHA-256 hashes.
 * This class demonstrates the use of the HashMap data structure for storing
 * and retrieving file integrity information.
 * 
 * The HashTable is persisted to disk so hashes are remembered between sessions.
 */
public class HashManager {
    
    // HashMap data structure - Key: file path, Value: SHA-256 hash
    private HashMap<String, String> hashTable;
    
    // File to persist the hash table
    private static final String HASH_STORAGE_FILE = "file_hashes.dat";
    
    /**
     * Constructor - initializes the HashMap and loads existing hashes from disk
     */
    public HashManager() {
        hashTable = new HashMap<>();
        loadHashTable();
    }
    
    /**
     * Checks if a file path is already in the hash table (i.e., file is "recognized")
     * 
     * @param filePath The file path to check
     * @return true if file is in the hash table, false otherwise
     */
    public boolean isFileRecognized(String filePath) {
        return hashTable.containsKey(filePath);
    }
    
    /**
     * Gets the stored hash for a given file path
     * 
     * @param filePath The file path
     * @return The stored SHA-256 hash, or null if not found
     */
    public String getStoredHash(String filePath) {
        return hashTable.get(filePath);
    }
    
    /**
     * Stores a new file hash in the hash table and saves to disk
     * 
     * @param filePath The file path
     * @param hash The SHA-256 hash to store
     */
    public void storeHash(String filePath, String hash) {
        hashTable.put(filePath, hash);
        saveHashTable();
    }
    
    /**
     * Updates the hash for an existing file (when file has been modified)
     * 
     * @param filePath The file path
     * @param newHash The new SHA-256 hash
     */
    public void updateHash(String filePath, String newHash) {
        hashTable.put(filePath, newHash);
        saveHashTable();
    }
    
    /**
     * Removes a file from the hash table
     * 
     * @param filePath The file path to remove
     * @return true if removed, false if not found
     */
    public boolean removeHash(String filePath) {
        if (hashTable.remove(filePath) != null) {
            saveHashTable();
            return true;
        }
        return false;
    }
    
    /**
     * Gets all stored file paths
     * 
     * @return Array of all file paths in the hash table
     */
    public String[] getAllFilePaths() {
        return hashTable.keySet().toArray(new String[0]);
    }
    
    /**
     * Gets the number of files in the hash table
     * 
     * @return The size of the hash table
     */
    public int getFileCount() {
        return hashTable.size();
    }
    
    /**
     * Clears all entries from the hash table
     */
    public void clearAll() {
        hashTable.clear();
        saveHashTable();
    }
    
    /**
     * Saves the hash table to disk (serialization)
     */
    private void saveHashTable() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HASH_STORAGE_FILE))) {
            oos.writeObject(hashTable);
        } catch (IOException e) {
            System.err.println("Error saving hash table: " + e.getMessage());
        }
    }
    
    /**
     * Loads the hash table from disk (deserialization)
     */
    @SuppressWarnings("unchecked")
    private void loadHashTable() {
        File file = new File(HASH_STORAGE_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                hashTable = (HashMap<String, String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading hash table: " + e.getMessage());
                hashTable = new HashMap<>();
            }
        }
    }
    
    /**
     * Gets a string representation of the hash table for display
     * 
     * @return String representation showing all entries
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hash Table Contents (").append(hashTable.size()).append(" entries):\n");
        sb.append("=".repeat(60)).append("\n");
        
        if (hashTable.isEmpty()) {
            sb.append("(Empty - no files tracked yet)\n");
        } else {
            for (Map.Entry<String, String> entry : hashTable.entrySet()) {
                sb.append("File: ").append(entry.getKey()).append("\n");
                sb.append("Hash: ").append(entry.getValue()).append("\n");
                sb.append("-".repeat(60)).append("\n");
            }
        }
        
        return sb.toString();
    }
}


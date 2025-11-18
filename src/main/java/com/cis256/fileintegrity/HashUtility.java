package com.cis256.fileintegrity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for computing SHA-256 hashes of files.
 * 
 * SHA-256 (Secure Hash Algorithm 256-bit) is a cryptographic hash function that:
 * - Takes any input file and produces a unique 256-bit (64 character hex) output
 * - Processes file bytes in 512-bit blocks
 * - Performs rounds of bitwise operations and modular arithmetic
 * - Is a one-way function (cannot reverse the hash to get original file)
 * - Changes completely with even a single bit change in the file
 * - Works on any file type (.txt, .jpg, .pdf, .zip, .exe, etc.)
 */
public class HashUtility {
    
    private static final String HASH_ALGORITHM = "SHA-256";
    
    /**
     * Computes the SHA-256 hash of a file.
     * 
     * @param filePath The path to the file to hash
     * @return The SHA-256 hash as a 64-character hexadecimal string
     * @throws IOException If the file cannot be read
     * @throws NoSuchAlgorithmException If SHA-256 is not available (should never happen)
     */
    public static String computeFileHash(String filePath) throws IOException, NoSuchAlgorithmException {
        // Read all bytes from the file (works for any file type)
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        
        // Get SHA-256 MessageDigest instance
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        
        // Compute the hash
        byte[] hashBytes = digest.digest(fileBytes);
        
        // Convert byte array to hexadecimal string
        return bytesToHex(hashBytes);
    }
    
    /**
     * Computes the SHA-256 hash of a file using Path object.
     * 
     * @param path The path to the file to hash
     * @return The SHA-256 hash as a 64-character hexadecimal string
     * @throws IOException If the file cannot be read
     * @throws NoSuchAlgorithmException If SHA-256 is not available
     */
    public static String computeFileHash(Path path) throws IOException, NoSuchAlgorithmException {
        return computeFileHash(path.toString());
    }
    
    /**
     * Converts a byte array to a hexadecimal string.
     * 
     * @param bytes The byte array to convert
     * @return The hexadecimal string representation
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Compares two hash strings for equality (case-insensitive).
     * 
     * @param hash1 First hash to compare
     * @param hash2 Second hash to compare
     * @return true if hashes match, false otherwise
     */
    public static boolean compareHashes(String hash1, String hash2) {
        if (hash1 == null || hash2 == null) {
            return false;
        }
        return hash1.equalsIgnoreCase(hash2);
    }
    
    /**
     * Validates if a string is a valid SHA-256 hash (64 hexadecimal characters).
     * 
     * @param hash The hash string to validate
     * @return true if valid SHA-256 hash format, false otherwise
     */
    public static boolean isValidSHA256Hash(String hash) {
        if (hash == null) {
            return false;
        }
        // SHA-256 produces 64 hexadecimal characters
        return hash.matches("[a-fA-F0-9]{64}");
    }
}



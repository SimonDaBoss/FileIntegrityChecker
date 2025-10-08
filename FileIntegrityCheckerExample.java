import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

public class FileIntegrityCheckerExample {

    // computes hash using SHA-256
    public static String computeHash(String filePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        byte[] hashBytes = digest.digest(fileBytes);
        
        // convering byte to hex format
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // saves file hash to record
    public static void saveHash(String filePath, String hash, String recordFile) throws IOException {
        try (FileWriter writer = new FileWriter(recordFile, true)) {
            writer.write(filePath + ":" + hash + "\n");
        }
    }

    // loads stored hash into a map
    public static Map<String, String> loadHashes(String recordFile) throws IOException {
        Map<String, String> hashMap = new HashMap<>();
        File file = new File(recordFile);
        if (!file.exists()) return hashMap;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) hashMap.put(parts[0], parts[1]);
            }
        }
        return hashMap;
    }

    // compare current hash with stored hash, so verify integrity, saves new hash if no previous records
    public static void verifyFile(String filePath, String recordFile) throws Exception {
        Map<String, String> storedHashes = loadHashes(recordFile);
        String currentHash = computeHash(filePath);

        if (!storedHashes.containsKey(filePath)) {
            System.out.println("No previous record found. Saving new hash...");
            saveHash(filePath, currentHash, recordFile);
        } else if (storedHashes.get(filePath).equals(currentHash)) {
            System.out.println("File is unchanged.");
        } else {
            System.out.println("WARNING: File has been modified!");
        }
    }

    // test program in console for now
    public static void main(String[] args) throws Exception {
        String filePath = "test.txt"; // file we want to check integrity for
        String recordFile = "hashes.txt"; // saved hashes we have stored, this is good because this means we keep track of multiple files that have been checked for integrity

        verifyFile(filePath, recordFile);
        // first you MUST run this to create the hashes.txt file
        // then you can run it again to check integrity
        // you can test this by changing test.txt file and running again
    }
}

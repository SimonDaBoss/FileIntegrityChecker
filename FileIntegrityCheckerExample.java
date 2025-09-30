import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Scanner;

// Note: The class name has been changed from Main to FileIntegrityCheckerExample
// heres how to test this
// lets test it with test.txt
// first run in ur cmd, Get-FileHash -Path .\test.txt -Algorithm SHA256
// it should give u the hash
// then run java FileIntegrityCheckerExample.java test.txt <the hash u got>
// it should say MATCH
// if u change a char in the hash it should say MISMATCH
// you can test this by copying the file to a new file and changing a char...

public class FileIntegrityCheckerExample{
    public static void main(String[] args){
        try {
            if (args.length == 0) {
                runInteractive();
            } else if (args.length == 1) {
                Path p = Paths.get(args[0]);
                String hash = sha256(p);
                System.out.println(hash);
            } else {
                Path p = Paths.get(args[0]);
                String provided = args[1].toLowerCase(Locale.ROOT).trim();
                String actual = sha256(p);
                System.out.println(actual);
                if (actual.equals(provided)) {
                    System.out.println("MATCH");
                    System.exit(0);
                } else {
                    System.out.println("MISMATCH");
                    System.exit(2);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void runInteractive() throws IOException, NoSuchAlgorithmException {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter path to file: ");
            String path = sc.nextLine().trim();
            if (path.isEmpty()) {
                System.out.println("No path provided. Exiting.");
                return;
            }
            Path p = Paths.get(path);
            String hash = sha256(p);
            System.out.println("Computed SHA-256: " + hash);

            System.out.print("Enter checksum to verify (or press Enter to skip): ");
            String provided = sc.nextLine().trim();
            if (!provided.isEmpty()) {
                if (hash.equals(provided.toLowerCase(Locale.ROOT))) {
                    System.out.println("Verification: MATCH");
                } else {
                    System.out.println("Verification: MISMATCH");
                }
            }
        }
    }

    private static String sha256(Path p) throws IOException, NoSuchAlgorithmException {
        if (!Files.exists(p)) {
            throw new IOException("File not found: " + p.toString());
        }
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream is = Files.newInputStream(p);
             DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {
                // reading via DigestInputStream updates the digest
            }
        }
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
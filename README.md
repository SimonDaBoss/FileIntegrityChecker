# FileIntegrityChecker
A Java project that utilizes hashing, a cybersecurity project for CIS 256


This project will be utilizing SHA-256 "Secure Hash Algorithm 256-bit", one of the most widely used cryptographic hash functions. It takes any input file (no matter how big) and produces a unique 256-bit (64 chars) output called a HASH, or a digest.

**How it works**
1) The file’s bytes are processed in small chunks (512-bit blocks).
2) The algorithm performs rounds of bitwise operations and modular arithmetic.
3) The final 256-bit result is output as a hexadecimal string.
4) You don’t need to implement this yourself — Java’s MessageDigest class does it internally.


This is a one-way-function meaning that you can’t reverse the hash to get the original file, making it secure for integrity checks.

Even a single bit change in a file completely changes the hash.

SHA-256 hashing works on any file type, because it operates on the raw bytes, not on text or file format.

```byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));``` 
this line reads every byte from the file — regardless of whether it’s: .txt, .jpg, .pdf, .zip, and even a .exe

Then SHA-256 turns those bytes into a unique 256-bit digest.

⚠️
Current implementation, using .txt's across different operating systems, line endings (\n vs. \r\n) can change, which technically alters the hash. So if you check the same .txt file on Windows and then on macOS, it might appear “changed” even if the text looks the same.

But for binary files (images, PDFs, executables), the hashes will be consistent everywhere.

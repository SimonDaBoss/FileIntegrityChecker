# File Integrity Checker

**CIS 256 Cybersecurity Project | Data Structures: HashMap Implementation**

A JavaFX-based application that utilizes **SHA-256 hashing** and **HashMap data structure** for file integrity verification. This project demonstrates both cybersecurity concepts and data structures implementation by automatically storing and retrieving file hashes using a persistent HashMap.

## 📊 Data Structure: HashMap

This project prominently features the **HashMap data structure** to manage file hashes:

- **Data Structure**: `HashMap<String, String>`
- **Key**: File path (String)
- **Value**: SHA-256 hash (String)
- **Operations Implemented**:
  - `put(key, value)` - Store new file hash
  - `get(key)` - Retrieve stored hash
  - `containsKey(key)` - Check if file is tracked
  - `remove(key)` - Remove file from tracking
  - `clear()` - Clear all entries
  - `size()` - Get number of tracked files
- **Persistence**: HashMap is serialized to `file_hashes.dat` for long-term storage
- **Time Complexity**: O(1) average case for all operations

### Why HashMap?

HashMap provides **constant-time performance** for basic operations (get and put), making it ideal for quickly checking whether a file has been seen before and retrieving its stored hash.

## 🔐 About SHA-256

SHA-256 (Secure Hash Algorithm 256-bit) is one of the most widely used cryptographic hash functions. 

### How It Works

1. **File Processing**: The file's bytes are read and processed in small chunks (512-bit blocks)
2. **Cryptographic Operations**: The algorithm performs rounds of bitwise operations and modular arithmetic
3. **Hash Output**: The final 256-bit result is output as a 64-character hexadecimal string

### Key Features of SHA-256

- ✅ **One-Way Function**: Cannot reverse the hash to get the original file (secure for integrity checks)
- ✅ **Deterministic**: Same input always produces the same hash
- ✅ **Avalanche Effect**: Even a single bit change completely changes the hash
- ✅ **Universal**: Works on any file type (.txt, .jpg, .pdf, .zip, .exe, etc.) because it operates on raw bytes
- ✅ **Collision Resistant**: Extremely unlikely for two different files to have the same hash

## 🚀 Features

This application provides two modes of operation:

### 1. Integrity Checker (HashTable) - Demonstrates HashMap Data Structure
- **Automatic Hash Management**: Uses a **HashMap data structure** to store file hashes
- **First-time files**: Automatically saved to the HashTable (no manual input needed!)
- **Recognized files**: Compares current hash against stored hash
- **Integrity Verification**: Detects if files have been modified
- **Persistent Storage**: HashTable is saved to disk (`file_hashes.dat`)
- **View All Hashes**: See all entries in the HashTable
- **Update Hashes**: Update stored hashes when files are legitimately modified
- **Clear HashTable**: Reset and start fresh

### 2. File vs File Comparison
- Select two files to compare
- Automatically computes SHA-256 hashes for both files
- Determines if the files are identical or different

## 🛠️ Technical Implementation

### Components

1. **HashManager.java** - HashMap data structure implementation
   - **HashMap<String, String>**: Stores file paths as keys, SHA-256 hashes as values
   - Persistent storage using Java serialization (`file_hashes.dat`)
   - CRUD operations: add, get, update, remove, clear
   - Demonstrates data structures concepts for CIS class requirements

2. **HashUtility.java** - Core utility class for SHA-256 operations
   - Uses Java's `MessageDigest` class for SHA-256 implementation
   - Reads file bytes using `Files.readAllBytes()`
   - Converts hash bytes to hexadecimal string representation

3. **FileIntegrityChecker.java** - Main JavaFX application
   - Tabbed interface for different comparison modes
   - File selection dialogs
   - Real-time hash computation
   - Integration with HashManager for automatic storage/retrieval
   - Visual feedback for match/mismatch results

4. **styles.css** - Modern UI styling

## 📋 Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: For dependency management and building
- **JavaFX**: Version 21.0.1 (automatically handled by Maven)

## 🏗️ Running the Project

### ⭐ Highly Recommended for running our JavaFX File Integrity Checker (IntelliJ)

1. Download **IntelliJ IDEA Community Edition** (FREE): https://www.jetbrains.com/idea/download/
2. Open the `FileIntegrityChecker` folder in IntelliJ
3. Wait for Maven sync to complete (automatic)
4. Right-click `FileIntegrityChecker.java` → **Run**
5. Done! 🎉

### Using Other IDEs

**Eclipse / VS Code / NetBeans** - All work great!
- Import as Maven project
- Let the IDE download dependencies
- Right-click `FileIntegrityChecker.java` and Run

👉 **See SETUP_GUIDE.md for detailed instructions for each IDE**

### Using Maven (Command Line) - u must have Maven for this to work

```bash
# Compile and run
mvn clean javafx:run

# Package as JAR
mvn clean package
```

### Using Batch Scripts (Windows)

```bash
# Run with Maven (if installed)
run.bat

# Manual compilation (if JavaFX SDK downloaded)
compile.bat
run-manual.bat
```

**Note**: If Maven commands don't work, just use IntelliJ IDEA since it makes the setup easy

## 📖 Usage Guide

### Integrity Checker Mode (HashTable)

**How it works:**
- The application uses a **HashMap** to automatically track files
- First time you check a file → Hash is saved to the HashTable
- Next time you check the same file → Hash is compared automatically
- No manual copying/pasting needed!

**Steps:**

1. Click **"Select File to Check"** to choose a file
2. The SHA-256 hash is automatically computed

**Three possible results:**

**A) NEW FILE (Not in HashTable yet)**
   - Status: "File is NEW - Hash saved to HashTable"
   - The hash is automatically saved
   - Future checks will verify against this hash

**B) FILE VERIFIED (Hash matches stored hash)**
   - Status: "File is RECOGNIZED in HashTable"
   - Result: ✓ **FILE INTEGRITY VERIFIED** (green)
   - File has not been modified

**C) FILE MODIFIED (Hash doesn't match)**
   - Status: "File is RECOGNIZED in HashTable"
   - Result: ⚠ **WARNING: FILE HAS BEEN MODIFIED** (red)
   - Current hash differs from stored hash
   - Option to update the stored hash if modification was intentional

**Additional Features:**
- **View All Stored Hashes**: See all files in the HashTable
- **Update Stored Hash**: Update hash when file is legitimately modified
- **Clear Hash Table**: Remove all entries and start fresh

### File vs File Mode

1. Click **"Select File 1"** to choose the first file
2. Click **"Select File 2"** to choose the second file
3. Hashes are computed automatically for both files
4. Click **"Compare Files"** to see results
5. Result will show:
   - ✓ **FILES ARE IDENTICAL** - Hashes match (green)
   - ✗ **FILES ARE DIFFERENT** - Hashes don't match (red)

## 🎯 Use Cases

1. **Verify Downloads**: Check if a downloaded file matches the publisher's hash
2. **Detect Tampering**: Verify files haven't been modified or corrupted
3. **Backup Verification**: Confirm backup copies are identical to originals
4. **Duplicate Detection**: Identify identical files with different names
5. **Security Auditing**: Ensure system files haven't been altered

## 📁 Project Structure

```
FileIntegrityChecker/
├── pom.xml
├── README.md
├── file_hashes.dat                    # HashTable storage (auto-generated)
└── src/
    └── main/
        ├── java/
        │   ├── module-info.java
        │   └── com/cis256/fileintegrity/
        │       ├── FileIntegrityChecker.java  # Main JavaFX app
        │       ├── HashManager.java           # HashMap implementation
        │       └── HashUtility.java           # SHA-256 utility
        └── resources/
            └── styles.css
```

## 🔬 Example Scenarios

### Scenario 1: Monitoring Important System Files

1. Select an important file (e.g., config file, executable)
2. First time: Hash is saved to HashTable
3. Later: Check the file again
4. If hash matches → File is intact
5. If hash differs → File has been tampered with or corrupted

### Scenario 2: Checking for File Duplication

1. Use File vs File tab
2. Select two suspected duplicate files
3. If hashes match, files are identical (can safely delete one)
4. If hashes differ, files are different despite similar names

### Scenario 3: Backup Integrity

1. Compare original file with backup copy
2. If hashes match, backup is perfect
3. If hashes differ, backup may be corrupted

## 🎓 Educational Value

### Data Structures Concepts Demonstrated:
- **HashMap implementation**: Key-value storage (file path → hash)
- **Persistent data structures**: Serialization to disk
- **CRUD operations**: Create, Read, Update, Delete on HashTable
- **Hash table efficiency**: O(1) lookup time for file recognition

### Cybersecurity Concepts:
- Cryptographic hash functions (SHA-256)
- File integrity verification
- Tamper detection
- One-way functions

### Software Engineering:
- JavaFX GUI development
- File I/O operations in Java
- Object-oriented design
- Maven project structure
- Separation of concerns (HashManager, HashUtility, UI)

## 📝 Notes

### Data Structures
- HashMap provides O(1) lookup performance for file recognition
- Persistent storage ensures hashes survive application restarts
- Demonstrates practical use of hash tables in real-world applications

### Cryptography
- SHA-256 is cryptographically secure but not suitable for password storage (use bcrypt/argon2 instead)
- Hash computation time depends on file size
- The application uses Java's built-in MessageDigest implementation
- All file types are supported (text, images, executables, archives, etc.)

### Goals Met
- ✅ **CIS 256**: HashMap Data Structure implementation with CRUD operations
- ✅ **Cybersecurity Theme**: SHA-256 hashing and integrity verification
- ✅ **OOP**: Clean OOP design, GUI development

## 👨‍💻 Author

Created for CIS 256 - Data Structures at CSM
by Eryx, Kirill, Dylan, Simon

**CAUTION:**: A single bit change in a file will completely change its SHA-256 hash.

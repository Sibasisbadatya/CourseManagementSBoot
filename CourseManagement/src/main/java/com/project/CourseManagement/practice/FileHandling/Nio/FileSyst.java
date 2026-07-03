package com.project.CourseManagement.practice.FileHandling.Nio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileSyst {
    public static void main(String[] args) throws IOException {
        FileSystem fileSystem = FileSystems.getDefault();
//        FileSystems gives you access to the file system of your machine (like Windows, Linux, Mac) so
//        Java can work with files and folders in a modern way.
//        What FileSystems Does
//
//        FileSystems is mainly used to:
//
//        Get the default file system
//        Create/access paths
//        Work with custom file systems (like ZIP files)
//        Understand how files are organized on the OS

//        1. Get Default File System
//        This is the most common use.
//        FileSystem fs = FileSystems.getDefault();
//        This gives Java access to your system’s default file system.
//        Example:
//        Windows → C:\, D:\
//        Linux/Mac → /
//        So Java now knows how your OS organizes files.

        String separator = fileSystem.getSeparator();
        System.out.println("separator"+separator);

//        What it means
//
//        Every operating system separates folders in a path using a specific symbol.
//        Examples:
//        Windows → \
//        Linux/Mac → /
//
//        fileSystem.getSeparator() returns that symbol.
//                So this line means:
//        “Tell me what character this file system uses to separate folders in a path.”


        Iterable<Path> rootDirectories = fileSystem.getRootDirectories();
        rootDirectories.forEach((path -> System.out.println(path)));
//        A root directory is the highest level in a file system.
//                It is the starting point of all paths.



//        In Java NIO, Files is the main utility class used to perform operations on files and directories.
//        It belongs to: java.nio.file.Files

//        If Path is the address of a file, then Files is the class that does something with that file.
//
//        Think of it like:
//                Path = where the file is
//                Files = what to do with it
//        So Files is used to:
//
//        create files
//        read files
//        write files
//        copy files
//        move files
//        delete files
//        check if files exist
//        work with folders

//        What Files Does ?
//
//        Files is a utility class with static methods.
//        That means you don’t create an object like: new Files();   // ❌ not allowed
//        Instead, you directly call methods like: Files.readString(path);

//        Some Important Methods
//        ---------------------------------
//        1.Create File
        Path path = Paths.get("C:\\Users\\sibasis.badatya\\Documents\\CourseManagement\\CourseManagementSBoot\\CourseManagement\\src\\main\\java\\com\\project\\CourseManagement\\practice\\FileHandling\\Nio\\Test.txt"); //Path is an interface in java.nio.file.
        // It represents the location of a file or directory in the file system. used for Path operations
//        Files.createFile(path);
//        Paths is a utility class in java.nio.file.
//                It is mainly used to create Path objects.
//        Creates a new file physically. used for Path Creation.

//        2. Create Directory
//        Path dir = Paths.get("C://Sibasis");
//        Files.createDirectory(dir);

//        3. Check if File Exists
//        Files.exists(path);

//        4. Read File Content
        String content = Files.readString(path);
        System.out.println("Content is "+content);

//        5. Write to File
//        Files.writeString(path, "Hello World");

//        6. Copy File
        Path destination = Path.of("C:\\Users\\sibasis.badatya\\Documents\\CourseManagement\\CourseManagementSBoot\\CourseManagement\\src\\main\\java\\com\\project\\CourseManagement\\practice\\FileHandling\\Test.txt");
//        Files.copy(path, destination);//it fails if destintaion file already exists

//        Read File Line by Line
        List<String> lines = Files.readAllLines(path);
        lines.forEach((line)-> System.out.println("Line "+ line));

//        Stream File Lines
        Files.lines(path).forEach(System.out::println);


        String testPath = "C:\\Users\\sibasis.badatya\\Documents\\CourseManagement\\CourseManagementSBoot\\CourseManagement\\src\\main\\java\\com\\project\\CourseManagement\\practice\\FileHandling\\Nio\\Test.txt";



//        buffered readers and writter
//        ---------------------------------------------------------
//        How they works
//        Internal Working of BufferedReader
//        Buffered Reader internally created an character array char[] buffer = new char[8192];
//        by default 8kb

//        What happens during read
//        Suppose file contains: HELLOWORLD
//        and you call:
//        reader.read();
//        Step 1: Buffer is empty
//        So BufferedReader asks underlying stream:
//        “Give me a chunk of characters.”
//        Underlying FileReader reads a block (by default data of 8kb) from disk into buffer.
//                Now memory buffer becomes:[H E L L O W O R L D]
//        Step 2: Return one char

//        Now read() returns:H
//        It does not go to disk.
//        It just returns buffer[0].
//        Then: E L L O
//        Step 4: Buffer exhausted  When internal pointer reaches end:

//        nextChar == nChars
//        Then it refills buffer from disk again.
//                So disk is touched only when buffer is empty.

//        Internal Variables in BufferedReader
//        char[] cb;       // buffer
//        int nChars;      // number of valid chars in buffer
//        int nextChar;    // next char to read

//        How readLine() works internally
//        readLine() repeatedly reads from internal buffer until it finds: \n


//        Internal Working of BufferedWriter
//        Internal Buffer
//        BufferedWriter also creates an internal char array:
//        char[] buffer = new char[8192];  This stores output temporarily before writing to file.

//        when we do
//        writer.write('H');
//        writer.write('E');
//        writer.write('L');

//        it goes to internal buffer H E L _ _..

//        When actual disk write happens
//        Actual file write happens only when:
//
//        1. Buffer becomes full
//        If buffer fills up, Java writes whole buffer to file at once.

//        2. flush() is called
//        writer.flush();
//        Java forces buffered content to disk immediately.

//        3. close() is called

//        Internal Variables in BufferedWriter
//
//        Internally similar fields exist:
//
//        char[] cb;     // buffer
//        int nChars;    // buffer size
//        int nextChar;  // next write position
//
//        Example:
//
//        buffer   = [H E L L O _ _ _]
//        nextChar = 5

        System.out.println("Buffered Reader");

        BufferedReader reader = Files.newBufferedReader(Path.of(testPath));

        int ch = reader.read();
        System.out.println((char) ch);

//        If file starts with H, output:
//        H
//                Internally
//        If buffer has data → return next char from memory
//        If buffer empty → refill from file, then return char
//
//        So read() looks small, but usually does not hit disk every time.

//        A Reader maintains an internal cursor (current read position).
//        Each read starts from wherever that cursor currently is.
//                So if one character was already read earlier, cursor moves forward.
//        Then next read starts from second character.

//        2. read(char[] cbuf, int off, int len)
        char[] arr = new char[10];
        int n = reader.read(arr, 1, 5);

        System.out.println(new String(arr));
//        1. char[] buff
//        This is your destination buffer.
//                It is the array where characters will be placed.
//        Example:
//        char[] arr = new char[10];
//        This creates space for 10 characters.
//        2. int off
//        This tells Java:
//        “Start placing the read characters from this index in the array.”
//        So if off = 2, reading starts storing from arr[2].
//        Indexes before that remain unchanged.
//        3. int len
//        This tells Java:
//        “Read at most this many characters.”
//        It does not guarantee exactly that many will be read.
//        Java reads:
//        up to len characters
//        or less if file ends early

//        3. readLine()
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
//        Stops at:
//
//        \n
//        \r
//        \r\n
//        and returns line text without newline.

//        4. skip(long n)
        reader.skip(5);
        System.out.println((char) reader.read());

//        mark(int readAheadLimit)
//        mark(int readAheadLimit)

//        6. reset()
//        Returns to previously marked position.
//                reader.reset();
//
//        7. close()
//        reader.close();
//        Closes stream and releases resources.
//                After this, no more reading allowed.


//        BufferedWriter Methods
//
//        BufferedWriter is used for efficient text writing.
        String writePath = "C:\\Users\\sibasis.badatya\\Documents\\CourseManagement\\CourseManagementSBoot\\CourseManagement\\src\\main\\java\\com\\project\\CourseManagement\\practice\\FileHandling\\Nio\\TestW.txt";
//Buffered Writter
        System.out.println("Buffered Writer");
        BufferedWriter writer = Files.newBufferedWriter(Path.of(writePath));
        writer.write('A');
        writer.write('B');
//        Internally stores chars in buffer first, not immediately on disk.


//        . write(char[] cbuf, int off, int len)
        char[] arr1 = {'H', 'E', 'L', 'L', 'O'};
        writer.write(arr, 0, 5);
//        Writes HELLO. if off would have 1 the result would be ELLO
//        Internally
//        Copies array content into internal buffer.


//        3. write(String s)
        writer.write("Hello World");
//        String chars are copied into internal buffer.
//                Actual disk write may happen later.

//        4. newLine()
        writer.newLine();
        writer.write("Hello");
        writer.newLine();
        writer.write("World");
//        Output in file:
//        Hello
//        World
//        why not new line Because Windows uses: \r\n linux uses \n
//        5. flush()
        writer.flush();
//        Forces buffered content to be written to file immediately.
//                Internally
//        Whatever is waiting in memory buffer is pushed to disk.
//        Useful when data must be physically written now.

//        6. close()
//        Before closing, Java automatically calls: flush()

//        Streaming
//        --------------------------
//                FileInputStream
//                FileOutputStream

//        Newer one with nio
//        -------------------------------
//        InputStream is =
//                Files.newInputStream(Path.of("a.mp4"));

//        OutputStream os =
//                Files.newOutputStream(Path.of("b.mp4"));


    }

}

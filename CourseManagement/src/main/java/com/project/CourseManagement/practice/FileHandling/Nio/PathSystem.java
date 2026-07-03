package com.project.CourseManagement.practice.FileHandling.Nio;

public class PathSystem {
    public static void main(String[] args) {
//        # Java NIO `Path` and `Paths` Notes
//
//## 1) `Path` in Java NIO
//
//### Definition
//
//`Path` is an interface in `java.nio.file`.
//
//        It represents the path of a file or directory in the file system.
//
//                It does not perform file operations directly.
//        It only represents and manipulates the path.
//
//                ---
//
//### Syntax
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//```
//
//        or
//
//```java
//        Path path = Paths.get("C:/Users/Admin/file.txt");
//```
//
//        ---
//
//### Key Idea
//
//* `Path` = represents file/directory location
//* `Files` = performs operation on that location
//                * `Paths` = creates `Path`
//
//        ---
//
//## 2) Important Methods of `Path`
//
//        ---
//
//## `getFileName()`
//
//### Purpose
//
//        Returns the last element of the path.
//
//                Usually this is the file name (or last folder name).
//
//### Syntax
//
//```java
//        path.getFileName();
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//        System.out.println(path.getFileName());
//```
//
//### Output
//
//```java
//        file.txt
//```
//
//### Notes
//
//        Returns only the last part, not the full path.
//
//                ---
//
//## `getParent()`
//
//### Purpose
//
//        Returns the parent directory of the path.
//
//### Syntax
//
//```java
//        path.getParent();
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//        System.out.println(path.getParent());
//```
//
//### Output
//
//```java
//        C:\Users\Admin
//```
//
//### Notes
//
//        If no parent exists, it returns `null`.
//
//        ---
//
//## `getRoot()`
//
//### Purpose
//
//        Returns the root component of the path.
//
//### Syntax
//
//```java
//        path.getRoot();
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//        System.out.println(path.getRoot());
//```
//
//### Output (Windows)
//
//```java
//        C:\
//```
//
//### Notes
//
//                * Windows root → `C:\`
//* Linux root → `/`
//
//        ---
//
//## `isAbsolute()`
//
//### Purpose
//
//        Checks whether path is absolute.
//
//### Syntax
//
//```java
//        path.isAbsolute();
//```
//
//### Example
//
//```java
//        Path p1 = Path.of("file.txt");
//        Path p2 = Path.of("C:/Users/file.txt");
//
//        System.out.println(p1.isAbsolute());
//        System.out.println(p2.isAbsolute());
//```
//
//### Output
//
//```java
//        false
//        true
//```
//
//### Notes
//
//                * Absolute path = full path from root
//                * Relative path = depends on current working directory
//
//        ---
//
//## `toAbsolutePath()`
//
//### Purpose
//
//        Converts relative path into full absolute path.
//
//### Syntax
//
//```java
//        path.toAbsolutePath();
//```
//
//### Example
//
//```java
//        Path path = Path.of("notes.txt");
//        System.out.println(path.toAbsolutePath());
//```
//
//### Output
//
//```java
//        C:\project\notes.txt
//```
//
//### Notes
//
//        Useful when full system path is needed.
//
//                ---
//
//## `normalize()`
//
//### Purpose
//
//        Removes unnecessary parts like `.` and `..` and simplifies the path.
//
//### Syntax
//
//```java
//        path.normalize();
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/../Docs/file.txt");
//        System.out.println(path.normalize());
//```
//
//### Output
//
//```java
//        C:\Users\Docs\file.txt
//```
//
//### Notes
//
//                * `.` means current folder
//                * `..` means parent folder
//
//                ---
//
//## `resolve()`
//
//### Purpose
//
//        Combines one path with another.
//
//                Used to append child path to parent path.
//
//### Syntax
//
//```java
//        path.resolve("child");
//```
//
//### Example
//
//```java
//        Path base = Path.of("C:/Users/Admin");
//        Path full = base.resolve("file.txt");
//
//        System.out.println(full);
//```
//
//### Output
//
//```java
//        C:\Users\Admin\file.txt
//```
//
//### Notes
//
//        Used for dynamically building paths.
//
//                ---
//
//## `relativize()`
//
//### Purpose
//
//        Finds relative path from one path to another.
//
//### Syntax
//
//```java
//        path1.relativize(path2);
//```
//
//### Example
//
//```java
//        Path p1 = Path.of("C:/Users");
//        Path p2 = Path.of("C:/Users/Admin/file.txt");
//
//        System.out.println(p1.relativize(p2));
//```
//
//### Output
//
//```java
//        Admin\file.txt
//```
//
//### Notes
//
//        Useful for relative navigation.
//
//        ---
//
//## `getNameCount()`
//
//### Purpose
//
//        Returns number of name elements in path.
//
//### Syntax
//
//```java
//        path.getNameCount();
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//        System.out.println(path.getNameCount());
//```
//
//### Output
//
//```java
//        3
//```
//
//### Notes
//
//        Elements are:
//
//                * Users
//                * Admin
//                * file.txt
//
//        Root is not counted.
//
//        ---
//
//## `getName(int index)`
//
//### Purpose
//
//        Returns specific name element by index.
//
//### Syntax
//
//```java
//        path.getName(index);
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//
//        System.out.println(path.getName(0));
//        System.out.println(path.getName(1));
//        System.out.println(path.getName(2));
//```
//
//### Output
//
//```java
//                Users
//        Admin
//        file.txt
//```
//
//### Notes
//
//        Index starts from `0`.
//
//        ---
//
//## `subpath(int beginIndex, int endIndex)`
//
//### Purpose
//
//        Returns part of path.
//
//### Syntax
//
//```java
//        path.subpath(begin, end);
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//        System.out.println(path.subpath(0, 2));
//```
//
//### Output
//
//```java
//        Users\Admin
//```
//
//### Notes
//
//                * Root is not included
//                * `endIndex` is excluded
//
//        ---
//
//## `startsWith()`
//
//### Purpose
//
//        Checks whether path starts with given path.
//
//### Syntax
//
//```java
//        path.startsWith(otherPath);
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//        System.out.println(path.startsWith("C:/Users"));
//```
//
//### Output
//
//```java
//        true
//```
//
//        ---
//
//## `endsWith()`
//
//### Purpose
//
//        Checks whether path ends with given path.
//
//### Syntax
//
//```java
//        path.endsWith(otherPath);
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//        System.out.println(path.endsWith("file.txt"));
//```
//
//### Output
//
//```java
//        true
//```
//
//        ---
//
//## `toUri()`
//
//### Purpose
//
//        Converts path into URI format.
//
//### Syntax
//
//```java
//        path.toUri();
//```
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//        System.out.println(path.toUri());
//```
//
//### Output
//
//```java
//        file:///C:/Users/Admin/file.txt
//```
//
//### Notes
//
//        Useful in web/file resource handling.
//
//        ---
//
//## `toFile()`
//
//### Purpose
//
//        Converts `Path` to old `File` object.
//
//### Syntax
//
//```java
//        path.toFile();
//```
//
//### Example
//
//```java
//        File file = path.toFile();
//```
//
//### Notes
//
//        Useful when old APIs require `File`.
//
//        ---
//
//## `iterator()`
//
//### Purpose
//
//        Iterates through each path element.
//
//### Example
//
//```java
//        Path path = Path.of("C:/Users/Admin/file.txt");
//
//        for (Path p : path) {
//            System.out.println(p);
//        }
//```
//
//### Output
//
//```java
//                Users
//        Admin
//        file.txt
//```
//
//### Notes
//
//        Iterates only name elements, not root.
//
//                ---
//
//## `compareTo()`
//
//### Purpose
//
//        Compares two paths lexicographically.
//
//### Syntax
//
//```java
//        path1.compareTo(path2);
//```
//
//### Example
//
//```java
//        System.out.println(Path.of("a.txt").compareTo(Path.of("b.txt")));
//```
//
//### Notes
//
//                * negative → smaller
//                * zero → equal
//                * positive → greater
//
//                ---
//
//## 3) `Paths` Class in Java NIO
//
//### Definition
//
//`Paths` is a utility class in `java.nio.file`.
//
//        Used to create `Path` objects.
//
//                ---
//
//## `Paths.get()`
//
//### Purpose
//
//        Creates a `Path` object from string path.
//
//### Syntax
//
//```java
//        Paths.get("path");
//```
//
//### Example
//
//```java
//        Path path = Paths.get("C:/Users/Admin/file.txt");
//```
//
//### Notes
//
//        Most common in older Java versions.
//
//        From Java 11+, prefer:
//
//```java
//        Path.of("C:/Users/Admin/file.txt");
//```
//
//        ---
//
//## 4) `Path.of()` (Modern Alternative)
//
//### Purpose
//
//        Modern shortcut to create `Path`.
//
//### Syntax
//
//```java
//        Path.of("file.txt");
//```
//
//### Example
//
//```java
//        Path path = Path.of("notes.txt");
//```
//
//### Notes
//
//        Cleaner than `Paths.get()` and preferred in modern Java.
//
//                ---
//
//## Final Summary
//
//* `Path` represents the path
//                * `Paths` creates the path
//                * `Path.of()` is the modern way to create paths
//                * `Path` methods help inspect, compare, navigate, and transform paths

    }
}

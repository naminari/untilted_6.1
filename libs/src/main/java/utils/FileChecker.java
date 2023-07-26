package utils;

import java.io.File;

public class FileChecker {
    public static boolean checkFileToRead(File file) {
        return file.exists() && file.isFile() && file.canRead();
    }
    public static boolean checkFileToWrite(File file) {
        return file.exists() && file.isFile() && file.canWrite();
    }
}

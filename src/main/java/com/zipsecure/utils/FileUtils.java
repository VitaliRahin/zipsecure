package com.zipsecure.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

public final class FileUtils {
    public static byte[] readFileToBytes(String filename) throws IOException {
        Path path = Paths.get(filename);
        return Files.readAllBytes(path);
    }

    public static String writeBytesToFile(byte[] bytes, String filename) throws IOException {
        Path path = Paths.get(filename);
        return Files.write(path, bytes, StandardOpenOption.CREATE_NEW).toString();
    }

    public static ZipFile receiveZipFile(String fileName) throws ZipException {
        File file = new File(fileName);
        return new ZipFile(file);
    }

    public static ZipFile receiveZipFile(File file) throws ZipException {
        return new ZipFile(file);
    }

    public static String generateZipName(File initialFile, String path){
        return path + initialFile.getName() + LocalDate.now() + ".zip";
    }
    public static void deleteFiles (List<String> filePaths){
        for (String path : filePaths) {
            new File(path).delete();
        }
    }
}

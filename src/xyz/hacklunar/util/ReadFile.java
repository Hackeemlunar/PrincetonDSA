package xyz.hacklunar.util;

import algs4.ST;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFile {
    public static String read(String fileName) {
        String fileData;
        try {
            fileData = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return fileData;
    }

    public static String[] readAsStringArray(String filename) {
        return ReadFile.read(filename).split("\n");
    }
}

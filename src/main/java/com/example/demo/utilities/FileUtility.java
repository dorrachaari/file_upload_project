package com.example.demo.utilities;

import com.example.demo.entities.FileEntity;
import com.example.demo.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
public class FileUtility {

    private FileUtility () {}

    public static void validateFile(MultipartFile file, long maxSize, List<String> allowedExtensions) throws FileException {
        if (file == null) {
            throw new FileException("File is null");
        }

        if (file.isEmpty()) {
            throw new FileException("File is empty");
        }

        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new FileException("File name is invalid");
        }
        String extension = FilenameUtils.getExtension(fileName);
        if (!allowedExtensions.contains(extension)) {
            throw new FileException("File extension is not allowed");
        }

        if (file.getSize() > maxSize) {
            throw new FileException("File size exceeds the maximum allowed size");
        }
    }

    public static int getLinesCount(final FileEntity file) throws IOException {
        try (var br = getBufferedReader(file)) {
            return (int) br.lines().count();
        }
    }

    public static String getRandomLine(FileEntity file, int lineIndex) throws IOException {
        try (var br = getBufferedReader(file)) {
            int currentLineIndex = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if (currentLineIndex == lineIndex) {
                    return line;
                }
                currentLineIndex++;
            }
        }
        return null;
    }

    public static List<String> getLongestLinesFromFilesList(final List<FileEntity> files, final int max) throws IOException {
        List<String> longestLines = new ArrayList<>();
        for (FileEntity file : files) {
            getLongestLinesFromFile(file,max, longestLines);
        }
        return longestLines;
    }

    public static void getLongestLinesFromFile(FileEntity file,int max,List<String> longestLines ) throws IOException {
        try (var br = getBufferedReader(file)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (longestLines.size() < max) {
                    longestLines.add(line);
                } else {
                    int shortestIndex = getShortestIndex(longestLines);
                    if (line.length() > longestLines.get(shortestIndex).length()) {
                        longestLines.set(shortestIndex, line);
                    }
                }
            }
        }
    }

    private static int getShortestIndex(List<String> longestLines) {
        int shortestIndex = 0;
        for (int i = 1; i < longestLines.size(); i++) {
            if (longestLines.get(i).length() < longestLines.get(shortestIndex).length()) {
                shortestIndex = i;
            }
        }
        return shortestIndex;
    }

    private static BufferedReader getBufferedReader(FileEntity file) {
        InputStream is = new ByteArrayInputStream(file.getContent());
        InputStreamReader isr = new InputStreamReader(is);
        return new BufferedReader(isr);
    }


}

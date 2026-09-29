package com.datadoghq.workshops.samplevulnerablejavaapp.service;

import com.datadoghq.workshops.samplevulnerablejavaapp.exception.FileForbiddenFileException;
import com.datadoghq.workshops.samplevulnerablejavaapp.exception.FileReadException;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileService {
    final static String ALLOWED_PREFIX = "/tmp/files/";

    public String readFile(String path) throws FileForbiddenFileException, FileReadException {
    java.nio.file.Path basePath = java.nio.file.Paths.get(ALLOWED_PREFIX).toAbsolutePath().normalize();
    java.nio.file.Path targetPath = basePath.resolve(path).normalize();
    
    if (!targetPath.startsWith(basePath)) {
        throw new FileForbiddenFileException("You are not allowed to read " + path);
    }
    try (BufferedReader br = new BufferedReader(new FileReader(targetPath.toFile()))) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            throw new FileReadException(e.getMessage());
        }
    }
}

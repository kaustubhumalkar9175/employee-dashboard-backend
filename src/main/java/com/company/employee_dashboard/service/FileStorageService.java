package com.company.employee_dashboard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        // Create uploads folder if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename to avoid conflicts
        String extension  = getExtension(file.getOriginalFilename());
        String filename   = UUID.randomUUID().toString() + "." + extension;
        Path filePath     = uploadPath.resolve(filename);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    public void deleteFile(String filename) {
        if (filename == null || filename.isEmpty()) return;
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("Could not delete file: " + filename);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "jpg";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot + 1).toLowerCase() : "jpg";
    }
}
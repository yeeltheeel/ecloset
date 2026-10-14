package org.example.ecloset.service;

import org.example.ecloset.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.url-prefix}")
    private String urlPrefix;

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Файл пустой");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new FileStorageException("Недопустимый тип файла: " + file.getContentType());
        }

        String extension = extractExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + extension;

        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            file.transferTo(target);
        } catch (IOException e) {
            throw new FileStorageException("Не удалось сохранить файл", e);
        }

        return urlPrefix + "/" + filename;
    }

    public void delete(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith(urlPrefix + "/")) {
            return;
        }
        String filename = imageUrl.substring((urlPrefix + "/").length());
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(filename);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Не удалось удалить файл: " + path + " — " + e.getMessage());
        }
    }

    private String extractExtension(String originalName) {
        if (originalName == null) return "";
        int dot = originalName.lastIndexOf('.');
        if (dot < 0 || dot == originalName.length() - 1) return "";
        String ext = originalName.substring(dot).toLowerCase();
        return ext.length() <= 10 ? ext : "";
    }
}
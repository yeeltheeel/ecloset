package org.example.ecloset.service;

import org.example.ecloset.exception.FileStorageException;
import org.example.ecloset.exception.InvalidImageUrlException;
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

    /** Имя файла в хранилище: UUID + расширение. Заодно режет ../ и вложенные пути. */
    private static final String FILENAME_PATTERN = "[A-Za-z0-9._-]+";

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
        if (!filename.matches(FILENAME_PATTERN)) {
            return;
        }
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

    public String storeBytes(byte[] bytes, String extension) {
        String filename = UUID.randomUUID() + extension;
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            Files.write(target, bytes);
        } catch (IOException e) {
            throw new FileStorageException("Не удалось сохранить файл", e);
        }
        return urlPrefix + "/" + filename;
    }

    public String requireStoredFile(String imageUrl) {
        String prefix = urlPrefix + "/";
        if (imageUrl == null || !imageUrl.startsWith(prefix)) {
            throw new InvalidImageUrlException("imageUrl должен быть ссылкой, полученной из /api/items/analyze");
        }
        String filename = imageUrl.substring(prefix.length());
        if (!filename.matches(FILENAME_PATTERN)) {
            throw new InvalidImageUrlException("Некорректный imageUrl");
        }
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path file = dir.resolve(filename).normalize();
        if (!file.startsWith(dir) || !Files.isRegularFile(file)) {
            throw new InvalidImageUrlException("Файл не найден: сначала загрузите фото через /api/items/analyze");
        }
        return prefix + filename;
    }
}
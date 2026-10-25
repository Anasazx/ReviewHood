package tn.anasazx.tunirate.fileStorage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {

    // Allowed image content types -> extension used when the original name has no safe one
    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "image/gif", "gif",
            "image/avif", "avif"
    );

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) {

        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        String defaultExtension = ALLOWED_TYPES.get(contentType);

        if (file.isEmpty() || defaultExtension == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPEG, PNG, WEBP, GIF or AVIF images are allowed");
        }

        try {

            String fileName = UUID.randomUUID() + "_" + sanitizeFileName(file.getOriginalFilename(), defaultExtension);

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            return fileName;


        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }


    public void deleteFile(String fileName) {

        try {

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName).normalize();

            // Never delete anything outside the upload directory
            if (!filePath.startsWith(uploadPath)) {
                return;
            }

            Files.deleteIfExists(filePath);

        } catch (IOException | InvalidPathException e) {
            throw new RuntimeException("File delete failed", e);
        }
    }

    // Keeps only the base name with safe characters, and makes sure it ends with an image extension
    private String sanitizeFileName(String originalFileName, String defaultExtension) {

        String name = originalFileName == null ? "" : originalFileName;
        name = name.substring(Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1);
        name = name.replaceAll("[^A-Za-z0-9._-]", "_");

        if (name.length() > 100) {
            name = name.substring(name.length() - 100);
        }

        int dot = name.lastIndexOf('.');
        String extension = dot >= 0 ? name.substring(dot + 1).toLowerCase(Locale.ROOT) : "";

        if (!ALLOWED_TYPES.containsValue(extension) && !extension.equals("jpeg")) {
            name = name + "." + defaultExtension;
        }

        return name;
    }

}

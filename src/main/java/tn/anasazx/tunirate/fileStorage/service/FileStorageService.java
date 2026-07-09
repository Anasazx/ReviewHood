package tn.anasazx.tunirate.fileStorage.service;

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

    @Value("${file.base-url}")
    private String baseUrl;


    //This is saving the file with the whole name even with the host name
    //TODO: fix this to save only the file name
    public String saveFile(MultipartFile file) {

        try {

            String fileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();


            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }


            Path filePath = uploadPath.resolve(fileName);


            file.transferTo(filePath.toFile());


            // Return public URL
            return baseUrl + fileName;


        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }


    public void deleteFile(String fileUrl) {

        try {

            String fileName =
                    fileUrl.substring(fileUrl.lastIndexOf("/") + 1);


            Path filePath =
                    Paths.get(uploadDir).resolve(fileName);


            Files.deleteIfExists(filePath);


        } catch (IOException e) {
            throw new RuntimeException("File delete failed", e);
        }
    }
}
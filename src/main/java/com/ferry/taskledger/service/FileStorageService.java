package com.ferry.taskledger.service;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class FileStorageService {

    private final Path uploadRoot = Paths.get("uploads");

    public String store(Long taskId, MultipartFile file) {
        try {
            Path uploadRootPath = uploadRoot
                    .toAbsolutePath()
                    .normalize();

            String originalFileName = Paths
                    .get(file.getOriginalFilename())
                    .getFileName()
                    .toString();

            String storedFileName =
                    UUID.randomUUID() + "_" + originalFileName;

            Path relativePath = Paths.get(
                    "tasks",
                    taskId.toString(),
                    storedFileName
            );

            Path targetPath = uploadRootPath
                    .resolve(relativePath)
                    .normalize();

            Path taskDirectory = targetPath.getParent();

            Files.createDirectories(taskDirectory);

            if (!targetPath.startsWith(uploadRootPath)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return relativePath.toString().replace("\\", "/");

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public void delete(String filePath) {
        try {
            Path uploadRootPath = uploadRoot
                    .toAbsolutePath()
                    .normalize();

            Path targetPath = uploadRootPath
                    .resolve(filePath)
                    .normalize();

            if (!targetPath.startsWith(uploadRootPath)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            Files.deleteIfExists(targetPath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    public Resource load(String filePath) {
        try {
            Path uploadRootPath = uploadRoot
                    .toAbsolutePath()
                    .normalize();

            Path targetPath = uploadRootPath
                    .resolve(filePath)
                    .normalize();

            if (!targetPath.startsWith(uploadRootPath)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            if (!Files.exists(targetPath)) {
                throw new NoSuchElementException("File not found");
            }

            return new UrlResource(targetPath.toUri());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load file", e);
        }
    }
}
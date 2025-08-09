package org.csc.chessclub.service.storage;

import org.csc.chessclub.exception.StorageServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    public StorageServiceImpl(StorageProperties properties) {
        if (properties.getLocation().trim().isEmpty()) {
            throw new StorageServiceException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageServiceException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageServiceException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return destinationFile.toString();
        } catch (IOException e) {
            throw new StorageServiceException("Failed to store file.");
        }
    }

    @Override
    public Path load(String filename) {
        return this.rootLocation.resolve(filename);
    }
}

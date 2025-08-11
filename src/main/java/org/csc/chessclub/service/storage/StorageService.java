package org.csc.chessclub.service.storage;

import java.io.IOException;
import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  String store(MultipartFile file) throws IOException;

  Path load(String filename);
}

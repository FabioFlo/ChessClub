package org.csc.chessclub.service.storage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class StorageServiceUnitTests {

  private StorageServiceImpl storageService;
  private final String rootPath = "src/test/resources/pdf-folder";

  @BeforeEach
  public void setUp() {
    StorageProperties properties = new StorageProperties();
    properties.setLocation(rootPath);

    storageService = new StorageServiceImpl(properties);
  }

  @Test
  @DisplayName("Should store file correctly")
  public void testStoreFile_whenValidFileProvided_thenReturnFileName() throws IOException {
    String filename = "announcement.pdf";
    Path filePath = Paths.get(rootPath, filename);
    byte[] content = "content".getBytes();

    MultipartFile mockFile = mock(MultipartFile.class);
    when(mockFile.getOriginalFilename()).thenReturn(filename);
    when(mockFile.isEmpty()).thenReturn(false);
    when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(content));

    StorageProperties storageProperties = new StorageProperties();
    Path dir = Files.createTempDirectory("pdf-test");
    storageProperties.setLocation(dir.toString());

    storageService = new StorageServiceImpl(storageProperties);

    String result = storageService.store(mockFile);

    assertNotNull(result);
    assertTrue(Files.exists(filePath));
    assertTrue(result.endsWith(filename));
  }

  @Test
  @DisplayName("Should correctly load an existing file")
  public void testLoadFile_whenValidFilenameProvided_thenReturnPath() {
    String filename = "announcement.pdf";

    Path result = storageService.load(filename);

    assertNotNull(result);
    assertEquals(Path.of(rootPath).resolve(filename), result);
  }
}

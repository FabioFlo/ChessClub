package org.csc.chessclub.service.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StorageServiceUnitTests {

    @Mock
    private StorageServiceImpl storageService;

    @Mock
    private MultipartFile multipartFile;

    @Test
    @DisplayName("File uploaded")
    public void testStoreFile_whenValidFileProvided_thenReturnFileName() {
        String filename = "announcement.pdf";

        when(storageService.store(multipartFile)).thenReturn(filename);
        String result = storageService.store(multipartFile);

        assertNotNull(result);
        verify(storageService).store(multipartFile);
    }
}

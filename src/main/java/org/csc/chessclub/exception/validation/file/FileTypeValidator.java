package org.csc.chessclub.exception.validation.file;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileTypeValidator implements ConstraintValidator<ValidFile, MultipartFile> {

  private final Tika tika = new Tika();
  private final FileValidationProperties config;

  public FileTypeValidator(FileValidationProperties config) {
    this.config = config;
  }

  /**
   * By default, this annotation does not throw an exception if the file is null. If you want the
   * validation to fail, in case the file is null or empty, this logic should be modified in: 'if
   * (file == null || file.isEmpty()) { return true; }' to return false or a message.
   */
  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

    if (file == null || file.isEmpty()) {
      return true;
    }

    List<String> messages = new ArrayList<>();

    String filename = file.getOriginalFilename();
    if (filename == null) {
      messages.add(FileValidationMessage.NO_FILENAME);
    }

    List<String> allowedExts = config.getAllowedExtensions();
    List<String> allowedMimes = config.getAllowedMimeTypes();

    if (!allowedExts.isEmpty()) {
      assert filename != null;
      if (allowedExts.stream().noneMatch(filename.toLowerCase()::endsWith)) {
        messages.add(FileValidationMessage.EXTENSION_NOT_ALLOWED);
      }
    }

    try {
      String detectedType = tika.detect(file.getInputStream());
      if (!allowedMimes.isEmpty() && allowedMimes.stream()
          .noneMatch(detectedType::equalsIgnoreCase)) {
        messages.add(FileValidationMessage.MIME_NOT_CORRESPOND);
      }
    } catch (IOException e) {
      return false;
    }

    if (!messages.isEmpty()) {
      context.disableDefaultConstraintViolation();
      messages.forEach(message ->
          context
              .buildConstraintViolationWithTemplate(message)
              .addConstraintViolation());
      return false;
    }

    return true;
  }
}


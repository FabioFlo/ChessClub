package org.csc.chessclub.exception.validation.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * This class read the allowed file 'Extensions' and 'Mime Types' values that are under the
 * specified prefix in the properties file.
 * <p>
 * The Default prefix is: 'file.upload'. Example: file.upload.allowed-extensions=.pdf
 * file.upload.allowed-mime-types=application/pdf
 * <p>
 * If none of this is specified, every format will be allowed.
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "file.upload")
public class FileValidationProperties {


    private List<String> allowedExtensions = Collections.emptyList();

    private List<String> allowedMimeTypes = Collections.emptyList();

}

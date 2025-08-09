package org.csc.chessclub.exception.validation.file;

public final class FileValidationMessage {

    private FileValidationMessage() {
    }

    public static final String FILE_NOT_VALID = "File is not valid";
    public static final String NO_FILENAME = "No Filename specified";
    public static final String EXTENSION_NOT_ALLOWED = "Extension is not allowed";
    public static final String MIME_NOT_CORRESPOND = "Media types do not correspond with file extension";

}

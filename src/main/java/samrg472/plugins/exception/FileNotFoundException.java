package samrg472.plugins.exception;

import java.io.File;

/**
 * @author samrg472
 */
public class FileNotFoundException extends java.io.FileNotFoundException {

    public FileNotFoundException(File path) {
        super(String.format("No such file found: %s", path == null ? "path is null" : path.getAbsolutePath()));
    }

}
